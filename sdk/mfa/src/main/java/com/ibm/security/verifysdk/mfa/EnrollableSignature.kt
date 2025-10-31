/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable

// Different to iOS, because the app needs to know the EnrollableType to decide whether to skip a certain enrollment.
@Serializable
data class EnrollableSignature(
    val biometricAuthentication: Boolean,
    val algorithmType: HashAlgorithmType,
    val authenticatorId: String,
    val enrollableType: EnrollableType
)