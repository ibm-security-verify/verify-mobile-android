package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class AgentReferenceTest {

    @Test
    fun serialize_to_Json() {
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
    fun serialize_withRequiredFields() {
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
    fun deserialize_from_Json() {
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
    fun deserialize_withRequiredFields() {
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
    fun deserialize_withNullValues() {
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