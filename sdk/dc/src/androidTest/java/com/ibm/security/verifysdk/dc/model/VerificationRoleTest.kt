package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VerificationRoleTest {

    @Test
    fun toString_method() {
        assertEquals("verifier", VerificationRole.VERIFIER.toString())
        assertEquals("prover", VerificationRole.PROVER.toString())
    }

    @Test
    fun encode_withValidRole() {
        val verifierRole = VerificationRole.VERIFIER
        val proverRole = VerificationRole.PROVER

        assertEquals("verifier", VerificationRole.encode(verifierRole))
        assertEquals("prover", VerificationRole.encode(proverRole))
    }

    @Test
    fun encode_withInvalidRole() {
        val invalidRole: Any = "invalid"
        assertNull(VerificationRole.encode(invalidRole))
    }

    @Test
    fun decode_withValidRole() {
        assertEquals(VerificationRole.VERIFIER, VerificationRole.decode("verifier"))
        assertEquals(VerificationRole.PROVER, VerificationRole.decode("prover"))
    }

    @Test
    fun decode_withCaseInsensitive() {
        assertEquals(VerificationRole.VERIFIER, VerificationRole.decode("Verifier"))
        assertEquals(VerificationRole.PROVER, VerificationRole.decode("Prover"))
    }

    @Test
    fun decode_withInvalidRole() {
        assertNull(VerificationRole.decode("invalid_role"))
    }

    @Test
    fun decode_withNull() {
        assertNull(VerificationRole.decode(null))
    }

    @Test
    fun serialization_to_Json() {
        val verifierRole = VerificationRole.VERIFIER
        val proverRole = VerificationRole.PROVER

        val jsonVerifier = json.encodeToString(verifierRole)
        val jsonProver = json.encodeToString(proverRole)

        assertEquals("\"verifier\"", jsonVerifier)
        assertEquals("\"prover\"", jsonProver)
    }

    @Test
    fun deserialize_from_Json() {
        val jsonVerifier = "\"verifier\""
        val jsonProver = "\"prover\""

        val decodedVerifier = json.decodeFromString<VerificationRole>(jsonVerifier)
        val decodedProver = json.decodeFromString<VerificationRole>(jsonProver)

        assertEquals(VerificationRole.VERIFIER, decodedVerifier)
        assertEquals(VerificationRole.PROVER, decodedProver)
    }

    @Test
    fun equal() {
        assertEquals(VerificationRole.VERIFIER, VerificationRole.decode("verifier"))
        assertEquals(VerificationRole.PROVER, VerificationRole.decode("prover"))
    }

    @Test
    fun notEqual() {
        assertNotEquals(VerificationRole.VERIFIER, VerificationRole.decode("prover"))
        assertNotEquals(VerificationRole.PROVER, VerificationRole.decode("verifier"))
    }

}