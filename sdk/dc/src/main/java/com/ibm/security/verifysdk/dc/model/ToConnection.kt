/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class ToConnection (

    @SerialName("name")
    val name: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("url")
    val url: String? = null,

    @SerialName("did")
    val did: String? = null,

    @SerialName("verkey")
    val verkey: String? = null,

    @SerialName("invitation")
    val invitation: ToInvitation? = null
)
