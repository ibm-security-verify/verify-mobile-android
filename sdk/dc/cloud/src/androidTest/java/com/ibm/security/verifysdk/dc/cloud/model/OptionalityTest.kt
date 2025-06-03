package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OptionalityTest {

    @Test
    fun serialization() {
        val optionality = Optionality.PREFERRED
        val jsonString = json.encodeToString(optionality)
        assertEquals("\"preferred\"", jsonString)
    }

    @Test
    fun deserialization() {
        val jsonString = "\"required\""
        val deserializedOptionality = json.decodeFromString<Optionality>(jsonString)
        assertEquals(Optionality.REQUIRED, deserializedOptionality)
    }

    @Test
    fun encode() {
        val encodedValue = Optionality.encode(Optionality.PREFERRED)
        assertEquals("preferred", encodedValue)
    }

    @Test
    fun decode() {
        val decodedValue = Optionality.decode("required")
        assertEquals(Optionality.REQUIRED, decodedValue)
    }

    @Test
    fun decode_withInvalidData() {
        val decodedValue = Optionality.decode("invalid")
        assertNull(decodedValue)
    }

    @Test
    fun toString_method() {
        assertEquals("required", Optionality.REQUIRED.toString())
        assertEquals("preferred", Optionality.PREFERRED.toString())
    }

    @Test
    fun decode_withNullValue() {
        val decodedValue = Optionality.decode(null)
        assertNull(decodedValue)
    }
}