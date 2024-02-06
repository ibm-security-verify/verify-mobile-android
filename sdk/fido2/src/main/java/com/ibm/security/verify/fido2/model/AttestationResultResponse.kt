/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
data class AttestationResultResponse(
    val error_code: String? = null,
    val error_message: String? = null,
    val status: String? = null
)