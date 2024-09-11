package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class ProofRequestTest {

    private val presentationDefinition1 = PresentationDefinition(
        id = "presentationDefinitionId1", inputDescriptors = listOf(
            InputDescriptor(
                id = "descriptorId1",
                schema = listOf(Schema(uri = "http://localhost/schema1.json", required = true))
            )
        )
    )

    private val presentationDefinition2 = PresentationDefinition(
        id = "presentationDefinitionId2", inputDescriptors = listOf(
            InputDescriptor(
                id = "descriptorId2",
                schema = listOf(Schema(uri = "http://localhost/schema1.json", required = false))
            )
        )
    )

    private val presentationDefinitionEmptyFields =
        PresentationDefinition(id = "", inputDescriptors = listOf())

    @Test
    fun initialize() {
        val proofRequest = ProofRequest(
            nonce = "12345",
            name = "Test Request",
            version = "1.0",
            requestedAttributes = "attribute1",
            requestedPredicates = "predicate1",
            allowProofRequestOverride = true,
            credFilter = listOf(CredFilter("type1", listOf("value1"))),
            properties = mapOf("key1" to "value1"),
            jsonld = DifPresentationRequest(presentationDefinition = presentationDefinition1),
            bbs = DifPresentationRequest(presentationDefinition = presentationDefinition2)
        )
        val jsonString = json.encodeToString(proofRequest)
        val deserializedProofRequest = json.decodeFromString<ProofRequest>(jsonString)

        assertEquals(proofRequest, deserializedProofRequest)
    }

    @Test
    fun initialize_withNullValues() {
        val proofRequest = ProofRequest(
            nonce = "12345",
            name = null,
            version = null,
            requestedAttributes = null,
            requestedPredicates = null,
            allowProofRequestOverride = null,
            credFilter = null,
            properties = null,
            jsonld = null,
            bbs = null
        )
        val jsonString = json.encodeToString(proofRequest)
        val deserializedProofRequest = json.decodeFromString<ProofRequest>(jsonString)

        assertEquals(proofRequest, deserializedProofRequest)
    }

    @Test
    fun initialize_withEmptyValues() {

        val proofRequest = ProofRequest(
            nonce = "12345",
            name = "",
            version = "",
            requestedAttributes = "",
            requestedPredicates = "",
            allowProofRequestOverride = false,
            credFilter = emptyList(),
            properties = emptyMap(),
            jsonld = DifPresentationRequest(presentationDefinitionEmptyFields),
            bbs = DifPresentationRequest(presentationDefinitionEmptyFields)
        )
        val jsonString = json.encodeToString(proofRequest)
        val deserializedProofRequest = json.decodeFromString<ProofRequest>(jsonString)

        assertEquals(proofRequest, deserializedProofRequest)
    }

    @Test
    fun initialize_withInvalidData() {
        val invalidJson = """{
            "nonce": "12345",
            "name": "Test Request",
            "version": "1.0",
            "requested_attributes": 123,
            "requested_predicates": "predicate1",
            "allow_proof_request_override": true,
            "cred_filter": "invalidType",
            "properties": "invalidType",
            "jsonld": {},
            "bbs": {}
        }""".trimIndent().replace("\n", "").replace(" ", "")

        try {
            json.decodeFromString<ProofRequest>(invalidJson)
            fail("Expected a SerializationException to be thrown due to invalid data")
        } catch (e: SerializationException) {
            // Test passes if a SerializationException is thrown
        } catch (e: Exception) {
            // Fail the test if an unexpected type of Exception is thrown
            fail("Expected a SerializationException but got ${e::class.simpleName}")
        }
    }
}