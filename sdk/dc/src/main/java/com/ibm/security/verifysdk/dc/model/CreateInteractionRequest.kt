/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class CreateInteractionRequest (

    @SerialName("role")
    val role: String,

    @SerialName("idoc_did")
    val idocDid: String,

    @SerialName("mode")
    val mode: InteractionMode? = null,

    @SerialName("to")
    val to: ToConnection? = null,

    @SerialName("direct_route")
    val directRoute: Boolean? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null

)
