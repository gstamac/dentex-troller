package com.dentex.troller

import android.content.Context
import com.dentex.troller.navigation.Track
import java.io.File

/**
 * The recorded track in the app's private storage. While recording, each point is appended as
 * one line, so a long track is never rewritten as a whole.
 */
class TrackStore(context: Context) {

    private val file = File(context.filesDir, FILE_NAME)

    /** Reads the track. A file that cannot be read is moved aside rather than overwritten. */
    fun load(): Track {
        if (!file.exists()) return Track()
        return try {
            Track.decode(file.readText())
        } catch (error: Exception) {
            file.renameTo(File(file.parentFile, "$FILE_NAME.unreadable-${System.currentTimeMillis()}"))
            Track()
        }
    }

    /** Adds [lines] at the end of the file, which is started with its header when missing. */
    fun append(lines: List<String>) {
        val text = lines.joinToString(separator = "\n", postfix = "\n")
        if (file.exists()) file.appendText(text) else file.writeText(Track.HEADER + "\n" + text)
    }

    /** Writes the whole [track] through a temporary file, so a crash cannot leave half a file. */
    fun replace(track: Track) {
        val part = File(file.parentFile, "$FILE_NAME.part")
        part.writeText(track.encode())
        if (!part.renameTo(file)) {
            file.delete()
            part.renameTo(file)
        }
    }

    private companion object {
        const val FILE_NAME = "track.txt"
    }
}
