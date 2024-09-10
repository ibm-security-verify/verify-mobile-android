package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InvitationTypeTest{

    @Test
    fun toString_method() {
        assertEquals("oob", InvitationType.OOB.toString())
        assertEquals("connection", InvitationType.CONNECTION.toString())
    }

    @Test
    fun encode_withValidType() {
        val oobType = InvitationType.OOB
        val connectionType = InvitationType.CONNECTION

        assertEquals("oob", InvitationType.encode(oobType))
        assertEquals("connection", InvitationType.encode(connectionType))
    }

    @Test
    fun encode_withInvalidType() {
        val invalidType: Any = "invalid"
        assertNull(InvitationType.encode(invalidType))
    }

    @Test
    fun decode_withValidType() {
        assertEquals(InvitationType.OOB, InvitationType.decode("oob"))
        assertEquals(InvitationType.CONNECTION, InvitationType.decode("connection"))
    }

    @Test
    fun decode_withCaseInsensitive() {
        assertEquals(InvitationType.OOB, InvitationType.decode("Oob"))
        assertEquals(InvitationType.CONNECTION, InvitationType.decode("Connection"))
    }

    @Test
    fun decode_withInvalidType() {
        assertNull(InvitationType.decode("invalid_Type"))
    }

    @Test
    fun decode_withNull() {
        assertNull(InvitationType.decode(null))
    }

    @Test
    fun serialization_to_Json() {
        val oobType = InvitationType.OOB
        val connectionType = InvitationType.CONNECTION

        val jsonOob = json.encodeToString(oobType)
        val jsonConnection = json.encodeToString(connectionType)

        assertEquals("\"oob\"", jsonOob)
        assertEquals("\"connection\"", jsonConnection)
    }

    @Test
    fun deserialize_from_Json() {
        val jsonOob = "\"oob\""
        val jsonConnection = "\"connection\""

        val decodedOob = json.decodeFromString<InvitationType>(jsonOob)
        val decodedConnection = json.decodeFromString<InvitationType>(jsonConnection)

        assertEquals(InvitationType.OOB, decodedOob)
        assertEquals(InvitationType.CONNECTION, decodedConnection)
    }

    @Test
    fun equal() {
        assertEquals(InvitationType.OOB, InvitationType.decode("oob"))
        assertEquals(InvitationType.CONNECTION, InvitationType.decode("connection"))
    }

    @Test
    fun notEqual() {
        assertNotEquals(InvitationType.OOB, InvitationType.decode("connection"))
        assertNotEquals(InvitationType.CONNECTION, InvitationType.decode("oob"))
    }
}