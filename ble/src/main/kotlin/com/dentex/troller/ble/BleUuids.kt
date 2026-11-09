package com.dentex.troller.ble

import java.util.UUID

/**
 * GATT identifiers used by the Haswing motor link.
 *
 * The service and characteristic UUIDs were recovered from the Helmsman APK
 * (`BleBlueBoatDataIOController`). They still need to be confirmed against a real
 * motor with a service discovery dump before they are treated as authoritative.
 */
object BleUuids {

    /** Primary serial service exposed by the motor's BLE module. */
    val SERVICE: UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")

    /** Characteristic used for phone to motor traffic. */
    val TX_CHARACTERISTIC: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")

    /** Characteristic used for motor to phone traffic (notifications). */
    val RX_CHARACTERISTIC: UUID = UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb")

    /** Standard Client Characteristic Configuration descriptor. */
    val CLIENT_CHARACTERISTIC_CONFIG: UUID =
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    /** Service UUIDs used to filter the scan, in addition to the device name prefix. */
    val SCAN_SERVICE_FILTERS: List<UUID> = listOf(SERVICE)

    /**
     * Name prefix the remote's Bluetooth module (an HM-10 type, for example "HM_BB65") advertises.
     * Its advertisement carries no service UUID, so the connect list uses the name to put it first.
     */
    const val REMOTE_NAME_PREFIX: String = "HM_"

    /** Whether [name] looks like the remote's Bluetooth module. */
    fun isRemoteName(name: String?): Boolean = name?.startsWith(REMOTE_NAME_PREFIX, ignoreCase = true) == true
}
