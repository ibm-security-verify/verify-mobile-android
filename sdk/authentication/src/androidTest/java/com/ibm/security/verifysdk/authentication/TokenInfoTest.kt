/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.authentication.model.TokenInfoSerializer.deserializeJsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TokenInfoTest {

    @Test
    fun deserialize_JsonElement_with_JsonNull() {
        val element = JsonNull
        val result = deserializeJsonElement(element)
        assertEquals(JsonNull, result)
    }

    @Test
    fun deserialize_JsonElement_with_null() {
        val element = JsonPrimitive("null")
        val result = deserializeJsonElement(element)
        assertEquals(JsonNull, result)
    }

    @Test
    fun deserialize_JsonElement_with_string() {
        val element = JsonPrimitive("hello")
        val result = deserializeJsonElement(element)
        assertEquals("hello", result)
    }

    @Test
    fun deserialize_JsonElement_with_boolean_true() {
        val element = JsonPrimitive("true")
        val result = deserializeJsonElement(element)
        assertEquals(true, result)
    }

    @Test
    fun deserialize_JsonElement_with_boolean_false() {
        val element = JsonPrimitive("false")
        val result = deserializeJsonElement(element)
        assertEquals(false, result)
    }

    @Test
    fun deserialize_JsonElement_with_integer() {
        val element = JsonPrimitive("42")
        val result = deserializeJsonElement(element)
        assertEquals(42, result)
    }

    @Test
    fun deserialize_JsonElement_with_float() {
        val element = JsonPrimitive("3.14")
        val result = deserializeJsonElement(element)
        assertEquals(3.14, result)
    }

    @Test
    fun `test deserialize with all fields`() {
        // Set up test data
        val json = """
            {
                "access_token": "access-token-value",
                "refresh_token": "refresh-token-value",
                "expires_in": 3600,
                "created_on": 1625159400,
                "expires_on": 1625163000,
                "scope": "read write",
                "token_type": "Bearer",
                "additional_field": "additional-value"
            }
        """.trimIndent()

        val decoder = mock(JsonDecoder::class.java)
        val jsonElement = Json.parseToJsonElement(json).jsonObject
        `when`(decoder.decodeSerializableValue(any())).thenReturn(jsonElement)

        // Call the method
        val result = TokenInfoSerializer.deserialize(decoder)

        // Assertions
        assertEquals("access-token-value", result.accessToken)
        assertEquals("refresh-token-value", result.refreshToken)
        assertEquals(Instant.ofEpochSecond(1625159400), result.createdOn)
        assertEquals(3600, result.expiresIn)
        assertEquals(Instant.ofEpochSecond(1625163000), result.expiresOn)
        assertEquals("read write", result.scope)
        assertEquals("Bearer", result.tokenType)
        assertEquals("additional-value", result.additionalData["additional_field"])
    }

    @Test
    fun `test deserialize with missing optional fields`() {
        // Set up test data
        val json = """
            {
                "access_token": "access-token-value",
                "expires_in": 3600
            }
        """.trimIndent()

        val decoder = mock(JsonDecoder::class.java)
        val jsonElement = Json.parseToJsonElement(json).jsonObject
        `when`(decoder.decodeSerializableValue(any())).thenReturn(jsonElement)

        // Call the method
        val result = TokenInfoSerializer.deserialize(decoder)

        // Assertions
        assertEquals("access-token-value", result.accessToken)
        assertEquals("", result.refreshToken)
        assertEquals(3600, result.expiresIn)
        assertTrue(result.createdOn.isAfter(Instant.now().minusSeconds(3600)))
        assertEquals(result.createdOn.plusSeconds(3600), result.expiresOn)
        assertEquals("", result.scope)
        assertEquals("Bearer", result.tokenType)
        assertTrue(result.additionalData.isEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test deserialize with invalid data throws exception`() {
        // Set up test data
        val json = """
            {
                "access_token": 12345
            }
        """.trimIndent()

        val decoder = mock(JsonDecoder::class.java)
        val jsonElement = Json.parseToJsonElement(json).jsonObject
        `when`(decoder.decodeSerializableValue(any())).thenReturn(jsonElement)

        // Call the method, expecting an exception
        TokenInfoSerializer.deserialize(decoder)
    }

    @Test
    fun `test deserialize with unknown fields`() {
        // Set up test data
        val json = """
            {
                "access_token": "access-token-value",
                "unknown_field": "unknown-value"
            }
        """.trimIndent()

        val decoder = mock(JsonDecoder::class.java)
        val jsonElement = Json.parseToJsonElement(json).jsonObject
        `when`(decoder.decodeSerializableValue(any())).thenReturn(jsonElement)

        // Call the method
        val result = TokenInfoSerializer.deserialize(decoder)

        // Assertions
        assertEquals("access-token-value", result.accessToken)
        assertEquals("unknown-value", result.additionalData["unknown_field"])
    }

    // Helper function for Mockito
    private fun <T> any(): T = Mockito.any<T>()

    @Test
    fun `test deserialize with multiple key variations`() {
        // Set up test data
        val json = """
            {
                "access_token": "access-token-value",
                "ACCESS_TOKEN": "overridden-value",
                "expires_in": 3600,
                "created_on": 1625159400,
                "expires_on": 1625163000,
                "scope": "read write",
                "token_type": "Bearer",
                "refresh_token": "refresh-token-value"
            }
        """.trimIndent()

        val decoder = mock(JsonDecoder::class.java)
        val jsonElement = Json.parseToJsonElement(json).jsonObject
        `when`(decoder.decodeSerializableValue(any())).thenReturn(jsonElement)

        // Call the method
        val result = TokenInfoSerializer.deserialize(decoder)

        // Assertions
        assertEquals("overridden-value", result.accessToken)
        assertEquals("refresh-token-value", result.refreshToken)
        assertEquals(Instant.ofEpochSecond(1625159400), result.createdOn)
        assertEquals(3600, result.expiresIn)
        assertEquals(Instant.ofEpochSecond(1625163000), result.expiresOn)
        assertEquals("read write", result.scope)
        assertEquals("Bearer", result.tokenType)
        assertTrue(result.additionalData.isEmpty())
    }
}