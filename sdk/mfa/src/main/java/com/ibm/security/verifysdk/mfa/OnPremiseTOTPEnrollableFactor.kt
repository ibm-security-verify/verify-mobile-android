/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.net.URL

internal data class OnPremiseTOTPEnrollableFactor(
    override val uri: URL,
    override val type: EnrollableType = EnrollableType.TOTP
) : EnrollableFactor