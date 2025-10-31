/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.net.URL

internal data class SignatureEnrollableFactor(
    override val uri: URL,
    override val type: EnrollableType,
    val algorithm: String
) : EnrollableFactor
