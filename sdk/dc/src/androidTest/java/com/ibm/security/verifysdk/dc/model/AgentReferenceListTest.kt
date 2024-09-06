package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AgentReferenceListTest {

    @Test
    fun serialize_to_Json() {
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
    fun deserialize_from_Json() {
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
    fun deserialize_withRequiredFields() {
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
    fun deserialize_withNullValues() {
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

    @Test
    fun serialize_withEmptyList() {
        val agentReferenceList = AgentReferenceList(
            count = 0,
            items = emptyList()
        )

        val jsonString = json.encodeToString(agentReferenceList)
        val expectedJson = """
                {
                   "count":0,
                   "items":[]
                }
            """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_withEmptyList() {
        val jsonString = """
                {
                   "count":0,
                   "items":[]
                }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val agentReferenceList = json.decodeFromString<AgentReferenceList>(jsonString)
        assertEquals(0, agentReferenceList.count)
        assertTrue(agentReferenceList.items.isEmpty())
    }

    @Test
    fun deserialize_withExtraFields() {
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

    @Test(expected = Throwable::class)
    fun deserialize_withWrongTypeForCount_shouldFail() {
        val jsonString = """
                {
                   "count":"two",
                   "items":[
                      {
                         "id":"id",
                         "name":"name",
                         "pass":"pass"
                      }
                   ]
                }
            """.trimIndent().replace("\n", "").replace(" ", "")

        json.decodeFromString<AgentReferenceList>(jsonString)
    }
}