package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RulesTest {
    
    @Test
    fun serialization() {
        val rule = Rules.ALL
        val jsonString = json.encodeToString(rule)
        assertEquals("\"all\"", jsonString) 
    }

    @Test
    fun deserialization() {
        val jsonString = "\"all\""
        val deserializedRule = json.decodeFromString<Rules>(jsonString)
        assertEquals(Rules.ALL, deserializedRule)
    }

    @Test
    fun encode() {
        val encodedValue = Rules.encode(Rules.PICK)
        assertEquals("pick", encodedValue)
    }

    @Test
    fun decode() {
        val decodedValue = Rules.decode("all")
        assertEquals(Rules.ALL, decodedValue)
    }

    @Test
    fun decode_withInvalidData() {
        val decodedValue = Rules.decode("invalid_rule")
        assertNull(decodedValue) 
    }

    @Test
    fun toString_method() {
        assertEquals("all", Rules.ALL.toString())
        assertEquals("pick", Rules.PICK.toString())
    }

    @Test
    fun decode_withNullValue() {
        val decodedValue = Rules.decode(null)
        assertNull(decodedValue) 
    }
}