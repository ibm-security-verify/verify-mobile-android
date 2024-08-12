/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.model.cloud

import com.ibm.security.verifysdk.core.serializer.URLSerializer
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