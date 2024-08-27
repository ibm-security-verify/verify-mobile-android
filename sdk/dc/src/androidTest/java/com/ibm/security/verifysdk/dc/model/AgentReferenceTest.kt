package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class AgentReferenceTest {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = false
        isLenient = true
    }

    @Test
    fun serialize_to_JSON() {
        val agentReference = AgentReference(
            id = "id",
            name = "name",
            pass = "pass"
        )

        val jsonString = json.encodeToString(agentReference)
        val expectedJson = """
            {
                "id": "id",
                "name": "name",
                "pass": "pass"
            }
        """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }


    @Test
    fun serialize_to_JSON_with_required_fields() {
        val agentReference = AgentReference(
            id = "id",
            name = "name"
        )

        val jsonString = json.encodeToString(agentReference)
        val expectedJson = """
            {
                "id": "id",
                "name": "name"
            }
        """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_JSON() {
        val jsonString = """
            {
                "id": "id",
                "name": "name",
                "pass": "pass"
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReference = json.decodeFromString<AgentReference>(jsonString)
        assertEquals("id", agentReference.id)
        assertEquals("name", agentReference.name)
        assertEquals("pass", agentReference.pass)
    }


    @Test
    fun deserialize_with_required_fields() {
        val jsonString = """
            {
                "id": "id",
                "name": "name"
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReference = json.decodeFromString<AgentReference>(jsonString)
        assertEquals("id", agentReference.id)
        assertEquals("name", agentReference.name)
        assertNull(agentReference.pass)
    }

    @Test
    fun deserialize_with_null_values() {
        val jsonString = """
            {
                "id": "id",
                "name": "name",
                "pass": null
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReference = json.decodeFromString<AgentReference>(jsonString)
        assertEquals("id", agentReference.id)
        assertEquals("name", agentReference.name)
        assertNull(agentReference.pass)
    }
}