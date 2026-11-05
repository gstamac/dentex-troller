package com.dentex.troller

import com.dentex.troller.ble.BleTransport
import com.dentex.troller.ble.DeviceScanner

/**
 * The link to the remote: the transport, the scanner that finds the remote, and in the sim build
 * the simulator behind them. Made by `Links.create`, which each build provides in its own source
 * set (`src/real` or `src/sim`), so only the sim build contains the simulator.
 */
class Link(
    val transport: BleTransport,
    val scanner: DeviceScanner,
    /** The simulated motor, or null on a real link. */
    val simulator: SimulatorControls? = null,
)

/** Controls of the simulated motor, for the buttons on the log screen. */
interface SimulatorControls {
    /** Runs a simulator command (see readme) and says what it did. */
    fun command(command: String, value: String? = null): String
}
