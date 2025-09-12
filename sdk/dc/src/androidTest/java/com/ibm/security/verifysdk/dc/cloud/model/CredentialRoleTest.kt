package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.dc.model.CredentialRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CredentialRoleTest {

    @Test
    fun toString_method() {
        assertEquals("issuer", CredentialRole.ISSUER.toString())
        assertEquals("holder", CredentialRole.HOLDER.toString())
    }

    @Test
    fun encode() {
        assertEquals("issuer", CredentialRole.encode(CredentialRole.ISSUER))
        assertEquals("holder", CredentialRole.encode(CredentialRole.HOLDER))
    }

    @Test
    fun encode_withNullValue() {
        assertNull(CredentialRole.encode(null))
    }

    @Test
    fun encode_withInvalidValue() {
        assertNull(CredentialRole.encode("invalid"))
    }

    @Test
    fun testDecode() {
        assertEquals(CredentialRole.ISSUER, CredentialRole.decode("issuer"))
        assertEquals(CredentialRole.HOLDER, CredentialRole.decode("holder"))

        assertEquals(CredentialRole.ISSUER, CredentialRole.decode("ISSUER"))
        assertEquals(CredentialRole.HOLDER, CredentialRole.decode("HOLDER"))
    }

    @Test
    fun decode_withNullValue() {
        assertNull(CredentialRole.decode(null))
    }

    @Test
    fun decode_withInvalidValue() {
        assertNull(CredentialRole.decode("invalid"))
    }

    @Test
    fun testEquality() {
        assertEquals(CredentialRole.ISSUER, CredentialRole.decode("issuer"))
        assertEquals(CredentialRole.HOLDER, CredentialRole.decode("holder"))
    }
}