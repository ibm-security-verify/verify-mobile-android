package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InteractionModeTest {

    @Test
    fun toString_method() {
        assertEquals("approved", InteractionMode.APPROVED.toString())
        assertEquals("rejected", InteractionMode.REJECTED.toString())
        assertEquals("manual", InteractionMode.MANUAL.toString())
    }

    @Test
    fun encode() {
        assertEquals("approved", InteractionMode.encode(InteractionMode.APPROVED))
        assertEquals("rejected", InteractionMode.encode(InteractionMode.REJECTED))
        assertEquals("manual", InteractionMode.encode(InteractionMode.MANUAL))
    }

    @Test
    fun encode_withNullValue()
    {
        assertNull(InteractionMode.encode(null))
    }

    @Test
    fun encode_withInvalidValue() {
        assertNull(InteractionMode.encode("invalid"))
    }

    @Test
    fun decode() {
        assertEquals(InteractionMode.APPROVED, InteractionMode.decode("approved"))
        assertEquals(InteractionMode.REJECTED, InteractionMode.decode("rejected"))
        assertEquals(InteractionMode.MANUAL, InteractionMode.decode("manual"))
    }


    @Test
    fun decode_withCaseInsensitive() {
        assertEquals(InteractionMode.APPROVED, InteractionMode.decode("Approved"))
        assertEquals(InteractionMode.REJECTED, InteractionMode.decode("Rejected"))
        assertEquals(InteractionMode.MANUAL, InteractionMode.decode("Manual"))
    }

    @Test
    fun decode_withInvalidValue() {
        assertNull(InteractionMode.decode("invalid"))
    }

    @Test
    fun decode_withNullValue() {
        assertNull(InteractionMode.decode(null))
    }

    @Test
    fun equal() {
        assertEquals(InteractionMode.APPROVED, InteractionMode.decode("APPROVED"))
        assertEquals(InteractionMode.REJECTED, InteractionMode.decode("REJECTED"))
        assertEquals(InteractionMode.MANUAL, InteractionMode.decode("MANUAL"))
    }
}