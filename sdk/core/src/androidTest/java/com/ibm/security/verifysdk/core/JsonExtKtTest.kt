package com.ibm.security.verifysdk.core

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class JsonExtKtTest {

    @Test
    fun toJsonElement() {
        assertEquals(JsonPrimitive(true).toString(), true.toJsonElement().toString())
        assertEquals(JsonPrimitive(42).toString(), 42.toJsonElement().toString())
        assertEquals(JsonPrimitive("wtf").toString(), "wtf".toJsonElement().toString())

        val listElements = ArrayList<String>()
        listElements.add("e1")
        listElements.add("e2")
        assertEquals(JsonArray(listElements.map { JsonPrimitive(it) }), listElements.toJsonArray())

        val listNull = ArrayList<String?>()
        listNull.add(null)
        assertEquals(JsonArray(listNull.map { JsonPrimitive(it) }), listNull.toJsonElement())

        val array = arrayOf("e3", "e4")
        assertEquals(JsonArray(array.map { JsonPrimitive(it) }), array.toJsonArray())

        val arrayNull = arrayOf(JsonNull, JsonNull)
        assertEquals(JsonArray(arrayNull.map { JsonNull }), arrayNull.toJsonArray())

        val testMapInt = HashMap<String, Int>()
        testMapInt["k1"] = 1
        testMapInt["k2"] = 2
        assertEquals(JsonObject(testMapInt.mapKeys { it.key }
            .mapValues { it.value.toJsonElement() }), testMapInt.toJsonObject())

        val e = Exception()
        assertEquals(JsonNull, e.toJsonElement())
        assertEquals(JsonNull, JsonNull.toJsonElement())
    }
}