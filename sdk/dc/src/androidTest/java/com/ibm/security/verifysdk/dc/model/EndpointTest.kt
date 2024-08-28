package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class EndpointTest {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = false
        isLenient = true
    }

    @Test
    fun serialize_to_JSON() {
        val endpoint = Endpoint(
            name = "exampleEndpoint",
            url = "https://example.com",
            pairwise = json.decodeFromString<DidInfo>(didInfo),
            ext = true,
            `public` = json.decodeFromString<DidInfo>(didInfo2),
            properties = mapOf("key" to JsonPrimitive("value")),
            did = "did:example:abcd",
            didDoc = JsonObject(mapOf("doc" to JsonPrimitive("document")))
        )

        val jsonString = json.encodeToString(endpoint)
        val expectedJson = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "pairwise": {"did":"did:example:1234","verkey":"12345"},
                "ext": true,
                "public": {"did":"did:example:5678","verkey":"12345"},
                "properties": {"key": "value"},
                "did": "did:example:abcd",
                "did_doc": {"doc": "document"}
            }
        """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_JSON() {
        val jsonString = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true,
                "public": {"did": "did:example:5678", "verkey": "12345"},
                "properties": {"key": "value"},
                "did": "did:example:abcd",
                "did_doc": {"doc": "document"}
            }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val endpoint = json.decodeFromString<Endpoint>(jsonString)

        assertEquals("exampleEndpoint", endpoint.name)
        assertEquals("https://example.com", endpoint.url)
        assertEquals("did:example:1234", endpoint.pairwise.did)
        assertEquals(true, endpoint.ext)
        assertEquals("did:example:5678", endpoint.public?.did)
        assertEquals(mapOf("key" to JsonPrimitive("value")), endpoint.properties)
        assertEquals("did:example:abcd", endpoint.did)
        assertEquals(JsonObject(mapOf("doc" to JsonPrimitive("document"))), endpoint.didDoc)
    }

    @Test
    fun deserialize_with_required_fields() {
        val jsonString = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true
            }
             """.trimIndent().replace("\n", "").replace(" ", "")

        val endpoint = json.decodeFromString<Endpoint>(jsonString)

        assertEquals("exampleEndpoint", endpoint.name)
        assertEquals("https://example.com", endpoint.url)
        assertEquals("did:example:1234", endpoint.pairwise.did)
        assertEquals(true, endpoint.ext)
        assertNull(endpoint.public)
        assertNull(endpoint.properties)
        assertNull(endpoint.did)
        assertNull(endpoint.didDoc)
        assertNull(endpoint.device)
    }

    @Test
    fun deserialize_with_null_values() {
        val jsonString = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true,
                "public": null,
                "properties": null,
                "did": null,
                "did_doc": null,
                "device": null
            }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val endpoint = json.decodeFromString<Endpoint>(jsonString)

        assertEquals("exampleEndpoint", endpoint.name)
        assertEquals("https://example.com", endpoint.url)
        assertEquals("did:example:1234", endpoint.pairwise.did)
        assertEquals(true, endpoint.ext)
        assertNull(endpoint.public)
        assertNull(endpoint.properties)
        assertNull(endpoint.did)
        assertNull(endpoint.didDoc)
        assertNull(endpoint.device)
    }

    private val didInfo = """
        {
           "did":"did:example:1234",
           "verkey":"12345"
        }
        """.trimIndent().replace("\n", "").replace(" ", "")

    private val didInfo2 = """
        {
           "did":"did:example:5678",
           "verkey":"12345"
        }
        """.trimIndent().replace("\n", "").replace(" ", "")
}
