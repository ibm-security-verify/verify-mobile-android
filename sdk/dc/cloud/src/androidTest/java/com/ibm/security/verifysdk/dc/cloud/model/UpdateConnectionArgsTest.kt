package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpdateConnectionArgsTest {

    @Test
    fun initialize() {
        val state = ConnectionState.CONNECTED
        val properties = mapOf("key1" to "value1", "key2" to "value2")

        val args = UpdateConnectionArgs(
            state = state,
            properties = properties
        )
        assertEquals(ConnectionState.CONNECTED, args.state)
        assertEquals(properties, args.properties)
    }

    @Test
    fun initialize_withNullValues() {
        val state = ConnectionState.REJECTED

        val args = UpdateConnectionArgs(
            state = state,
            properties = null
        )
        assertEquals(ConnectionState.REJECTED, args.state)
        assertNull(args.properties)
    }

    fun initialize_withEmptyAttributes() {
        val state = ConnectionState.INBOUND_INVITATION
        val properties = emptyMap<String, String>()

        val args = UpdateConnectionArgs(
            state = state,
            properties = properties
        )
        assertTrue(args.properties!!.isEmpty())
    }

    @Test
    fun initialize_withSpecialCharacters() {
        val state = ConnectionState.OUTBOUND_OFFER
        val properties = mapOf("key!@#" to "value$%^")

        val args = UpdateConnectionArgs(
            state = state,
            properties = properties
        )
        assertEquals("value$%^", args.properties!!["key!@#"])
    }

    @Test
    fun initialize_withLongStrings() {
        val state = ConnectionState.DID_EXCHANGE_RESPONSE_SENT
        val longKey = "K".repeat(1000)
        val longValue = "V".repeat(1000)
        val properties = mapOf(longKey to longValue)

        val args = UpdateConnectionArgs(
            state = state,
            properties = properties
        )
        assertEquals(longValue, args.properties!![longKey])
    }

    @Test
    fun deserialize_from_Json() {
        val jsonString = """
        {
            "state": "inbound_invitation",
            "properties": {
                "key1": "value1",
                "key2": "value2"
            }
        }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val updateConnectionArgs =
            json.decodeFromString(UpdateConnectionArgs.serializer(), jsonString)

        assertEquals("value1", updateConnectionArgs.properties!!["key1"])
        assertEquals("value2", updateConnectionArgs.properties!!["key2"])
        assertEquals(ConnectionState.INBOUND_INVITATION, updateConnectionArgs.state)
    }

    @Test
    fun serialize_withNullValues() {
        val expectedJson = """
        {
            "state": "rejected"
        }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val updateConnectionArgs = UpdateConnectionArgs(
            state = ConnectionState.REJECTED,
            properties = null
        )

        val actualJson = json.encodeToString(updateConnectionArgs)
        assertEquals(ConnectionState.REJECTED, updateConnectionArgs.state)
        assertEquals(expectedJson, actualJson)
        assertNull(updateConnectionArgs.properties)
    }
}