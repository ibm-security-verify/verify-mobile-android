package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OSTypeTest {

    @Test
    fun toString_method() {
        assertEquals("0", OSType._0.toString())
        assertEquals("1", OSType._1.toString())
    }

    @Test
    fun encode() {
        assertEquals("0", OSType.encode(OSType._0))
        assertEquals("1", OSType.encode(OSType._1))
    }

    @Test
    fun encode_withNullValue() {
        assertNull(OSType.encode(null))
    }

    @Test
    fun encode_withInvalidValue() {
        assertNull(OSType.encode("invalid"))
    }

    @Test
    fun decode() {
        assertEquals(OSType._0, OSType.decode("0"))
        assertEquals(OSType._1, OSType.decode("1"))

        assertEquals(OSType._0, OSType.decode("0"))
        assertEquals(OSType._1, OSType.decode("1"))
    }

    @Test
    fun decode_withNullValue() {
        assertNull(OSType.decode(null))
    }

    @Test
    fun decode_withInvalidValue() {
        assertNull(OSType.decode("invalid"))
    }

    @Test
    fun decode_withInvalidNumber() {
        assertNull(OSType.decode("2"))
    }

    @Test
    fun equal() {
        assertEquals(OSType._0, OSType.decode("0"))
        assertEquals(OSType._1, OSType.decode("1"))
    }
}