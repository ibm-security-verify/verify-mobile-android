package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.dc.model.VerificationState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VerificationStateTest {

    @Test
    fun toString_method() {
        assertEquals(
            "outbound_verification_request",
            VerificationState.OUTBOUND_VERIFICATION_REQUEST.toString()
        )
        assertEquals(
            "inbound_verification_request",
            VerificationState.INBOUND_VERIFICATION_REQUEST.toString()
        )
        assertEquals("outbound_proof_request", VerificationState.OUTBOUND_PROOF_REQUEST.toString())
        assertEquals("inbound_proof_request", VerificationState.INBOUND_PROOF_REQUEST.toString())
        assertEquals("proof_generated", VerificationState.PROOF_GENERATED.toString())
        assertEquals("proof_shared", VerificationState.PROOF_SHARED.toString())
        assertEquals("passed", VerificationState.PASSED.toString())
        assertEquals("failed", VerificationState.FAILED.toString())
        assertEquals("deleted", VerificationState.DELETED.toString())
    }

    @Test
    fun encode() {
        assertEquals(
            "outbound_verification_request",
            VerificationState.encode(VerificationState.OUTBOUND_VERIFICATION_REQUEST)
        )
        assertEquals(
            "inbound_verification_request",
            VerificationState.encode(VerificationState.INBOUND_VERIFICATION_REQUEST)
        )
        assertEquals(
            "outbound_proof_request",
            VerificationState.encode(VerificationState.OUTBOUND_PROOF_REQUEST)
        )
        assertEquals(
            "inbound_proof_request",
            VerificationState.encode(VerificationState.INBOUND_PROOF_REQUEST)
        )
        assertEquals("proof_generated", VerificationState.encode(VerificationState.PROOF_GENERATED))
        assertEquals("proof_shared", VerificationState.encode(VerificationState.PROOF_SHARED))
        assertEquals("passed", VerificationState.encode(VerificationState.PASSED))
        assertEquals("failed", VerificationState.encode(VerificationState.FAILED))
        assertEquals("deleted", VerificationState.encode(VerificationState.DELETED))
    }


    @Test
    fun encode_withNulValue() {
        assertNull(VerificationState.encode(null))
    }

    @Test
    fun encode_withInvalidValue() {
        assertNull(VerificationState.encode("invalid"))
    }

    @Test
    fun decode() {
        assertEquals(
            VerificationState.OUTBOUND_VERIFICATION_REQUEST,
            VerificationState.decode("outbound_verification_request")
        )
        assertEquals(
            VerificationState.INBOUND_VERIFICATION_REQUEST,
            VerificationState.decode("inbound_verification_request")
        )
        assertEquals(
            VerificationState.OUTBOUND_PROOF_REQUEST,
            VerificationState.decode("outbound_proof_request")
        )
        assertEquals(
            VerificationState.INBOUND_PROOF_REQUEST,
            VerificationState.decode("inbound_proof_request")
        )
        assertEquals(VerificationState.PROOF_GENERATED, VerificationState.decode("proof_generated"))
        assertEquals(VerificationState.PROOF_SHARED, VerificationState.decode("proof_shared"))
        assertEquals(VerificationState.PASSED, VerificationState.decode("passed"))
        assertEquals(VerificationState.FAILED, VerificationState.decode("failed"))
        assertEquals(VerificationState.DELETED, VerificationState.decode("deleted"))
    }


    @Test
    fun decode_withCaseInsensitive() {
        assertEquals(
            VerificationState.OUTBOUND_VERIFICATION_REQUEST,
            VerificationState.decode("OUTBOUND_VERIFICATION_REQUEST")
        )
        assertEquals(
            VerificationState.INBOUND_VERIFICATION_REQUEST,
            VerificationState.decode("INBOUND_VERIFICATION_REQUEST")
        )
    }

    @Test
    fun decode_withInvalidValue() {
        assertNull(VerificationState.decode("invalid"))
    }


    @Test
    fun decode_withNullValue() {
        assertNull(VerificationState.decode(null))
    }

    @Test
    fun equal() {
        assertEquals(
            VerificationState.OUTBOUND_VERIFICATION_REQUEST,
            VerificationState.decode("outbound_verification_request")
        )
        assertEquals(
            VerificationState.INBOUND_VERIFICATION_REQUEST,
            VerificationState.decode("inbound_verification_request")
        )
    }
}