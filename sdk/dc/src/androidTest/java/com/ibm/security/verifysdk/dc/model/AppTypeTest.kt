package com.ibm.security.verifysdk.dc.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AppTypeTest {

    @Test
    fun toString_method() {
        assertEquals("0", AppType._0.toString())
        assertEquals("1", AppType._1.toString())
    }

    @Test
    fun encode_method_with_valid_enum() {
        assertEquals("0", AppType.encode(AppType._0))
        assertEquals("1", AppType.encode(AppType._1))
    }

    @Test
    fun encode_method_with_invalid_data() {
        assertNull(AppType.encode("invalid"))
        assertNull(AppType.encode(123))
        assertNull(AppType.encode(null))
    }

    @Test
    fun decode_method_with_valid_values() {
        assertEquals(AppType._0, AppType.decode("0"))
        assertEquals(AppType._1, AppType.decode("1"))
    }

    @Test
    fun decode_method_with_invalid_values() {
        assertNull(AppType.decode("invalid"))
        assertNull(AppType.decode("2"))
        assertNull(AppType.decode(null))
    }
}