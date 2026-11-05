package com.dentex.troller

import android.content.Context
import com.dentex.troller.ble.AndroidBleTransport
import com.dentex.troller.ble.BleScanner
import com.dentex.troller.logging.DiagnosticLog
import kotlinx.coroutines.CoroutineScope

/** The real Bluetooth link to the remote. */
internal object Links {
    fun create(context: Context, log: DiagnosticLog, scope: CoroutineScope): Link =
        Link(AndroidBleTransport(context, log, scope), BleScanner(context))
}
