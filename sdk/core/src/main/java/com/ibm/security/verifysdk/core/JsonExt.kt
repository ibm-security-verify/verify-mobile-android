/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

fun Any?.toJsonElement(): JsonElement = when (this) {
    is Array<*> -> this.toJsonArray()
    is Boolean -> JsonPrimitive(this)
    is JsonElement -> this
    is List<*> -> this.toJsonArray()
    is Map<*, *> -> this.toJsonObject()
    is Number -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    else -> JsonNull
}

fun Array<*>.toJsonArray() = JsonArray(map { it.toJsonElement() })
fun Iterable<*>.toJsonArray() = JsonArray(map { it.toJsonElement() })
fun Map<*, *>.toJsonObject(): JsonObject =
    JsonObject(mapKeys { it.key.toString() }.mapValues { it.value.toJsonElement() })

fun Json.encodeToString(map: MutableMap<String, Any>) = encodeToString(map.toJsonElement())