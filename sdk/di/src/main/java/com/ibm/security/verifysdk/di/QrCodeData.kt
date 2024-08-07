/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.di

import com.ibm.security.verifysdk.core.serializer.URLSerializer
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class QrCodeData(
    val name          : String? = null,
    val id            : String? = null,
    @Serializable(with = URLSerializer::class)
    val url           : URL? = null,
    val clientId      : String? = null,
    @Serializable(with = URLSerializer::class)
    val tokenEndpoint : URL? = null
)
