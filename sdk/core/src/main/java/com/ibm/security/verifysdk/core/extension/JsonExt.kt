/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.extension

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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
