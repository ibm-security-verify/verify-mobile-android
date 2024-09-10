package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnvTypeTest {

    @Test
    fun toString_method() {
        assertEquals("0", EnvType._0.toString())
        assertEquals("1", EnvType._1.toString())
    }

    @Test
    fun encode() {
        assertEquals("0", EnvType.encode(EnvType._0))
        assertEquals("1", EnvType.encode(EnvType._1))
    }

    @Test
    fun encode_withNullValue() {
        assertNull(EnvType.encode(null))
    }

    @Test
    fun encode_withInvalidValue() {
        assertNull(EnvType.encode("invalid"))
    }

    @Test
    fun decode() {
        assertEquals(EnvType._0, EnvType.decode("0"))
        assertEquals(EnvType._1, EnvType.decode("1"))

        assertEquals(EnvType._0, EnvType.decode("0"))
        assertEquals(EnvType._1, EnvType.decode("1"))
    }

    @Test
    fun decode_withNullValue() {
        assertNull(EnvType.decode(null))
    }

    @Test
    fun decode_withInvalidValue() {
        assertNull(EnvType.decode("invalid"))
    }

    @Test
    fun decode_withInvalidNumber() {
        assertNull(EnvType.decode("2"))
    }

    @Test
    fun equal() {
        assertEquals(EnvType._0, EnvType.decode("0"))
        assertEquals(EnvType._1, EnvType.decode("1"))
    }
}