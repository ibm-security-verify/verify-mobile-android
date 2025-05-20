/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ConnectionInfo (

    @SerialName("id")
    val id: String,

    @SerialName("state")
    val state: ConnectionState,

    @SerialName("role")
    val role: ConnectionRole,

    @SerialName("local")
    val local: ConnectionAgentInfo,

    @SerialName("invitation")
    val invitation: InvitationInfo? = null,

    @SerialName("remote")
    val remote: ConnectionAgentInfo,
)