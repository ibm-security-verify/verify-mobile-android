package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnectionStateTest {

    @Test
    fun to_String() {
        assertEquals("inbound_invitation", ConnectionState.INBOUND_INVITATION.toString())
        assertEquals("outbound_offer", ConnectionState.OUTBOUND_OFFER.toString())
        assertEquals("inbound_offer", ConnectionState.INBOUND_OFFER.toString())
        assertEquals(
            "did_exchange_response_sent",
            ConnectionState.DID_EXCHANGE_RESPONSE_SENT.toString()
        )
        assertEquals("connected", ConnectionState.CONNECTED.toString())
        assertEquals("rejected", ConnectionState.REJECTED.toString())
    }

    @Test
    fun encode() {
        assertEquals(
            "inbound_invitation",
            ConnectionState.encode(ConnectionState.INBOUND_INVITATION)
        )
        assertEquals("outbound_offer", ConnectionState.encode(ConnectionState.OUTBOUND_OFFER))
        assertEquals("inbound_offer", ConnectionState.encode(ConnectionState.INBOUND_OFFER))
        assertEquals(
            "did_exchange_response_sent",
            ConnectionState.encode(ConnectionState.DID_EXCHANGE_RESPONSE_SENT)
        )
        assertEquals("connected", ConnectionState.encode(ConnectionState.CONNECTED))
        assertEquals("rejected", ConnectionState.encode(ConnectionState.REJECTED))

        // Test with non-ConnectionState data
        assertNull(ConnectionState.encode("invalid_data"))
        assertNull(ConnectionState.encode(null))
    }

    @Test
    fun decode() {
        assertEquals(
            ConnectionState.INBOUND_INVITATION,
            ConnectionState.decode("inbound_invitation")
        )
        assertEquals(ConnectionState.OUTBOUND_OFFER, ConnectionState.decode("outbound_offer"))
        assertEquals(ConnectionState.INBOUND_OFFER, ConnectionState.decode("inbound_offer"))
        assertEquals(
            ConnectionState.DID_EXCHANGE_RESPONSE_SENT,
            ConnectionState.decode("did_exchange_response_sent")
        )
        assertEquals(ConnectionState.CONNECTED, ConnectionState.decode("connected"))
        assertEquals(ConnectionState.REJECTED, ConnectionState.decode("rejected"))

        // Test case-insensitive decoding
        assertEquals(
            ConnectionState.INBOUND_INVITATION,
            ConnectionState.decode("INBOUND_INVITATION")
        )
        assertEquals(ConnectionState.OUTBOUND_OFFER, ConnectionState.decode("OUTBOUND_OFFER"))

        // Test with invalid and null data
        assertNull(ConnectionState.decode("invalid_data"))
        assertNull(ConnectionState.decode(null))
    }

    @Test
    fun decode_withEnumConstants() {
        assertEquals(
            ConnectionState.INBOUND_INVITATION,
            ConnectionState.decode(ConnectionState.INBOUND_INVITATION)
        )
        assertEquals(
            ConnectionState.OUTBOUND_OFFER,
            ConnectionState.decode(ConnectionState.OUTBOUND_OFFER)
        )
        assertEquals(
            ConnectionState.INBOUND_OFFER,
            ConnectionState.decode(ConnectionState.INBOUND_OFFER)
        )
        assertEquals(
            ConnectionState.DID_EXCHANGE_RESPONSE_SENT,
            ConnectionState.decode(ConnectionState.DID_EXCHANGE_RESPONSE_SENT)
        )
        assertEquals(ConnectionState.CONNECTED, ConnectionState.decode(ConnectionState.CONNECTED))
        assertEquals(ConnectionState.REJECTED, ConnectionState.decode(ConnectionState.REJECTED))

        // Test with null
        assertNull(ConnectionState.decode(null))
    }

    @Test
    fun encode_withInvalidValues() {
        assertNull(ConnectionState.encode("random_string"))
        assertNull(ConnectionState.encode(null))
    }

    @Test
    fun decode_withInvalidValues() {
        assertNull(ConnectionState.decode("random_string"))
        assertNull(ConnectionState.decode(null))
    }

    @Test
    fun get_value() {
        assertEquals("inbound_invitation", ConnectionState.INBOUND_INVITATION.value)
        assertEquals("outbound_offer", ConnectionState.OUTBOUND_OFFER.value)
        assertEquals("inbound_offer", ConnectionState.INBOUND_OFFER.value)
        assertEquals("did_exchange_response_sent", ConnectionState.DID_EXCHANGE_RESPONSE_SENT.value)
        assertEquals("connected", ConnectionState.CONNECTED.value)
        assertEquals("rejected", ConnectionState.REJECTED.value)
    }
}