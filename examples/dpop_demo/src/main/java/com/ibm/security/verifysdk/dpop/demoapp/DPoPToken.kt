/*
 *  Copyright contributors to the IBM Security Verify DPoP Sample App for Android project
 */
package com.ibm.security.verifysdk.dpop.demoapp

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DpopToken @OptIn(ExperimentalSerializationApi::class) constructor(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("grant_id")
    val grantId: String,
    val scope: String,
    @SerialName("token_type")
    val tokenType: String
)
