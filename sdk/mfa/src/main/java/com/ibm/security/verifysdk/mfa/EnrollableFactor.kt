/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.net.URL

internal interface EnrollableFactor {
    val uri: URL
    val type: EnrollableType
}