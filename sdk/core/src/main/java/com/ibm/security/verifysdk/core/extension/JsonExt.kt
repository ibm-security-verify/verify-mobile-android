/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.extension

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

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

fun JsonObject.getJsonArrayOrNull(path: String): List<JsonElement>? {
    return this[path]?.jsonArray?.toList()
}

fun JsonObject.getStringOrNull(path: String): String? {
    return this[path]?.jsonPrimitive?.content
}

fun JsonObject.getStringOrThrow(key: String): String {
    return this[key]?.jsonPrimitive?.content ?: throw SerializationException("Missing $key")
}

fun JsonObject.getStringList(key: String): List<String> {
    return this[key]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull } ?: emptyList()
}


