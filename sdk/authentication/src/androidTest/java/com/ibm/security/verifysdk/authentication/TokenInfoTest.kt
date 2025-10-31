/*
 * Copyright contributors to the IBM Verify SDK for Android project
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
}