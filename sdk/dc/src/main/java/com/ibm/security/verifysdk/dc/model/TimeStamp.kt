/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeStamp(

    @SerialName("created")
    val created: Long,
    @SerialName("stated")
    val stated: Long? = null,
    @SerialName("updated")
    val updated: Long? = null,
    @SerialName("deleted")
    val deleted: Long? = null
)