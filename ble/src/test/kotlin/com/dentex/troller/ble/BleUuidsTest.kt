package com.dentex.troller.ble

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BleUuidsTest {

    @Test
    fun theRemoteIsRecognisedByItsModuleName() {
        assertTrue(BleUuids.isRemoteName("HM_BB65"))
        assertTrue(BleUuids.isRemoteName("hm_bb65"))
    }

    @Test
    fun otherNamesAndNoNameAreNotTheRemote() {
        assertFalse(BleUuids.isRemoteName(null))
        assertFalse(BleUuids.isRemoteName(""))
        assertFalse(BleUuids.isRemoteName("HM10"))
        assertFalse(BleUuids.isRemoteName("Galaxy Buds"))
    }
}
