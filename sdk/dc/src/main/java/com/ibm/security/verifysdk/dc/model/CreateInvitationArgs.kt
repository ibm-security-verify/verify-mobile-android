/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class CreateInvitationArgs(

    @SerialName("goal_code")
    val goalCode: String? = null,

    @SerialName("goal")
    val goal: String? = null,

    @SerialName("label")
    val label: String? = null,

    @SerialName("attach")
    val attach: OutOfBandInvitationAttachArgs? = null,

    @SerialName("type")
    val type: InvitationType? = null,

    @SerialName("direct_route")
    val directRoute: Boolean? = null,

    @SerialName("manual_accept")
    val manualAccept: Boolean? = null,

    @SerialName("max_acceptances")
    val maxAcceptances: Double? = null,

    @SerialName("max_connections")
    val maxConnections: Double? = null,

    @SerialName("max_queue_count")
    val maxQueueCount: Double? = null,

    @SerialName("max_queue_time_ms")
    val maxQueueTimeMs: Double? = null,

    @SerialName("invitation_lifetime_ms")
    val invitationLifetimeMs: Double? = null,

    @SerialName("connection_lifetime_ms")
    val connectionLifetimeMs: Double? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null
)
