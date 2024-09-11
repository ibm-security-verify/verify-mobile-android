/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LdpObject(

    @SerialName("proof_type")
    val proofType: List<String>
)
