package com.dentex.troller.chart

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapScaleTest {

    @Test
    fun picksTheLongestRoundDistanceThatFits() {
        // 100 px at 0.7 m/px holds 70 m, so the line is 50 m.
        val scale = MapScale.fit(metersPerUnit = 0.7, maxLength = 100.0)!!
        assertEquals(50.0, scale.meters, 1e-9)
        assertEquals(50 / 0.7, scale.length, 1e-9)
        assertEquals("50 m", scale.label)
    }

    @Test
    fun goesThroughOneTwoAndFive() {
        assertEquals("1 m", MapScale.fit(0.015, 100.0)!!.label)
        assertEquals("2 m", MapScale.fit(0.03, 100.0)!!.label)
        assertEquals("200 m", MapScale.fit(4.0, 100.0)!!.label)
        assertEquals("1 km", MapScale.fit(10.0, 100.0)!!.label)
        assertEquals("20 km", MapScale.fit(250.0, 100.0)!!.label)
    }

    @Test
    fun anExactlyRoundDistanceFillsTheWholeLength() {
        val scale = MapScale.fit(5.0, 100.0)!!
        assertEquals("500 m", scale.label)
        assertEquals(100.0, scale.length, 1e-9)
    }

    @Test
    fun noLineWhenEvenAMetreIsTooLongOrTheScaleIsUnknown() {
        assertNull(MapScale.fit(0.005, 100.0))
        assertNull(MapScale.fit(0.0, 100.0))
        assertNull(MapScale.fit(Double.NaN, 100.0))
        assertNull(MapScale.fit(Double.POSITIVE_INFINITY, 100.0))
    }
}
