/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class DidDoc(

    @SerialName("@context")
    val atContext: List<String>,

    @SerialName("id")
    val id: kotlin.String,

    @SerialName("service")
    val service: List<JsonElement>,

    @SerialName("verificationMethod")
    val verificationMethod: List<AriesDidDocPublicKey>? = null,

    @SerialName("authentication")
    val authentication: List<String>? = null,

    @SerialName("assertion")
    val assertion: List<String>? = null,

    @SerialName("keyAgreement")
    val keyAgreement: List<String>? = null,

    @SerialName("capabilityInvocation")
    val capabilityInvocation: List<String>? = null,

    @SerialName("capabilityDelegation")
    val capabilityDelegation: List<String>? = null,

    @SerialName("publicKey")
    val publicKey: List<AriesDidDocPublicKey>? = null
)
