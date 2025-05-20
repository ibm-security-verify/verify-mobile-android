package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert

@RunWith(AndroidJUnit4::class)
class PdStatusTest {

    @Test
    fun serialization_withDefaultInstance() {
        // covers DefaultConstructorMarker test
        val expectedJson = """{}"""
        val pdStatus = PdStatus()
        val jsonString = json.encodeToString(PdStatus.serializer(), pdStatus)
        JSONAssert.assertEquals(expectedJson, jsonString, false)
    }

    @Test
    fun serialization_withDirectiveRequired() {
        val pdStatus = PdStatus(Directives.REQUIRED)
        val jsonString = json.encodeToString(PdStatus.serializer(), pdStatus)
        val expectedJson = """{"directive":"required"}"""
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun serialization_withDirectiveAllowed() {
        val pdStatus = PdStatus(Directives.ALLOWED)
        val jsonString = json.encodeToString(PdStatus.serializer(), pdStatus)
        val expectedJson = """{"directive":"allowed"}"""
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun serialization_withDirectiveDisallowed() {
        val pdStatus = PdStatus(Directives.DISALLOWED)
        val jsonString = json.encodeToString(PdStatus.serializer(), pdStatus)
        val expectedJson = """{"directive":"disallowed"}"""
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun serialization_withNullValue() {
        val pdStatus = PdStatus(null)
        val jsonString = json.encodeToString(pdStatus)
        val expectedJson = """{}"""
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun serialization_withKSerializerAndNullValue() {
        val pdStatus = PdStatus(null)
        val jsonString = json.encodeToString(PdStatus.serializer(), pdStatus)
        val expectedJson = """{}"""
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialization() {
        val jsonString = """{"directive":"required"}"""
        val pdStatus = json.decodeFromString<PdStatus>(jsonString)
        assertNotNull(pdStatus)
        assertEquals(Directives.REQUIRED, pdStatus.directive)
    }

    @Test
    fun deserialization_withNullValue() {
        val jsonString = """{"directive":null}"""
        val pdStatus = json.decodeFromString(PdStatus.serializer(), jsonString)
        assertNotNull(pdStatus)
        assertNull(pdStatus.directive)
    }

    @Test
    fun deserialization_withUnknownKeys() {
        val jsonString = """{"directive":"allowed","unknownKey":"unknownValue"}"""
        val pdStatus = json.decodeFromString<PdStatus>(jsonString)
        assertNotNull(pdStatus)
        assertEquals(Directives.ALLOWED, pdStatus.directive)
    }

    @Test(expected = SerializationException::class)
    fun deserialization_withInvalidValue() {
        val jsonString = """{"directive":"invalidDirective"}"""
        json.decodeFromString<PdStatus>(jsonString)
    }
}