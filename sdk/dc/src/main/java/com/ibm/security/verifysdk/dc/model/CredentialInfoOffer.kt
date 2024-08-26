/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CredentialInfoOffer (

    @SerialName("jsonld")
    val jsonld: JsonElement? = null,

    @SerialName("attributes")
    val attributes: Map<String, JsonElement>? = null
)
