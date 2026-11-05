package com.dentex.troller.ble

import kotlinx.coroutines.flow.Flow

/** Finds remotes to connect to: [BleScanner] on a phone, or a simulated remote in the sim build. */
interface DeviceScanner {

    /** False when Bluetooth is switched off, so a scan would find nothing. */
    val isBluetoothEnabled: Boolean

    /** Emits every device seen while the flow is collected. */
    fun scan(filterByService: Boolean = false): Flow<BleScanResult>
}
