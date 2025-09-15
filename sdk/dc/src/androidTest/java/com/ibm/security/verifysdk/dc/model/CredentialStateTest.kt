package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CredentialStateTest {

    @Test
    fun toString_method() {
        assertEquals("outbound_request", CredentialState.OUTBOUND_REQUEST.toString())
        assertEquals("inbound_request", CredentialState.INBOUND_REQUEST.toString())
        assertEquals("accepted", CredentialState.ACCEPTED.toString())
    }

    @Test
    fun encode_withValidState() {
        assertEquals("outbound_request", CredentialState.encode(CredentialState.OUTBOUND_REQUEST))
        assertEquals("inbound_request", CredentialState.encode(CredentialState.INBOUND_REQUEST))
        assertEquals("accepted", CredentialState.encode(CredentialState.ACCEPTED))
    }

    @Test
    fun encode_withInvalidState() {
        val invalidState: Any = "invalid"
        assertNull(CredentialState.encode(invalidState))
    }

    @Test
    fun decode_withValidState() {
        assertEquals(CredentialState.OUTBOUND_REQUEST, CredentialState.decode("outbound_request"))
        assertEquals(CredentialState.INBOUND_REQUEST, CredentialState.decode("inbound_request"))
        assertEquals(CredentialState.ACCEPTED, CredentialState.decode("accepted"))
    }

    @Test
    fun decode_withCaseInsensitive() {
        assertEquals(CredentialState.OUTBOUND_REQUEST, CredentialState.decode("Outbound_Request"))
        assertEquals(CredentialState.INBOUND_REQUEST, CredentialState.decode("Inbound_Request"))
        assertEquals(CredentialState.ACCEPTED, CredentialState.decode("Accepted"))
    }

    @Test
    fun decode_withInvalidState() {
        assertNull(CredentialState.decode("invalid_state"))
    }

    @Test
    fun decode_withNull() {
        assertNull(CredentialState.decode(null))
    }

    @Test
    fun serialization() {
        val outboundRequest = CredentialState.OUTBOUND_REQUEST
        val inboundRequest = CredentialState.INBOUND_REQUEST
        val accepted = CredentialState.ACCEPTED

        val jsonOutbound = json.encodeToString(outboundRequest)
        val jsonInbound = json.encodeToString(inboundRequest)
        val jsonAccepted = json.encodeToString(accepted)

        assertEquals("\"outbound_request\"", jsonOutbound)
        assertEquals("\"inbound_request\"", jsonInbound)
        assertEquals("\"accepted\"", jsonAccepted)
    }

    @Test
    fun deserialization() {
        val jsonOutbound = "\"outbound_request\""
        val jsonInbound = "\"inbound_request\""
        val jsonAccepted = "\"accepted\""

        val decodedOutbound = json.decodeFromString<CredentialState>(jsonOutbound)
        val decodedInbound = json.decodeFromString<CredentialState>(jsonInbound)
        val decodedAccepted = json.decodeFromString<CredentialState>(jsonAccepted)

        assertEquals(CredentialState.OUTBOUND_REQUEST, decodedOutbound)
        assertEquals(CredentialState.INBOUND_REQUEST, decodedInbound)
        assertEquals(CredentialState.ACCEPTED, decodedAccepted)
    }

    @Test
    fun equal() {
        assertEquals(CredentialState.OUTBOUND_REQUEST, CredentialState.decode("outbound_request"))
        assertEquals(CredentialState.INBOUND_REQUEST, CredentialState.decode("inbound_request"))
    }

    @Test
    fun notEqual() {
        assertNotEquals(CredentialState.OUTBOUND_REQUEST, CredentialState.decode("inbound_request"))
        assertNotEquals(CredentialState.INBOUND_REQUEST, CredentialState.decode("outbound_request"))
    }
}