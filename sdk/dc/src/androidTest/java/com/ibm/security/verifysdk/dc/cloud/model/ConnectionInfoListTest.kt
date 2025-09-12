package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.dc.model.ConnectionAgentInfo
import com.ibm.security.verifysdk.dc.model.ConnectionInfo
import com.ibm.security.verifysdk.dc.model.ConnectionInfoList
import com.ibm.security.verifysdk.dc.model.ConnectionRole
import com.ibm.security.verifysdk.dc.model.ConnectionState
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class ConnectionInfoListTest {

    @Test
    fun deserialize_from_Json() {

        val id = "id"
        val state = ConnectionState.CONNECTED
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

        val jsonStringEndpointRemote = """
            {
                "name": "remoteEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true
            }
             """.trimIndent().replace("\n", "").replace(" ", "")

        val connectionAgentInfoLocal = json.decodeFromString<ConnectionAgentInfo>(jsonStringEndpointLocal)
        val connectionAgentInfoRemote = json.decodeFromString<ConnectionAgentInfo>(jsonStringEndpointRemote)

        val connectionInfo1 = ConnectionInfo(
            id = "${id}01",
            state = state,
            role = role,
            local = connectionAgentInfoLocal,
            remote = connectionAgentInfoRemote
        )

        val connectionInfo2 = ConnectionInfo(
            id = "${id}02",
            state = state,
            role = role,
            local = connectionAgentInfoLocal,
            remote = connectionAgentInfoRemote
        )

        val items = listOf(connectionInfo1, connectionInfo2)

        val connectionInfoList = ConnectionInfoList(
            count = 2,
            items = items
        )

        assertEquals(2, connectionInfoList.count)
        assertEquals(items, connectionInfoList.items)
        assertEquals("${id}01", connectionInfoList.items[0].id)
        assertEquals("${id}02", connectionInfoList.items[1].id)
    }

    @Test
    fun initialize_withEmptyList() {
        val connectionInfoList = ConnectionInfoList(
            count = 0,
            items = emptyList()
        )

        assertEquals(0, connectionInfoList.count)
        assertEquals(emptyList<ConnectionInfo>(), connectionInfoList.items)
    }


}