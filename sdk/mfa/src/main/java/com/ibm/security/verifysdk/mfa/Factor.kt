/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */
package com.ibm.security.verifysdk.mfa

import java.util.UUID

interface Factor {
    val id : UUID
    val displayName : String
}