/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class CreateConnectionArgs (

    @SerialName("direct_route")
    val directRoute: Boolean? = null,

    @SerialName("to")
    val to: ToAgent? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null,

    @SerialName("url")
    val url: String? = null,

    @SerialName("no_did_exchange_response")
    val noDidExchangeResponse: Boolean? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("state")
    val state: String? = null,

    @SerialName("role")
    val role: String? = null,

    @SerialName("local")
    val local: Endpoint? = null,

    @SerialName("did_exchange")
    val didExchange: Boolean? = null,

    @SerialName("invitation")
    val invitation: InvitationInfo? = null,

    @SerialName("remote")
    val remote: Endpoint? = null,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null,

    @SerialName("ext_complete")
    val extComplete: Boolean? = null,

    @SerialName("max_queue_count")
    val maxQueueCount: Int? = null,

    @SerialName("max_queue_time_ms")
    val maxQueueTimeMs: Int? = null,

    @SerialName("~thread")
    val tildeThread: String? = null
)