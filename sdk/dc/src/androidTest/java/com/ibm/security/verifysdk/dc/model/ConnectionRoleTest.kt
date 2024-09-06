package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnectionRoleTest {

    @Test
    fun toString_forInviter() {
        val inviter = ConnectionRole.INVITER
        assertEquals("inviter", inviter.toString())
    }

    @Test
    fun toString_forInvitee() {
        val invitee = ConnectionRole.INVITEE
        assertEquals("invitee", invitee.toString())
    }

    @Test
    fun encode_withValidConnectionRole() {
        val inviter = ConnectionRole.INVITER
        val invitee = ConnectionRole.INVITEE

        assertEquals("inviter", ConnectionRole.encode(inviter))
        assertEquals("invitee", ConnectionRole.encode(invitee))
    }

    @Test
    fun encode_withNull() {
        assertNull(ConnectionRole.encode(null))
    }

    @Test
    fun encode_withValidType() {
        val invalidData = "someInvalidData"
        assertNull(ConnectionRole.encode(invalidData))
    }

    @Test
    fun decode_withValidString() {
        val inviterString = "inviter"
        val inviteeString = "invitee"

        assertEquals(ConnectionRole.INVITER, ConnectionRole.decode(inviterString))
        assertEquals(ConnectionRole.INVITEE, ConnectionRole.decode(inviteeString))
    }

    @Test
    fun decode_withInvalidString() {
        val invalidString = "invalid"
        assertNull(ConnectionRole.decode(invalidString))
    }

    @Test
    fun decode_withNull() {
        assertNull(ConnectionRole.decode(null))
    }
}
