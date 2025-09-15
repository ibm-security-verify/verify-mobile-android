/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class AgentQrCodeData(
    val name: String,
    val id: String,
    val serviceBaseUrl: String,
    val clientId: String,
    val aznCode: String,
    val oauthBaseUrl: String
)