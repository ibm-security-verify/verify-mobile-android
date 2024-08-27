package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class ConnectionInfoTest {

    private lateinit var endpointLocal: Endpoint
    private lateinit var endpointRemote: Endpoint

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Before
    fun setup() {

        val jsonStringEndpointLocal = """
            {
                "name": "localEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true
            }
             """.trimIndent().replace("\n", "").replace(" ", "")

        endpointLocal = json.decodeFromString<Endpoint>(jsonStringEndpointLocal)

        val jsonStringEndpointRemote = """
            {
                "name": "remoteEndpoint",
                "url": "https://example.com",
                "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                "ext": true
            }
             """.trimIndent().replace("\n", "").replace(" ", "")

        endpointRemote = json.decodeFromString<Endpoint>(jsonStringEndpointRemote)

    }

    @Test
    fun constructor_with_required_fields() {
        val id = "connectionId"
        val state = "active"
        val role = ConnectionRole.INVITER
        val didExchange = true

        val connectionInfo = ConnectionInfo(
            id = id,
            state = state,
            role = role,
            local = endpointLocal,
            didExchange = didExchange
        )

        assertEquals(id, connectionInfo.id)
        assertEquals(state, connectionInfo.state)
        assertEquals(role, connectionInfo.role)
        assertEquals(endpointLocal, connectionInfo.local)
        assertTrue(connectionInfo.didExchange)
        assertNull(connectionInfo.invitation)
        assertNull(connectionInfo.remote)
        assertNull(connectionInfo.timestamps)
        assertNull(connectionInfo.extComplete)
        assertNull(connectionInfo.maxQueueCount)
        assertNull(connectionInfo.maxQueueTimeMs)
    }

    @Test
    fun constructor_with_all_fields() {
        val id = "connectionId"
        val state = "active"
        val role = ConnectionRole.INVITEE
        val local = endpointLocal
        val remote = endpointRemote
        val didExchange = true
        val invitation = null
        val timestamps = mapOf("created" to JsonPrimitive(1625159400))
        val extComplete = true
        val maxQueueCount = 5.0
        val maxQueueTimeMs = 10000.0

        val connectionInfo = ConnectionInfo(
            id = id,
            state = state,
            role = role,
            local = local,
            remote = remote,
            didExchange = didExchange,
            invitation = invitation,
            timestamps = timestamps,
            extComplete = extComplete,
            maxQueueCount = maxQueueCount,
            maxQueueTimeMs = maxQueueTimeMs
        )

        assertEquals(id, connectionInfo.id)
        assertEquals(state, connectionInfo.state)
        assertEquals(role, connectionInfo.role)
        assertEquals(local, connectionInfo.local)
        assertEquals(remote, connectionInfo.remote)
        assertTrue(connectionInfo.didExchange)
        assertEquals(invitation, connectionInfo.invitation)
        assertEquals(timestamps, connectionInfo.timestamps)
        assertTrue(connectionInfo.extComplete!!)
        assertEquals(maxQueueCount, connectionInfo.maxQueueCount ?: 0)
        assertEquals(maxQueueTimeMs, connectionInfo.maxQueueTimeMs ?: 0)
    }

    @Test
    fun equal() {
        val id = "connectionId"
        val state = "active"
        val role = ConnectionRole.INVITER
        val local = endpointLocal
        val didExchange = true

        val connectionInfo1 = ConnectionInfo(
            id = id,
            state = state,
            role = role,
            local = local,
            didExchange = didExchange
        )

        val connectionInfo2 = ConnectionInfo(
            id = id,
            state = state,
            role = role,
            local = local,
            didExchange = didExchange
        )

        assertEquals(connectionInfo1, connectionInfo2)
    }

    @Test
    fun serialization_to_JSON() {
        val id = "connectionId"
        val state = "active"
        val role = ConnectionRole.INVITER
        val local = endpointLocal
        val didExchange = true

        val connectionInfo = ConnectionInfo(
            id = id,
            state = state,
            role = role,
            local = local,
            didExchange = didExchange
        )

        val jsonString = json.encodeToString(connectionInfo)
        val expectedJson = """
            {
                "id": "connectionId",
                "state": "active",
                "role": "inviter",
                "local": {
                    "name": "localEndpoint",
                    "url": "https://example.com",
                    "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                    "ext": true
                },
                "did_exchange": true
            }
        """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_JSON() {
        val jsonString = """
            {
                "id": "connectionId",
                "state": "active",
                "role": "inviter",
                "local": {
                    "name": "localEndpoint",
                    "url": "https://example.com",
                    "pairwise": {"did": "did:example:1234", "verkey": "12345"},
                    "ext": true
                },
                "did_exchange": true
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val connectionInfo = Json.decodeFromString(ConnectionInfo.serializer(), jsonString)

        assertEquals("connectionId", connectionInfo.id)
        assertEquals("active", connectionInfo.state)
        assertEquals(ConnectionRole.INVITER, connectionInfo.role)
        assertEquals(endpointLocal, connectionInfo.local)
        assertTrue(connectionInfo.didExchange)
    }
}
