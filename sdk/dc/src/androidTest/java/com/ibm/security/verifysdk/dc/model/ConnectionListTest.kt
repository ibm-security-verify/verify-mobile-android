package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class ConnectionListTest {

    @Test
    fun deserialize_from_Json() {

        val id = "id"
        val state = "string"
        val role = ConnectionRole.INVITER
        val didExchange = true

        val jsonStringEndpointLocal = """
            {
                "name": "localEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true
            }
             """.trimIndent().replace("\n", "").replace(" ", "")

        val endpointLocal = json.decodeFromString<Endpoint>(jsonStringEndpointLocal)

        val connectionInfo1 = ConnectionInfo(
            id = "${id}01",
            state = state,
            role = role,
            local = endpointLocal,
            didExchange = didExchange
        )

        val connectionInfo2 = ConnectionInfo(
            id = "${id}02",
            state = state,
            role = role,
            local = endpointLocal,
            didExchange = didExchange
        )

        val items = listOf(connectionInfo1, connectionInfo2)

        val connectionList = ConnectionList(
            count = 2,
            items = items
        )

        assertEquals(2, connectionList.count)
        assertEquals(items, connectionList.items)
        assertEquals("${id}01", connectionList.items[0].id)
        assertEquals("${id}02", connectionList.items[1].id)
    }

    @Test
    fun initialize_withEmptyList() {
        val connectionList = ConnectionList(
            count = 0,
            items = emptyList()
        )

        assertEquals(0, connectionList.count)
        assertEquals(emptyList<ConnectionInfo>(), connectionList.items)
    }


}