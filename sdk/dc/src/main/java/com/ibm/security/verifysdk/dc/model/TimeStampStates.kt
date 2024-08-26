/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TimeStampStates(

    @SerialName("connected")
    val connected: Int,
    @SerialName("deleted")
    val deleted: Int?,
    @SerialName("updated")
    val updated: Int?,
    @SerialName("did_exchange_response_sent")
    val didExchangeResponseSent: Int?,
    @SerialName("inbound_offer")
    val inboundOffer: Int?
)