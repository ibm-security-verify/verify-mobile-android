/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class ToInvitation(

    @SerialName("url")
    val url: String? = null,

    @SerialName("accept_invitation_manually")
    val acceptInvitationManually: Boolean? = null,

    @SerialName("direct_route")
    val directRoute: Boolean? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null

)