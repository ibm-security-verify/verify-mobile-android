/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TimeStamp(

    @SerialName("created")
    val created: Int,
    @SerialName("stated")
    val states: TimeStampStates?,
    @SerialName("updated")
    val updated: Int?,
    @SerialName("deleted")
    val deleted: Int?
)