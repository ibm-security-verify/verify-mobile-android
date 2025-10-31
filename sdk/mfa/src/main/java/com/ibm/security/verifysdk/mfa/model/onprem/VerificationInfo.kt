/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.model.onprem

import kotlinx.serialization.Serializable

@Serializable
data class VerificationInfo(
    val mechanism: String,
    val location: String,
    val type: String,
    var serverChallenge: String,
    val keyHandles: List<String>
)