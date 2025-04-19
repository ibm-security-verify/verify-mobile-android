package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InvitationRoleTest {

    @Test
    fun encode_withInverter() {
        val encodedInviter = InvitationRole.encode(InvitationRole.INVITER)
        assertEquals("inviter", encodedInviter)
    }

    @Test
    fun encode_withInvitee() {
        val encodedInvitee = InvitationRole.encode(InvitationRole.INVITEE)
        assertEquals("invitee", encodedInvitee)
    }

    @Test
    fun encode_withInvalid() {
        val encodedInvalid = InvitationRole.encode("invalid")
        assertNull(encodedInvalid)
    }

    @Test
    fun decode_withInverterExactMatch() {
        val decodedInviterExact = InvitationRole.decode("inviter")
        assertEquals(InvitationRole.INVITER, decodedInviterExact)
    }

    @Test
    fun decode_withInviteeExactMatch() {
        val decodedInviteeExact = InvitationRole.decode("invitee")
        assertEquals(InvitationRole.INVITEE, decodedInviteeExact)
    }

    @Test
    fun decode_withInverter() {
        val decodedInviterCaseInsensitive = InvitationRole.decode("Inviter")
        assertEquals(InvitationRole.INVITER, decodedInviterCaseInsensitive)
    }

    @Test
    fun decode_withInvitee() {
        val decodedInviteeUpperCase = InvitationRole.decode("INVITEE")
        assertEquals(InvitationRole.INVITEE, decodedInviteeUpperCase)
    }

    @Test
    fun decode_withNull()   {
        val decodedNull = InvitationRole.decode(null)
        assertNull(decodedNull)
    }

    @Test
    fun decode_withInvalid()   {
        val decodedInvalid = InvitationRole.decode("invalid")
        assertNull(decodedInvalid)
    }

    @Test
    fun decode_withEqualValue()   {
        val decodedEnumDirectMatch = InvitationRole.decode(InvitationRole.INVITER)
        assertEquals(InvitationRole.INVITER, decodedEnumDirectMatch)
    }

    @Test
    fun decode_withNonExactMatch()   {
        val decodedMixedCaseMatch = InvitationRole.decode("Inviter")
        assertEquals(InvitationRole.INVITER, decodedMixedCaseMatch)
    }
}