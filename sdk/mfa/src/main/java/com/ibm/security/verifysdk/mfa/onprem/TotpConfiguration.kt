/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem

import kotlinx.serialization.Serializable

@Serializable
internal data class TotpConfiguration(
    val period: String,
    val secretKeyUrl: String,
    val secretKey: String,
    val digits: String,
    val username: String,
    val algorithm: String
)
