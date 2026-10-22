package com.dentex.troller.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge

/**
 * Scans for nearby devices.
 *
 * The remote control of the motor is the BLE peer, and it does not necessarily advertise the
 * GATT service in its advertisement packet. The Helmsman application therefore scans without any
 * filter and lists everything that answers. The filter is only offered as an optional convenience
 * and is off by default.
 */
class BleScanner(private val context: Context) {

    private val adapter: BluetoothAdapter?
        get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter

    val isBluetoothEnabled: Boolean get() = adapter?.isEnabled == true

    /**
     * Emits every advertisement seen while the flow is collected.
     *
     * The modern scanner and the legacy scan API are both used and their results are merged.
     * The Helmsman application only uses the legacy API, and on some Android versions that API
     * also reports peers that the modern scanner does not surface. Running both therefore
     * covers every case the reference application can see.
     *
     * @param filterByService when true the scan requests the motor service UUID as a filter.
     *   Only use this when the peer is known to advertise it, otherwise the peer is invisible.
     */
    @SuppressLint("MissingPermission")
    fun scan(filterByService: Boolean = false): Flow<BleScanResult> {
        val scanner: BluetoothLeScanner? = adapter?.bluetoothLeScanner
        val modern = if (scanner == null) {
            flowOf()
        } else {
            modernScan(scanner, filterByService)
        }
        return merge(modern, legacyScan())
    }

    @SuppressLint("MissingPermission")
    private fun modernScan(scanner: BluetoothLeScanner, filterByService: Boolean): Flow<BleScanResult> =
        callbackFlow {
            val filters = if (filterByService) {
                BleUuids.SCAN_SERVICE_FILTERS.map { uuid ->
                    ScanFilter.Builder().setServiceUuid(ParcelUuid(uuid)).build()
                }
            } else {
                emptyList()
            }

            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

            val callback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    trySend(result.toBleScanResult())
                }

                override fun onBatchScanResults(results: MutableList<ScanResult>) {
                    results.forEach { trySend(it.toBleScanResult()) }
                }

                override fun onScanFailed(errorCode: Int) {
                    close(IllegalStateException("BLE scan failed with error code $errorCode"))
                }
            }

            scanner.startScan(filters, settings, callback)
            awaitClose { runCatching { scanner.stopScan(callback) } }
        }

    /**
     * Legacy scan, used alongside the modern scanner.
     *
     * The Helmsman application uses this API and it reports the same devices, including peers
     * that do not advertise a service UUID. Failures are swallowed so that the modern scanner
     * keeps working on its own.
     */
    @SuppressLint("MissingPermission", "DEPRECATION")
    private fun legacyScan(): Flow<BleScanResult> {
        val bluetoothAdapter = adapter ?: return flowOf()
        if (!bluetoothAdapter.isEnabled) return flowOf()
        return callbackFlow {
            val callback = object : BluetoothAdapter.LeScanCallback {
                override fun onLeScan(device: BluetoothDevice?, rssi: Int, data: ByteArray?) {
                    if (device != null) {
                        trySend(
                            BleScanResult(
                                deviceId = device.address,
                                deviceName = device.safeName(),
                                rssi = rssi,
                            ),
                        )
                    }
                }
            }
            try {
                if (!bluetoothAdapter.startLeScan(callback)) {
                    close()
                }
            } catch (_: SecurityException) {
                close()
            }
            awaitClose { runCatching { bluetoothAdapter.stopLeScan(callback) } }
        }
    }

    @SuppressLint("MissingPermission")
    private fun ScanResult.toBleScanResult(): BleScanResult {
        val name = try {
            scanRecord?.deviceName ?: device.safeName()
        } catch (_: SecurityException) {
            null
        }
        return BleScanResult(
            deviceId = device.address,
            deviceName = name,
            rssi = rssi,
            serviceUuids = scanRecord?.serviceUuids?.map { it.uuid.toString() } ?: emptyList(),
        )
    }

    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    private fun BluetoothDevice.safeName(): String? = try {
        name
    } catch (_: SecurityException) {
        null
    }
}
