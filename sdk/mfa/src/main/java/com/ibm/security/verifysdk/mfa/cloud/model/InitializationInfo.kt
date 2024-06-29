/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud.model

import com.ibm.security.verifysdk.mfa.URLSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
internal data class InitializationInfo(
    @SerialName("registrationUri")
    @Serializable(with = URLSerializer::class)
    val uri: URL,
    val code: String,
    val accountName: String
)