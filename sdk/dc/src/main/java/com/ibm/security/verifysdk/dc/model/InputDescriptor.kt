/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class InputDescriptor(

    @SerialName("id")
    val id: String,

    @SerialName("schema")
    val schema: List<Schema>,

    @SerialName("name")
    val name: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("group")
    val group: List<String>? = null,

    @SerialName("issuance")
    val issuance: List<JsonElement>? = null,

    @SerialName("constraints")
    val constraints: Constraints? = null
)
