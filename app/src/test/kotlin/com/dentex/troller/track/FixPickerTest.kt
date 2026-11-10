package com.dentex.troller.track

import com.dentex.troller.chart.PositionSource
import com.dentex.troller.navigation.TrackPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FixPickerTest {

    private fun at(seconds: Long, latitude: Double = 44.2) = TrackPoint(latitude, 14.8, seconds * 1000)

    @Test
    fun thePhoneIsRecordedWithoutTheMotor() {
        val picker = FixPicker()
        assertTrue(picker.phone(at(1)))
    }

    @Test
    fun thePhoneIsLeftOutWhileTheMotorIsFresh() {
        val picker = FixPicker()
        picker.motor(at(10))
        assertFalse(picker.phone(at(12)))
        assertFalse(picker.phone(at(15)))
    }

    @Test
    fun thePhoneTakesOverOnceTheMotorGoesQuiet() {
        val picker = FixPicker()
        picker.motor(at(10))
        assertTrue(picker.phone(at(16)))
    }

    @Test
    fun aTrackStartsFromTheFreshestFixTheMotorFirst() {
        val picker = FixPicker()
        assertNull(picker.freshest(1_000))
        picker.phone(at(10, latitude = 44.1))
        assertEquals(PositionSource.PHONE, picker.freshest(12_000)!!.second)
        picker.motor(at(11, latitude = 44.3))
        val (point, source) = picker.freshest(12_000)!!
        assertEquals(PositionSource.MOTOR, source)
        assertEquals(44.3, point.latitude, 0.0)
        // The motor quiet, the phone still fresh.
        picker.phone(at(17, latitude = 44.1))
        assertEquals(PositionSource.PHONE, picker.freshest(18_000)!!.second)
        // Nothing fresh at all.
        assertNull(picker.freshest(60_000))
    }
}
