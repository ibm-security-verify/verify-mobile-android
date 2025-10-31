/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

enum class UserAction(val value: String) {
    DENY("USER_DENIED"),
    MARK_AS_FRAUD("USER_FRAUDULENT"),
    VERIFY("VERIFY_ATTEMPT"),
    BIOMETRY_FAILED("BIOMETRY_FAILED")
}
