/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AriesDidDocPublicKey(

    @SerialName("id")
    val id: String,

    @SerialName("type")
    val type: String,

    @SerialName("controller")
    val controller: String,

    @SerialName("publicKeyBase58")
    val publicKeyBase58: String
)
