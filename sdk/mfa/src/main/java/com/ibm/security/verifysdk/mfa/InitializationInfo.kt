/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class InitializationInfo(
    @SerialName("registrationUri")
    @Serializable(with = URLSerializer::class)
    val uri: URL,
    val code: String,
    val accountName: String
)