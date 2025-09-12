package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.dc.model.ConnectionAgentInfo
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class ConnectionAgentInfoTest {

    @Test
    fun serialize_to_Json() {
        val connectionAgentInfo = ConnectionAgentInfo(
            name = "exampleEndpoint",
            url = "https://example.com",
            ext = true,
            properties = mapOf("key" to JsonPrimitive("value")),
            did = "did:example:abcd",
            didDoc = JsonObject(mapOf("doc" to JsonPrimitive("document")))
        )

        val jsonString = json.encodeToString(connectionAgentInfo)
        val expectedJson = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "ext": true,
                "properties": {"key": "value"},
                "did": "did:example:abcd",
                "did_doc": {"doc": "document"}
            }
        """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_Json() {
        val jsonString = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "ext": true,
                "properties": {"key": "value"},
                "did": "did:example:abcd",
                "did_doc": {"doc": "document"}
            }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val connectionAgentInfo = json.decodeFromString<ConnectionAgentInfo>(jsonString)

        assertEquals("exampleEndpoint", connectionAgentInfo.name)
        assertEquals("https://example.com", connectionAgentInfo.url)
        assertEquals(true, connectionAgentInfo.ext)
        assertEquals(mapOf("key" to JsonPrimitive("value")), connectionAgentInfo.properties)
        assertEquals("did:example:abcd", connectionAgentInfo.did)
        assertEquals(JsonObject(mapOf("doc" to JsonPrimitive("document"))), connectionAgentInfo.didDoc)
    }

    @Test
    fun deserialize_withRequiredAttributes() {
        val jsonString = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "ext": true
            }
             """.trimIndent().replace("\n", "").replace(" ", "")

        val connectionAgentInfo = json.decodeFromString<ConnectionAgentInfo>(jsonString)

        assertEquals("exampleEndpoint", connectionAgentInfo.name)
        assertEquals("https://example.com", connectionAgentInfo.url)
        assertEquals(true, connectionAgentInfo.ext)
        assertNull(connectionAgentInfo.properties)
        assertNull(connectionAgentInfo.did)
        assertNull(connectionAgentInfo.didDoc)
    }

    @Test
    fun deserialize_withNullValues() {
        val jsonString = """
            {
                "name": "exampleEndpoint",
                "url": "https://example.com",
                "ext": true,
                "public": null,
                "properties": null,
                "did": null,
                "did_doc": null
            }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val connectionAgentInfo = json.decodeFromString<ConnectionAgentInfo>(jsonString)

        assertEquals("exampleEndpoint", connectionAgentInfo.name)
        assertEquals("https://example.com", connectionAgentInfo.url)
        assertEquals(true, connectionAgentInfo.ext)
        assertNull(connectionAgentInfo.properties)
        assertNull(connectionAgentInfo.did)
        assertNull(connectionAgentInfo.didDoc)
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
