/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
@file:UseSerializers(URLSerializer::class)
package com.ibm.security.verifysdk.mfa.model.cloud

import com.ibm.security.verifysdk.core.serializer.URLSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

@Serializable
internal data class InitializationInfo(
    @SerialName("registrationUri")
    val uri: URL,
    val code: String,
    val accountName: String
)