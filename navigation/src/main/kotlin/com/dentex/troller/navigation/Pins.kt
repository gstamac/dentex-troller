package com.dentex.troller.navigation

import java.io.ByteArrayInputStream
import java.time.Instant
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element

/** A marked spot, for example a fishing spot imported from another chart app. */
data class Pin(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    /** When the spot was marked, or null when unknown. */
    val timeMillis: Long?,
) {
    /** Same name at the same spot, to within about 1 m. */
    fun sameAs(other: Pin): Boolean =
        name == other.name &&
            Math.abs(latitude - other.latitude) < SAME_SPOT_DEGREES &&
            Math.abs(longitude - other.longitude) < SAME_SPOT_DEGREES

    private companion object {
        const val SAME_SPOT_DEGREES = 0.00001
    }
}

/** All pins kept on the phone. */
data class PinLibrary(val pins: List<Pin> = emptyList()) {

    /** Adds the pins that aren't there yet; returns the library and how many were added. */
    fun merge(incoming: List<Pin>): Pair<PinLibrary, Int> {
        val added = mutableListOf<Pin>()
        for (pin in incoming) {
            if (pins.none { it.sameAs(pin) } && added.none { it.sameAs(pin) }) added += pin
        }
        return PinLibrary(pins + added) to added.size
    }

    fun delete(id: String): PinLibrary = PinLibrary(pins.filterNot { it.id == id })

    fun encode(): String = buildString {
        appendLine(HEADER)
        pins.forEach { pin ->
            appendLine(String.format(Locale.US, "pin %s %.7f %.7f %s", pin.id, pin.latitude, pin.longitude, pin.timeMillis ?: NONE))
            appendLine("name ${pin.name}")
        }
    }

    companion object {
        private const val HEADER = "dentex-pins 1"
        private const val NONE = "-"

        /** Reads [encode]'s text. Throws [IllegalArgumentException] when it is not a pin file. */
        fun decode(text: String): PinLibrary {
            val lines = text.lines().map { it.trimEnd() }.filter { it.isNotEmpty() }
            require(lines.firstOrNull() == HEADER) { "Not a pin file" }
            val pins = mutableListOf<Pin>()
            for (line in lines.drop(1)) {
                val rest = line.substringAfter(' ', "")
                when (line.substringBefore(' ')) {
                    "pin" -> {
                        val parts = rest.split(' ')
                        require(parts.size == 4) { "Bad pin line: $line" }
                        pins += Pin(parts[0], "", parts[1].toDouble(), parts[2].toDouble(), parts[3].takeIf { it != NONE }?.toLong())
                    }
                    "name" -> pins[pins.lastIndex] = pins.last().copy(name = rest)
                    else -> Unit // Written by a newer version; skipped.
                }
            }
            return PinLibrary(pins)
        }
    }
}

/** What a GPX file holds that the app can use. */
data class GpxContent(
    val pins: List<Pin>,
    /** Named routes, from `<rte>`. */
    val routes: List<Pair<String, List<Waypoint>>>,
)

/** Reads GPX 1.0 and 1.1 files, such as the export of the Navionics Boating app. */
object Gpx {

    /** Throws [IllegalArgumentException] when the bytes are not a GPX file. */
    fun parse(bytes: ByteArray, newId: () -> String): GpxContent {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
            // A GPX file never needs a DTD; refusing them keeps external entities out.
            trySetFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            trySetFeature("http://xml.org/sax/features/external-general-entities", false)
            trySetFeature("http://xml.org/sax/features/external-parameter-entities", false)
            isExpandEntityReferences = false
        }
        val root = try {
            factory.newDocumentBuilder().parse(ByteArrayInputStream(bytes)).documentElement
        } catch (error: Exception) {
            throw IllegalArgumentException("Not a GPX file", error)
        }
        require(root.localName == "gpx" || root.tagName == "gpx") { "Not a GPX file" }

        val pins = children(root, "wpt").mapNotNull { wpt ->
            val point = waypointOf(wpt) ?: return@mapNotNull null
            Pin(
                id = newId(),
                name = RouteLibrary.cleanName(text(wpt, "name") ?: "").ifEmpty { "Pin" },
                latitude = point.latitude,
                longitude = point.longitude,
                timeMillis = text(wpt, "time")?.let { runCatching { Instant.parse(it).toEpochMilli() }.getOrNull() },
            )
        }
        val routes = children(root, "rte").mapIndexedNotNull { index, rte ->
            val points = children(rte, "rtept").mapNotNull(::waypointOf)
            if (points.isEmpty()) null else (RouteLibrary.cleanName(text(rte, "name") ?: "").ifEmpty { "GPX route ${index + 1}" } to points)
        }
        return GpxContent(pins, routes)
    }

    private fun DocumentBuilderFactory.trySetFeature(name: String, value: Boolean) {
        runCatching { setFeature(name, value) }
    }

    private fun children(parent: Element, name: String): List<Element> {
        val nodes = parent.childNodes
        return (0 until nodes.length).mapNotNull { nodes.item(it) as? Element }
            .filter { (it.localName ?: it.tagName) == name }
    }

    private fun text(parent: Element, name: String): String? =
        children(parent, name).firstOrNull()?.textContent?.trim()?.takeIf { it.isNotEmpty() }

    private fun waypointOf(element: Element): Waypoint? {
        val latitude = element.getAttribute("lat").toDoubleOrNull() ?: return null
        val longitude = element.getAttribute("lon").toDoubleOrNull() ?: return null
        return runCatching { Waypoint(latitude, longitude) }.getOrNull()
    }
}
