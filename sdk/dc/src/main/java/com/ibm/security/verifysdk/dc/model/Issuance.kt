/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Issuance(

    @SerialName("manifest")
    val manifest: String? = null

) : kotlin.collections.HashMap<String, Any>()
