package com.dentex.troller.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/** The runtime permissions the Bluetooth scan and connection need. */
object BluetoothPermissions {

    val bluetooth: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    /**
     * Not needed to derive a position, but some vendor builds refuse to hand out BLE scan results
     * while it is missing, so it is requested as well. Null where [bluetooth] already includes it.
     */
    val location: String? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Manifest.permission.ACCESS_FINE_LOCATION else null

    /** Everything to ask for in one request. */
    val all: Array<String> = bluetooth + listOfNotNull(location)

    fun hasBluetooth(context: Context): Boolean = bluetooth.all { granted(context, it) }

    fun hasLocation(context: Context): Boolean = location == null || granted(context, location)

    private fun granted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}
