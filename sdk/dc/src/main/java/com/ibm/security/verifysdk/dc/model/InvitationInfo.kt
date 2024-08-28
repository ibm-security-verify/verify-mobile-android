/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class InvitationInfo(

    @SerialName("id")
    val id: String,

    @SerialName("url")
    val url: String,

    @SerialName("short_url")
    val shortUrl: String,

    @SerialName("direct_route")
    val directRoute: Boolean,

    @SerialName("manual_accept")
    val manualAccept: Boolean,

    @SerialName("max_acceptances")
    val maxAcceptances: Double,

    @SerialName("cur_acceptances")
    val curAcceptances: Double,

    @SerialName("recipient_key")
    val recipientKey: String,

    @SerialName("max_connections")
    val maxConnections: Double? = null,

    @SerialName("max_queue_count")
    val maxQueueCount: Double? = null,

    @SerialName("max_queue_time_ms")
    val maxQueueTimeMs: Double? = null,

    @SerialName("connection_lifetime_ms")
    val connectionLifetimeMs: Double? = null,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null,

    @SerialName("attachments")
    val attachments: List<JsonElement>? = null,

    @SerialName("attach_args")
    val attachArgs: OutOfBandInvitationAttachArgs? = null
)

typealias TimeStampsValue = JsonElement

