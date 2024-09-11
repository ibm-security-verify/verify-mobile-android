/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DifPresentationOptions(

    @SerialName("challenge")
    val challenge: String? = null,

    @SerialName("domain")
    val domain: String? = null
)
