package com.ibm.security.verifysdk.dc.model

import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
class CreateVerificationRequestArgsTest {

    @Test
    fun serialization() {
        val requestArgs = CreateVerificationRequestArgs(
            to = ToConnection("connectionId"),
            comment = "This.is.a.comment",
            state = VerificationState.PROOF_SHARED,
            proofSchemaId = "schema123",
            proofRequest = ProofRequestArgs("requestId"),
            properties = mapOf("key1" to "value1", "key2" to "value2"),
            directRoute = true,
            ariesVersion = "v1.0",
            allowProofRequestOverride = false
        )

        val jsonString = json.encodeToString(requestArgs)

        val expectedJson = """{
            "to":{"name":"connectionId"},
            "comment":"This.is.a.comment",
            "state":"proof_shared",
            "proof_schema_id":"schema123",
            "proof_request":{"name":"requestId"},
            "properties":{"key1":"value1","key2":"value2"},
            "direct_route":true,
            "aries_version":"v1.0",
            "allow_proof_request_override":false
        }""".trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialization() {
        val jsonString = """{
            "to":{"name":"connectionId"},
            "comment":"This.is.a.comment",
            "state":"proof_shared",
            "proof_schema_id":"schema123",
            "proof_request":{"name":"requestId"},
            "properties":{"key1":"value1","key2":"value2"},
            "direct_route":true,
            "aries_version":"v1.0",
            "allow_proof_request_override":false
        }""".trimIndent().replace("\n", "").replace(" ", "")

        val requestArgs = json.decodeFromString<CreateVerificationRequestArgs>(jsonString)

        assertNotNull(requestArgs)
        assertEquals("connectionId", requestArgs.to?.name)
        assertEquals("This.is.a.comment", requestArgs.comment)
        assertEquals(VerificationState.PROOF_SHARED, requestArgs.state)
        assertEquals("schema123", requestArgs.proofSchemaId)
        assertEquals("requestId", requestArgs.proofRequest?.name)
        assertEquals(mapOf("key1" to "value1", "key2" to "value2"), requestArgs.properties)
        assertEquals(true, requestArgs.directRoute)
        assertEquals("v1.0", requestArgs.ariesVersion)
        assertEquals(false, requestArgs.allowProofRequestOverride)
    }

    @Test
    fun serialization_withNullValue() {
        val requestArgs = CreateVerificationRequestArgs()

        val jsonString = json.encodeToString(requestArgs)

        val expectedJson = "{}"
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialization_withMissingFields() {
        val jsonString = """{
            "comment":"This is a partial comment",
            "direct_route":true
        }"""

        val requestArgs = json.decodeFromString<CreateVerificationRequestArgs>(jsonString)

        assertNotNull(requestArgs)
        assertEquals("This is a partial comment", requestArgs.comment)
        assertEquals(true, requestArgs.directRoute)
        assertNull(requestArgs.to)
        assertNull(requestArgs.state)
    }

    @Test(expected = SerializationException::class)
    fun deserialization_withInvalidData() {
        val invalidJson = """{
            "to":"invalidConnectionObject"
        }"""

        json.decodeFromString<CreateVerificationRequestArgs>(invalidJson)
    }
}