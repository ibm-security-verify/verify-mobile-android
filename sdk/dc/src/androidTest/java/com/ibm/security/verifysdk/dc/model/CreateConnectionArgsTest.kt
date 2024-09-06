package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalSerializationApi::class)
class CreateConnectionArgsTest {

    @Test
    fun initialize_withNullValues() {
        val args = CreateConnectionArgs();
        assertNull(args.directRoute);
        assertNull(args.to);
        assertNull(args.properties);
        assertNull(args.url);
        assertNull(args.noDidExchangeResponse);
        assertNull(args.id);
        assertNull(args.state);
        assertNull(args.role);
        assertNull(args.local);
        assertNull(args.didExchange);
        assertNull(args.invitation);
        assertNull(args.remote);
        assertNull(args.timestamps);
        assertNull(args.extComplete);
        assertNull(args.maxQueueCount);
        assertNull(args.maxQueueTimeMs);
        assertNull(args.tildeThread);
    }

    @Test
    fun initialize_withParticalValues() {
        val args = CreateConnectionArgs(
            directRoute = true,
            properties = mapOf("key1" to "value1"),
            url = "https://localhost",
            noDidExchangeResponse = false,
            id = "connection-id",
            state = "active",
            role = "initiator",
            didExchange = true,
            extComplete = true,
            maxQueueCount = 10,
            maxQueueTimeMs = 5000,
            tildeThread = "thread-id"
        )

        assertEquals(true, args.directRoute)
        assertEquals(null, args.to)
        assertEquals(mapOf("key1" to "value1"), args.properties)
        assertEquals("https://localhost", args.url)
        assertEquals(false, args.noDidExchangeResponse)
        assertEquals("connection-id", args.id)
        assertEquals("active", args.state)
        assertEquals("initiator", args.role)
        assertEquals(null, args.local)
        assertEquals(true, args.didExchange)
        assertEquals(null, args.invitation)
        assertEquals(null, args.remote)
        assertEquals(null, args.timestamps)
        assertEquals(true, args.extComplete)
        assertEquals(10, args.maxQueueCount)
        assertEquals(5000, args.maxQueueTimeMs)
        assertEquals("thread-id", args.tildeThread)
    }

    @Test
    fun initialize_withMapProperties() {

        val instant = Instant.now().toEpochMilli()
        val properties = mapOf("prop1" to "value1", "prop2" to "value2")
        val timeStampsValue: Map<String, TimeStampsValue> = mapOf(
            "created_at" to JsonPrimitive(instant),
            "updated_at" to JsonPrimitive(instant + 1L)
        )

        val args = CreateConnectionArgs(
            properties = properties,
            timestamps = timeStampsValue
        )

        assertEquals(properties, args.properties)
        assertEquals(timeStampsValue, args.timestamps)
    }

    @Test
    fun initialize_withBooleanValues() {
        val args = CreateConnectionArgs(
            directRoute = true,
            noDidExchangeResponse = false,
            didExchange = true,
            extComplete = false
        )
        assertEquals(true, args.directRoute)
        assertEquals(false, args.noDidExchangeResponse)
        assertEquals(true, args.didExchange)
        assertEquals(false, args.extComplete)
    }
}