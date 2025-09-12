package com.ibm.security.verifysdk.dc.model

import com.ibm.security.verifysdk.dc.model.CredFilter
import com.ibm.security.verifysdk.dc.model.InputDescriptor
import com.ibm.security.verifysdk.dc.model.PresentationDefinition
import com.ibm.security.verifysdk.dc.model.PresentationRequest
import com.ibm.security.verifysdk.dc.model.ProofRequest
import com.ibm.security.verifysdk.dc.model.Schema
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.skyscreamer.jsonassert.JSONAssert

@OptIn(ExperimentalSerializationApi::class)
@RunWith(Parameterized::class)
class ProofRequestTest(private val serializer: KSerializer<ProofRequest>?) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<KSerializer<ProofRequest>?>> {
            return listOf(
                arrayOf(ProofRequest.serializer()),
                arrayOf(null)
            )
        }
    }

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
                schema = listOf(Schema(uri = "http://localhost/schema2.json", required = false))
            )
        )
    )

    private val presentationDefinitionEmptyFields =
        PresentationDefinition(id = "", inputDescriptors = listOf())

    @Test
    fun deserialize_withUnknownFields() {
        val jsonString = """{"nonce":"12345","name":"TestName","extra_field":"extra_value"}"""
        var proofRequest: ProofRequest
        serializer?.let {
            proofRequest = json.decodeFromString(serializer as DeserializationStrategy<ProofRequest>, jsonString)
        }.run {
            proofRequest = json.decodeFromString<ProofRequest>(jsonString)
        }
        assertEquals("12345", proofRequest.nonce)
        assertEquals("TestName", proofRequest.name)
        // Extra fields are ignored, no exceptions should be thrown.
    }

    @Test
    fun serialize_withNullValue() {
        // covers DefaultConstructorMarker test
        val expectedJson = """{"nonce":"12345"}"""
        val proofRequest = ProofRequest(nonce = "12345", name = null)
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }
        JSONAssert.assertEquals(expectedJson, jsonString, false)
    }

    @Test
    fun serialize_withRequiredValue() {
        // covers DefaultConstructorMarker test
        val expectedJson = """{"nonce":"12345"}"""
        val proofRequest = ProofRequest(nonce = "12345")
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }
        JSONAssert.assertEquals(expectedJson, jsonString, false)
    }

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
            jsonld = PresentationRequest(presentationDefinition = presentationDefinition1),
            bbs = PresentationRequest(presentationDefinition = presentationDefinition2)
        )
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }

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
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }
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
            jsonld = PresentationRequest(presentationDefinitionEmptyFields),
            bbs = PresentationRequest(presentationDefinitionEmptyFields)
        )
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }
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
            serializer?.let {
                json.decodeFromString(serializer as DeserializationStrategy<ProofRequest>, invalidJson)
            }.run {
                json.decodeFromString<ProofRequest>(invalidJson)
            }
            fail("Expected a SerializationException to be thrown due to invalid data")
        } catch (e: SerializationException) {
            // Test passes if a SerializationException is thrown
        } catch (e: Exception) {
            // Fail the test if an unexpected type of Exception is thrown
            fail("Expected a SerializationException but got ${e::class.simpleName}")
        }
    }

    @Test
    fun serialize_withAllFields() {
        val credFilter = listOf(CredFilter("filterType", listOf("value")))
        val properties = mapOf("key1" to "value1", "key2" to "value2")
        val jsonldRequest = PresentationRequest(
            PresentationDefinition(
                "jsonld", listOf(
                    InputDescriptor("id", listOf(Schema(uri = "url", required = true)))
                )
            )
        )
        val bbsRequest = PresentationRequest(
            PresentationDefinition(
                "bbs", listOf(
                    InputDescriptor("id", listOf(Schema(uri = "url", required = false)))
                )
            )
        )

        val proofRequest = ProofRequest(
            nonce = "123456",
            name = "Test Name",
            version = "1.0",
            requestedAttributes = "requestedAttributes",
            requestedPredicates = "requestedPredicates",
            allowProofRequestOverride = true,
            credFilter = credFilter,
            properties = properties,
            jsonld = jsonldRequest,
            bbs = bbsRequest
        )
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }
        assertTrue(jsonString.contains("\"nonce\":\"123456\""))
        assertTrue(jsonString.contains("\"name\":\"Test Name\""))
        assertTrue(jsonString.contains("\"version\":\"1.0\""))
        assertTrue(jsonString.contains("\"requested_attributes\":\"requestedAttributes\""))
        assertTrue(jsonString.contains("\"requested_predicates\":\"requestedPredicates\""))
        assertTrue(jsonString.contains("\"allow_proof_request_override\":true"))
        assertTrue(jsonString.contains("\"cred_filter\":[{\"attr_name\":\"filterType\",\"attr_values\":[\"value\"]}]"))
        assertTrue(jsonString.contains("\"properties\":{\"key1\":\"value1\",\"key2\":\"value2\"}"))
        assertTrue(jsonString.contains("\"jsonld\":{\"presentation_definition\":{\"id\":\"jsonld\",\"input_descriptors\":[{\"id\":\"id\",\"schema\":[{\"uri\":\"url\",\"required\":true}]}]}}"))
        assertTrue(jsonString.contains("\"bbs\":{\"presentation_definition\":{\"id\":\"bbs\",\"input_descriptors\":[{\"id\":\"id\",\"schema\":[{\"uri\":\"url\",\"required\":false}]}]}}"))
    }

    @Test
    fun deserialize_withAllFields() {
        val jsonString = """
            {
                "nonce": "123456",
                "name": "Test Name",
                "version": "1.0",
                "requested_attributes": "requestedAttributes",
                "requested_predicates": "requestedPredicates",
                "allow_proof_request_override": true,
                "cred_filter": [{"attr_name": "attr_name", "attr_values": ["value"]}],
                "properties": {"key1": "value1", "key2": "value2"},
                "jsonld": { "presentation_definition": {"id": "jsonldId", "input_descriptors": [{"id":"id","schema":[{"uri":"url","required":true}]}]}},
                "bbs": { "presentation_definition": {"id": "bbsId", "input_descriptors": [{"id":"id","schema":[{"uri":"url","required":false}]}]}}}
        """.trimIndent()
        lateinit var proofRequest: ProofRequest
        serializer?.let {
            proofRequest = json.decodeFromString(serializer as DeserializationStrategy<ProofRequest>, jsonString)
        }.run {
            proofRequest = json.decodeFromString<ProofRequest>(jsonString)
        }
        assertEquals("123456", proofRequest.nonce)
        assertEquals("Test Name", proofRequest.name)
        assertEquals("1.0", proofRequest.version)
        assertEquals("requestedAttributes", proofRequest.requestedAttributes)
        assertEquals("requestedPredicates", proofRequest.requestedPredicates)
        assertTrue(proofRequest.allowProofRequestOverride!!)
        assertEquals("attr_name", proofRequest.credFilter!![0].attrName)
        assertEquals("value1", proofRequest.properties!!["key1"])
        assertEquals("jsonldId", proofRequest.jsonld!!.presentationDefinition.id)
        assertEquals("bbsId", proofRequest.bbs!!.presentationDefinition.id)
    }

    @Test
    fun deserialize_withMissingOptionalFields() {
        val jsonString = """
            { 
              "nonce": "123456"
            }
        """.trimIndent()
        lateinit var proofRequest: ProofRequest
        serializer?.let {
            proofRequest = json.decodeFromString(serializer as DeserializationStrategy<ProofRequest>, jsonString)
        }.run {
            proofRequest = json.decodeFromString<ProofRequest>(jsonString)
        }
        assertEquals("123456", proofRequest.nonce)
        assertNull(proofRequest.name)
        assertNull(proofRequest.version)
        assertNull(proofRequest.requestedAttributes)
        assertNull(proofRequest.requestedPredicates)
        assertNull(proofRequest.allowProofRequestOverride)
        assertNull(proofRequest.credFilter)
        assertNull(proofRequest.properties)
        assertNull(proofRequest.jsonld)
        assertNull(proofRequest.bbs)
    }

    @Test
    fun deserialize_withNullValues() {
        val jsonString = """
            {
                "nonce": "123456",
                "name": null,
                "version": null,
                "requested_attributes": null,
                "requested_predicates": null,
                "allow_proof_request_override": null,
                "cred_filter": null,
                "properties": null,
                "jsonld": null,
                "bbs": null
            }
        """.trimIndent()
        lateinit var proofRequest: ProofRequest
        serializer?.let {
            proofRequest = json.decodeFromString(serializer as DeserializationStrategy<ProofRequest>, jsonString)
        }.run {
            proofRequest = json.decodeFromString<ProofRequest>(jsonString)
        }
        assertEquals("123456", proofRequest.nonce)
        assertNull(proofRequest.name)
        assertNull(proofRequest.version)
        assertNull(proofRequest.requestedAttributes)
        assertNull(proofRequest.requestedPredicates)
        assertNull(proofRequest.allowProofRequestOverride)
        assertNull(proofRequest.credFilter)
        assertNull(proofRequest.properties)
        assertNull(proofRequest.jsonld)
        assertNull(proofRequest.bbs)
    }

    @Test
    fun initialize_withEmptyListsAndMaps() {
        val proofRequest = ProofRequest(
            nonce = "123456",
            name = "Test Name",
            version = "1.0",
            requestedAttributes = "requestedAttributes",
            requestedPredicates = "requestedPredicates",
            allowProofRequestOverride = false,
            credFilter = emptyList(),
            properties = emptyMap(),
            jsonld = null,
            bbs = null
        )
        lateinit var jsonString: String
        serializer?.let {
            jsonString = json.encodeToString(serializer, proofRequest)
        }.run {
            jsonString = json.encodeToString(proofRequest)
        }
        assertEquals("Test Name", proofRequest.name)
        assertTrue(jsonString.contains("\"cred_filter\":[]"))
        assertTrue(jsonString.contains("\"properties\":{}"))
    }

    @Test
    fun deserialize_withEmptyListsAndMaps() {
        val jsonString = """
            {
                "nonce": "123456",
                "cred_filter": [],
                "properties": {}
            }
        """.trimIndent()
        lateinit var proofRequest: ProofRequest
        serializer?.let {
            proofRequest = json.decodeFromString(serializer as DeserializationStrategy<ProofRequest>, jsonString)
        }.run {
            proofRequest = json.decodeFromString<ProofRequest>(jsonString)
        }
        assertEquals("123456", proofRequest.nonce)
        assertTrue(proofRequest.credFilter!!.isEmpty())
        assertTrue(proofRequest.properties!!.isEmpty())
    }
}