/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class Filter(

    @SerialName("type")
    val type: String,

    @SerialName("const")
    val `const`: JsonPrimitive? = null,

    @SerialName("enum")
    val `enum`: List<JsonPrimitive>? = null,

    @SerialName("exclusiveMinimum")
    val exclusiveMinimum: JsonPrimitive? = null,

    @SerialName("exclusiveMaximum")
    val exclusiveMaximum: JsonPrimitive? = null,

    @SerialName("format")
    val format: String? = null,

    @SerialName("minLength")
    val minLength: Double? = null,

    @SerialName("maxLength")
    val maxLength: Double? = null,

    @SerialName("minimum")
    val minimum: JsonPrimitive? = null,

    @SerialName("maximum")
    val maximum: JsonPrimitive? = null,

    @SerialName("not")
    val not: JsonElement? = null,

    @SerialName("pattern")
    val pattern: String? = null
)
