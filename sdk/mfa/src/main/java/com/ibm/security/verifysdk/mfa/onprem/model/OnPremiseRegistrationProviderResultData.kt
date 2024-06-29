/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem.model

import com.ibm.security.verifysdk.authentication.TokenInfo

internal data class OnPremiseRegistrationProviderResultData(
    val tokenInfo: TokenInfo,
    val metadata: Metadata
)
