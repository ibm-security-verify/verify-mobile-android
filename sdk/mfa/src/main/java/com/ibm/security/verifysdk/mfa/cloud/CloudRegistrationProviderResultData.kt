/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud

import com.ibm.security.verifysdk.authentication.TokenInfo

internal data class CloudRegistrationProviderResultData(
    val tokenInfo: TokenInfo,
    val metadata: Metadata
)
