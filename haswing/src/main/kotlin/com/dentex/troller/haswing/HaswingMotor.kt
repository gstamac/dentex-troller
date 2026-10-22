package com.dentex.troller.haswing

import kotlinx.coroutines.flow.StateFlow

/**
 * A motor that this application can control.
 *
 * The interface is intentionally small: the diagnostics build only needs the identity and the
 * telemetry, and the propulsion methods stay behind explicit calls so that a read-only session
 * cannot accidentally move the boat.
 */
interface HaswingMotor {

    /** Identity of the motor, or `null` while no heartbeat has been received. */
    val identity: StateFlow<MotorIdentity?>

    /** Latest telemetry. */
    val telemetry: StateFlow<MotorTelemetry>

    /** The profile that the motor was identified as. */
    val profile: MotorProfile

    /**
     * Disables propulsion.
     *
     * This is the emergency stop and must work even when the profile has not been confirmed.
     */
    suspend fun requestStop(): CommandResult
}
