package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DirectivesTest {

    @Test
    fun toString_method() {
        assertEquals("required", Directives.REQUIRED.toString())
        assertEquals("allowed", Directives.ALLOWED.toString())
        assertEquals("disallowed", Directives.DISALLOWED.toString())
    }

    @Test
    fun encode() {
        assertEquals("required", Directives.encode(Directives.REQUIRED))
        assertEquals("allowed", Directives.encode(Directives.ALLOWED))
        assertEquals("disallowed", Directives.encode(Directives.DISALLOWED))
    }

    @Test
    fun encode_withInvalidData() {
        assertNull(Directives.encode(null))
        assertNull(Directives.encode("invalid"))
    }

    @Test
    fun decode() {
        assertEquals(Directives.REQUIRED, Directives.decode("required"))
        assertEquals(Directives.ALLOWED, Directives.decode("allowed"))
        assertEquals(Directives.DISALLOWED, Directives.decode("disallowed"))
    }

    @Test
    fun decode_withCaseInsensitive() {
        assertEquals(Directives.REQUIRED, Directives.decode("REQUIRED"))
        assertEquals(Directives.ALLOWED, Directives.decode("AlLoWeD"))
        assertEquals(Directives.DISALLOWED, Directives.decode("dIsAlLoWeD"))
    }

    @Test
    fun decode_withInvalidData() {
        assertNull(Directives.decode(null))
        assertNull(Directives.decode("invalid"))
    }

    @Test
    fun serialization() {
        val jsonString = json.encodeToString(Directives.REQUIRED)
        assertEquals("\"required\"", jsonString)

        val jsonStringAllowed = json.encodeToString(Directives.ALLOWED)
        assertEquals("\"allowed\"", jsonStringAllowed)

        val jsonStringDisallowed = json.encodeToString(Directives.DISALLOWED)
        assertEquals("\"disallowed\"", jsonStringDisallowed)
    }

    @Test
    fun deserialization() {
        val directive = json.decodeFromString<Directives>("\"required\"")
        assertEquals(Directives.REQUIRED, directive)

        val directiveAllowed = json.decodeFromString<Directives>("\"allowed\"")
        assertEquals(Directives.ALLOWED, directiveAllowed)

        val directiveDisallowed = json.decodeFromString<Directives>("\"disallowed\"")
        assertEquals(Directives.DISALLOWED, directiveDisallowed)
    }

    @Test(expected = SerializationException::class)
    fun deserialization_withInvalidData() {
        json.decodeFromString<Directives>("\"invalid\"")
    }

    @Test
    fun serialization_followedBy_decodeFromString() {
        val directive = Directives.REQUIRED
        val jsonString = json.encodeToString(directive)
        val deserializedDirective = json.decodeFromString<Directives>(jsonString)

        assertEquals(directive, deserializedDirective)
    }

    @Test
    fun serialization_followedBy_decodeFromString_withOtherValues() {
        val directiveAllowed = Directives.ALLOWED
        val jsonStringAllowed = json.encodeToString(directiveAllowed)
        val deserializedAllowed = json.decodeFromString<Directives>(jsonStringAllowed)

        assertEquals(directiveAllowed, deserializedAllowed)

        val directiveDisallowed = Directives.DISALLOWED
        val jsonStringDisallowed = json.encodeToString(directiveDisallowed)
        val deserializedDisallowed = json.decodeFromString<Directives>(jsonStringDisallowed)

        assertEquals(directiveDisallowed, deserializedDisallowed)
    }
}