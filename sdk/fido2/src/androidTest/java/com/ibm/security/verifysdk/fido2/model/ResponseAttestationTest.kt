package com.ibm.security.verifysdk.fido2.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert

@RunWith(AndroidJUnit4::class)
class ResponseAttestationTest {

    @Test
    fun testSerializationWithAllFields() {
        val response = ResponseAttestation(
            clientDataJSON = """{"type":"webauthn.create","challenge":"randomChallenge"}""",
            attestationObject = """{"fmt":"none","authData":"authDataValue"}"""
        )

        val jsonString = json.encodeToString(ResponseAttestation.serializer(), response)
        val expectedJson =
            """{"clientDataJSON":"{\"type\":\"webauthn.create\",\"challenge\":\"randomChallenge\"}","attestationObject":"{\"fmt\":\"none\",\"authData\":\"authDataValue\"}"}"""
        JSONAssert.assertEquals(expectedJson, jsonString, false)
    }

    @Test
    fun testDeserializationWithAllFields() {
        val jsonString =
            """{"clientDataJSON":"{\"type\":\"webauthn.create\",\"challenge\":\"randomChallenge\"}","attestationObject":"{\"fmt\":\"none\",\"authData\":\"authDataValue\"}"}"""
        val response = json.decodeFromString<ResponseAttestation>(jsonString)

        assertEquals(
            """{"type":"webauthn.create","challenge":"randomChallenge"}""",
            response.clientDataJSON
        )
        assertEquals("""{"fmt":"none","authData":"authDataValue"}""", response.attestationObject)
    }

    @Test
    fun testSerializationWithEmptyFields() {
        val response = ResponseAttestation(
            clientDataJSON = "",
            attestationObject = ""
        )

        val jsonString = json.encodeToString(ResponseAttestation.serializer(), response)
        val expectedJson = """{"clientDataJSON":"","attestationObject":""}"""
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun testDeserializationWithEmptyFields() {
        val jsonString = """{"clientDataJSON":"","attestationObject":""}"""
        val response = json.decodeFromString<ResponseAttestation>(jsonString)

        assertEquals("", response.clientDataJSON)
        assertEquals("", response.attestationObject)
    }

    @Test
    fun testDeserializationWithNumericValue() {
        val jsonString =
            """{"clientDataJSON":123,"attestationObject":true}""" // Invalid types for fields
        val response = json.decodeFromString<ResponseAttestation>(jsonString)
        assertEquals("123", response.clientDataJSON)
    }
}