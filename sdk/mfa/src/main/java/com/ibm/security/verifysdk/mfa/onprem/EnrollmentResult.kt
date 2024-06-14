/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnrollmentResult(
    val totalResults: Int,
    val schemas: List<String>,
    @SerialName("Resources")
    val resources: List<Resources>
)

@Serializable
data class Resources(
    val meta: Meta,
    val id: String,
    val userName: String,
    @SerialName("urn:ietf:params:scim:schemas:extension:isam:1.0:MMFA:Authenticator")
    val authenticator: Authenticator
)

@Serializable
data class Meta(
    val location: String,
    val resourceType: String
)

@Serializable
data class Authenticator(
    val userPresenceMethods: List<UserPresenceMethod>? = null,
    val fingerprintMethods: List<FingerprintMethod>? = null
)

@Serializable
data class UserPresenceMethod(
    val id: String,
    val keyHandle: String,
    val authenticator: String,
    val enabled: Boolean,
    val algorithm: String
)

@Serializable
data class FingerprintMethod(
    val id: String,
    val keyHandle: String,
    val authenticator: String,
    val enabled: Boolean,
    val algorithm: String
)



