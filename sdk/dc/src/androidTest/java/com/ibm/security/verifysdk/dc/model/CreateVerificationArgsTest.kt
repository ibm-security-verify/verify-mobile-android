package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class CreateVerificationArgsTest{

    @Test
    fun initialize() {
        val toConnection = ToConnection(id = "123", name = "Connection Name")
        val proofRequestArgs = ProofRequestArgs(name = "requestId")
        val properties = mapOf("key1" to "value1", "key2" to "value2")
        val nonRevocationTimes = mapOf("start" to "1620000000", "end" to "1629999999")

        val verificationArgs = CreateVerificationArgs(
            to = toConnection,
            comment = "Test Comment",
            state = VerificationState.INBOUND_PROOF_REQUEST,
            proofSchemaId = "schema123",
            proofRequest = proofRequestArgs,
            properties = properties,
            directRoute = true,
            ariesVersion = "1.0",
            allowProofRequestOverride = true,
            nonRevocationTimes = nonRevocationTimes
        )

        assertEquals("Test Comment", verificationArgs.comment)
        assertEquals("schema123", verificationArgs.proofSchemaId)
        assertEquals(properties, verificationArgs.properties)
        assertTrue(verificationArgs.directRoute!!)
        assertEquals("1.0", verificationArgs.ariesVersion)
        assertTrue(verificationArgs.allowProofRequestOverride!!)
        assertEquals(nonRevocationTimes, verificationArgs.nonRevocationTimes)
        assertEquals(VerificationState.INBOUND_PROOF_REQUEST, verificationArgs.state)
    }

    @Test
    fun initialize_withNull() {
        val verificationArgs = CreateVerificationArgs()

        assertNull(verificationArgs.to)
        assertNull(verificationArgs.comment)
        assertNull(verificationArgs.state)
        assertNull(verificationArgs.proofSchemaId)
        assertNull(verificationArgs.proofRequest)
        assertNull(verificationArgs.properties)
        assertNull(verificationArgs.directRoute)
        assertNull(verificationArgs.ariesVersion)
        assertNull(verificationArgs.allowProofRequestOverride)
        assertNull(verificationArgs.nonRevocationTimes)
    }

    @Test
    fun serialize_to_Json() {        val toConnection = ToConnection(id = "123", name = "Connection_Name")
        val proofRequestArgs = ProofRequestArgs(name = "requestId")
        val properties = mapOf("key1" to "value1", "key2" to "value2")
        val nonRevocationTimes = mapOf("start" to "1620000000", "end" to "1629999999")

        val verificationArgs = CreateVerificationArgs(
            to = toConnection,
            comment = "Test_Comment",
            state = VerificationState.INBOUND_PROOF_REQUEST,
            proofSchemaId = "schema123",
            proofRequest = proofRequestArgs,
            properties = properties,
            directRoute = true,
            ariesVersion = "1.0",
            allowProofRequestOverride = true,
            nonRevocationTimes = nonRevocationTimes
        )

        val jsonString = json.encodeToString(verificationArgs)
        val expectedJson = """{
            "to":{"name":"Connection_Name","id":"123"},
            "comment":"Test_Comment",
            "state":"inbound_proof_request",
            "proof_schema_id":"schema123",
            "proof_request":{"name":"requestId"},
            "properties":{"key1":"value1","key2":"value2"},
            "direct_route":true,
            "aries_version":"1.0",
            "allow_proof_request_override":true,
            "non_revocation_times":{"start":"1620000000","end":"1629999999"}
        }""".trimIndent().replace("\n", "").replace(" ", "")

        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_Json() {
        val jsonString = """{
            "to":{"id":"123","name":"Connection_Name"},
            "comment":"Test_Comment",
            "state":"inbound_proof_request",
            "proof_schema_id":"schema123",
            "proof_request":{"name":"requestId"},
            "properties":{"key1":"value1","key2":"value2"},
            "direct_route":true,
            "aries_version":"1.0",
            "allow_proof_request_override":true,
            "non_revocation_times":{"start":"1620000000","end":"1629999999"}
        }""".replace("\n", "").replace(" +", "")

        val verificationArgs = json.decodeFromString<CreateVerificationArgs>(jsonString)

        assertEquals("Test_Comment", verificationArgs.comment)
        assertEquals("inbound_proof_request", verificationArgs.state!!.value)
        assertEquals("schema123", verificationArgs.proofSchemaId)
        assertEquals("requestId", verificationArgs.proofRequest!!.name)
        assertTrue(verificationArgs.directRoute!!)
        assertEquals("1.0", verificationArgs.ariesVersion)
        assertTrue(verificationArgs.allowProofRequestOverride!!)
        assertEquals(mapOf("start" to "1620000000", "end" to "1629999999"), verificationArgs.nonRevocationTimes)
    }

    @Test
    fun equal() {
        val toConnection = ToConnection(id = "123", name = "Connection Name")
        val verificationArgs1 = CreateVerificationArgs(
            to = toConnection,
            comment = "Test Comment"
        )
        val verificationArgs2 = CreateVerificationArgs(
            to = toConnection,
            comment = "Test Comment"
        )

        assertEquals(verificationArgs1, verificationArgs2)
    }

    @Test
    fun notEqual() {
        val toConnection1 = ToConnection(id = "123", name = "Connection Name")
        val toConnection2 = ToConnection(id = "456", name = "Different Connection Name")

        val verificationArgs1 = CreateVerificationArgs(
            to = toConnection1,
            comment = "Test Comment"
        )
        val verificationArgs2 = CreateVerificationArgs(
            to = toConnection2,
            comment = "Test Comment"
        )

        assertNotEquals(verificationArgs1, verificationArgs2)
    }

    @Test
    fun hashcode() {
        val toConnection = ToConnection(id = "123", name = "Connection Name")
        val verificationArgs1 = CreateVerificationArgs(
            to = toConnection,
            comment = "Test Comment"
        )
        val verificationArgs2 = CreateVerificationArgs(
            to = toConnection,
            comment = "Test Comment"
        )

        assertEquals(verificationArgs1.hashCode(), verificationArgs2.hashCode())
    }

}