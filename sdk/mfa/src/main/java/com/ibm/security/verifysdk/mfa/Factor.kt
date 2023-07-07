/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.UUID

interface Factor {
    val id : UUID
    val displayName : String
}