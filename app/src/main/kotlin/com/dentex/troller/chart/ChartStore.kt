package com.dentex.troller.chart

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Locates the offline chart files and imports new ones.
 *
 * The charts are too large to ship inside the APK, and MapLibre reads PMTiles only from a real
 * file, so they live in the app-specific external storage,
 * `Android/data/com.dentex.troller/files/charts/`. They get there either with `adb push` or by
 * importing them from the phone's storage (Settings, Charts, or the chart screen while none is installed).
 */
class ChartStore(private val context: Context) {

    /** Where the chart file of [kind] is expected. */
    fun file(kind: ChartKind): File {
        val directory = context.getExternalFilesDir(DIRECTORY) ?: File(context.filesDir, DIRECTORY)
        directory.mkdirs()
        return File(directory, kind.fileName)
    }

    /** The installed file of [kind], or `null` when it is missing or not that kind of PMTiles archive. */
    fun current(kind: ChartKind): InstalledChart? {
        val candidate = file(kind)
        if (!candidate.isFile || candidate.length() < PmtilesHeader.LENGTH) return null
        val header = ByteArray(PmtilesHeader.LENGTH)
        val read = candidate.inputStream().use { it.read(header) }
        if (read != header.size || PmtilesHeader.kind(header) != kind) return null
        return InstalledChart(candidate, candidate.length(), candidate.lastModified())
    }

    /** An installed chart file; size and time make a replaced file a different value. */
    data class InstalledChart(val file: File, val bytes: Long, val modifiedMillis: Long)

    /**
     * Copies the chart at [uri] into place. Its tile type decides which file it replaces.
     *
     * The copy goes to a temporary file first, so a failed or rejected import keeps the file
     * that was installed before.
     */
    suspend fun import(uri: Uri): ImportResult = withContext(Dispatchers.IO) {
        val directory = file(ChartKind.DEPTHS).parentFile
        val temporary = File(directory, "import.part")
        try {
            val input = context.contentResolver.openInputStream(uri)
                ?: return@withContext ImportResult.Failed("The selected file could not be opened")
            input.use { source -> temporary.outputStream().use { source.copyTo(it, BUFFER_SIZE) } }

            val header = ByteArray(PmtilesHeader.LENGTH)
            val read = temporary.inputStream().use { it.read(header) }
            val kind = if (read == header.size) PmtilesHeader.kind(header) else null
            if (kind == null) {
                temporary.delete()
                return@withContext ImportResult.Failed("The selected file is not a PMTiles chart")
            }
            val target = file(kind)
            if (target.exists() && !target.delete()) {
                temporary.delete()
                return@withContext ImportResult.Failed("The old chart could not be replaced")
            }
            if (!temporary.renameTo(target)) {
                temporary.delete()
                return@withContext ImportResult.Failed("The chart could not be moved into place")
            }
            ImportResult.Imported(kind, target.length())
        } catch (error: IOException) {
            temporary.delete()
            ImportResult.Failed("Import failed: ${error.message}")
        } catch (error: SecurityException) {
            temporary.delete()
            ImportResult.Failed("Import failed: ${error.message}")
        }
    }

    sealed interface ImportResult {
        data class Imported(val kind: ChartKind, val bytes: Long) : ImportResult
        data class Failed(val reason: String) : ImportResult
    }

    companion object {
        const val DIRECTORY = "charts"
        private const val BUFFER_SIZE = 1 shl 16
    }
}

/** The chart files built by `tools/charts/build-chart.sh`. */
enum class ChartKind(val fileName: String, val title: String) {
    /** Vector tiles: land, depth bands and contours. */
    DEPTHS("adriatic.pmtiles", "Chart"),

    /** Raster tiles: depths in EMODnet's rainbow colour ramp. */
    RAINBOW("adriatic-rainbow.pmtiles", "Rainbow depth layer"),
}

/** The fixed 127 byte header at the start of every PMTiles archive. */
object PmtilesHeader {
    const val LENGTH = 127
    private val MAGIC = "PMTiles".toByteArray(Charsets.US_ASCII)

    /** Offset of the tile type byte in a version 3 header. */
    const val TILE_TYPE_OFFSET = 99
    const val TILE_TYPE_MVT = 1
    const val TILE_TYPE_PNG = 2
    const val TILE_TYPE_JPEG = 3
    const val TILE_TYPE_WEBP = 4

    /** True when [header] starts with the PMTiles magic followed by spec version 3. */
    fun isVersion3(header: ByteArray): Boolean =
        header.size >= MAGIC.size + 1 &&
            MAGIC.indices.all { header[it] == MAGIC[it] } &&
            header[MAGIC.size].toInt() == 3

    /** Which chart file a version 3 [header] belongs to, or `null` for anything else. */
    fun kind(header: ByteArray): ChartKind? {
        if (!isVersion3(header) || header.size <= TILE_TYPE_OFFSET) return null
        return when (header[TILE_TYPE_OFFSET].toInt()) {
            TILE_TYPE_MVT -> ChartKind.DEPTHS
            TILE_TYPE_PNG, TILE_TYPE_JPEG, TILE_TYPE_WEBP -> ChartKind.RAINBOW
            else -> null
        }
    }
}

/** The base look of the chart, picked from the layers menu. */
enum class ChartLayer(val title: String, val needs: ChartKind?) {
    /** The vector depth bands and contours. */
    DEFAULT("Default", null),

    /** The rainbow depth raster over the depth bands. */
    RAINBOW("Rainbow depths", ChartKind.RAINBOW),

    /** The contours on plain water, without the depth shading. */
    LINES("Contour lines only", null),
}
