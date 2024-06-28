/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem.model

import kotlinx.serialization.Serializable

@Serializable
data class VerificationInfo(
    val mechanism: String,
    val location: String,
    val type: String,
    var serverChallenge: String,
    val keyHandles: List<String>
)