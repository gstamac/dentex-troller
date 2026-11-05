package com.dentex.troller

import android.content.Context
import java.io.File

/** A text file in the app's private storage that holds one library, such as the routes or the pins. */
class TextStore<T>(
    context: Context,
    private val fileName: String,
    private val empty: T,
    private val decode: (String) -> T,
    private val encode: (T) -> String,
) {

    private val file = File(context.filesDir, fileName)

    /**
     * Reads the library. A file that cannot be read is moved aside rather than overwritten, so
     * what is in it is not lost.
     */
    fun load(): T {
        if (!file.exists()) return empty
        return try {
            decode(file.readText())
        } catch (error: Exception) {
            file.renameTo(File(file.parentFile, "$fileName.unreadable-${System.currentTimeMillis()}"))
            empty
        }
    }

    /** Writes the library through a temporary file, so a crash cannot leave half a file. */
    fun save(value: T) {
        val part = File(file.parentFile, "$fileName.part")
        part.writeText(encode(value))
        if (!part.renameTo(file)) {
            file.delete()
            part.renameTo(file)
        }
    }
}
