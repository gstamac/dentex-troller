package com.dentex.troller.chart

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Locates the offline chart file and imports a new one.
 *
 * The chart is too large to ship inside the APK, and MapLibre reads PMTiles only from a real
 * file, so it lives in the app-specific external storage:
 * `Android/data/com.dentex.troller/files/charts/adriatic.pmtiles`. It gets there either with
 * `adb push` or by importing it from the phone's storage in the chart screen.
 */
class ChartStore(private val context: Context) {

    /** Where the chart is expected. */
    val file: File
        get() {
            val directory = context.getExternalFilesDir(DIRECTORY) ?: File(context.filesDir, DIRECTORY)
            directory.mkdirs()
            return File(directory, FILE_NAME)
        }

    /** The installed chart, or `null` when none is installed or the file is not a PMTiles archive. */
    fun current(): InstalledChart? {
        val candidate = file
        if (!candidate.isFile || candidate.length() < PmtilesHeader.LENGTH) return null
        val header = ByteArray(PmtilesHeader.LENGTH)
        val read = candidate.inputStream().use { it.read(header) }
        if (read != header.size || !PmtilesHeader.isVersion3(header)) return null
        return InstalledChart(candidate, candidate.length(), candidate.lastModified())
    }

    /** An installed chart; size and time make a replaced file a different value. */
    data class InstalledChart(val file: File, val bytes: Long, val modifiedMillis: Long)

    /**
     * Copies the chart at [uri] into place.
     *
     * The copy goes to a temporary file first, so a failed or rejected import keeps the chart
     * that was installed before.
     */
    suspend fun import(uri: Uri): ImportResult = withContext(Dispatchers.IO) {
        val target = file
        val temporary = File(target.parentFile, "$FILE_NAME.part")
        try {
            val input = context.contentResolver.openInputStream(uri)
                ?: return@withContext ImportResult.Failed("The selected file could not be opened")
            input.use { source -> temporary.outputStream().use { source.copyTo(it, BUFFER_SIZE) } }

            val header = ByteArray(PmtilesHeader.LENGTH)
            val read = temporary.inputStream().use { it.read(header) }
            if (read != header.size || !PmtilesHeader.isVersion3(header)) {
                temporary.delete()
                return@withContext ImportResult.Failed("The selected file is not a PMTiles chart")
            }
            if (target.exists() && !target.delete()) {
                temporary.delete()
                return@withContext ImportResult.Failed("The old chart could not be replaced")
            }
            if (!temporary.renameTo(target)) {
                temporary.delete()
                return@withContext ImportResult.Failed("The chart could not be moved into place")
            }
            ImportResult.Imported(target.length())
        } catch (error: IOException) {
            temporary.delete()
            ImportResult.Failed("Import failed: ${error.message}")
        } catch (error: SecurityException) {
            temporary.delete()
            ImportResult.Failed("Import failed: ${error.message}")
        }
    }

    sealed interface ImportResult {
        data class Imported(val bytes: Long) : ImportResult
        data class Failed(val reason: String) : ImportResult
    }

    companion object {
        const val DIRECTORY = "charts"
        const val FILE_NAME = "adriatic.pmtiles"
        private const val BUFFER_SIZE = 1 shl 16
    }
}

/** The fixed 127 byte header at the start of every PMTiles archive. */
object PmtilesHeader {
    const val LENGTH = 127
    private val MAGIC = "PMTiles".toByteArray(Charsets.US_ASCII)

    /** True when [header] starts with the PMTiles magic followed by spec version 3. */
    fun isVersion3(header: ByteArray): Boolean =
        header.size >= MAGIC.size + 1 &&
            MAGIC.indices.all { header[it] == MAGIC[it] } &&
            header[MAGIC.size].toInt() == 3
}
