/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ConnectionInfo (

    @SerialName("id")
    val id: String,

    @SerialName("state")
    val state: String,

    @SerialName("role")
    val role: ConnectionRole,

    @SerialName("local")
    val local: Endpoint,

    @SerialName("did_exchange")
    val didExchange: Boolean,

    @SerialName("invitation")
    val invitation: InvitationInfo? = null,

    @SerialName("remote")
    val remote: Endpoint? = null,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null,

    @SerialName("ext_complete")
    val extComplete: Boolean? = null,

    @SerialName("max_queue_count")
    val maxQueueCount: Double? = null,

    @SerialName("max_queue_time_ms")
    val maxQueueTimeMs: Double? = null

)
