package com.dentex.troller

import android.content.Context
import com.dentex.troller.navigation.RouteLibrary
import java.io.File

/** Keeps the route library in the app's private storage. */
class RouteStore(context: Context) {

    private val file = File(context.filesDir, FILE_NAME)

    /**
     * Reads the saved routes. A file that cannot be read is moved aside rather than overwritten,
     * so the routes in it are not lost.
     */
    fun load(): RouteLibrary {
        if (!file.exists()) return RouteLibrary()
        return try {
            RouteLibrary.decode(file.readText())
        } catch (error: Exception) {
            file.renameTo(File(file.parentFile, "$FILE_NAME.unreadable-${System.currentTimeMillis()}"))
            RouteLibrary()
        }
    }

    /** Writes the library through a temporary file, so a crash cannot leave half a file. */
    fun save(library: RouteLibrary) {
        val part = File(file.parentFile, "$FILE_NAME.part")
        part.writeText(library.encode())
        if (!part.renameTo(file)) {
            file.delete()
            part.renameTo(file)
        }
    }

    private companion object {
        const val FILE_NAME = "routes.txt"
    }
}
