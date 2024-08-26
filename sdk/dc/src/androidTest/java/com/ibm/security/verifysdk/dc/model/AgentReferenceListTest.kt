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
class AgentReferenceListTest {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Test
    fun serialize_to_JSON() {
        val agentReference = AgentReference(
            id = "id",
            name = "name",
            pass = "pass"
        )

        val agentReferenceList = AgentReferenceList(
            count = 2,
            items = listOf(agentReference, agentReference)
        )

        val jsonString = json.encodeToString(agentReferenceList)
        val expectedJson = """
            {
               "count":2,
               "items":[
                  {
                     "id":"id",
                     "name":"name",
                     "pass":"pass"
                  },
                  {
                     "id":"id",
                     "name":"name",
                     "pass":"pass"
                  }
               ]
            }
        """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_JSON() {
        val jsonString = """
            {
               "count":2,
               "items":[
                  {
                     "id":"id",
                     "name":"name",
                     "pass":"pass"
                  },
                  {
                     "id":"id",
                     "name":"name",
                     "pass":"pass"
                  }
               ]
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReferenceList = json.decodeFromString<AgentReferenceList>(jsonString)
        assertEquals(2, agentReferenceList.count)
        assertEquals("name", agentReferenceList.items[0].name)
        assertEquals("pass", agentReferenceList.items[0].pass)
    }


    @Test
    fun deserialize_with_missing_optional_fields() {
        val jsonString = """
            {
               "count":2,
               "items":[
                  {
                     "id":"id",
                     "name":"name"
                  },
                  {
                     "id":"id",
                     "name":"name"
                  }
               ]
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReferenceList = json.decodeFromString<AgentReferenceList>(jsonString)
        assertEquals(2, agentReferenceList.count)
        assertEquals("name", agentReferenceList.items[0].name)
        assertNull(agentReferenceList.items[0].pass)
    }

    @Test
    fun deserialize_with_null_values() {
        val jsonString = """
            {
               "count":2,
               "items":[
                  {
                     "id":"id",
                     "name":"name",
                     "pass": null
                  },
                  {
                     "id":"id",
                     "name":"name",
                     "pass": null
                  }
               ]
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReferenceList = json.decodeFromString<AgentReferenceList>(jsonString)
        assertEquals(2, agentReferenceList.count)
        assertEquals("name", agentReferenceList.items[0].name)
        assertNull(agentReferenceList.items[0].pass)
        assertNull(agentReferenceList.items[1].pass)
    }
}