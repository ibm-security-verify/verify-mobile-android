/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionAttribute(val rawValue: String) {
    IPAddress("ipAddress"),
    Location("location"),
    Image("image"),
    UserAgent("userAgent"),
    Type("type"),
    Custom("custom")
}