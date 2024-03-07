/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import com.ibm.security.verifysdk.core.KeystoreHelper.hash


fun String.toNumberOrNull(): Number? {

    return this.toIntOrNull() ?: this.toLongOrNull() ?: this.toDoubleOrNull()
}

fun String.toNumberOrDefault(default: Number): Number {

    return this.toIntOrNull() ?: this.toLongOrNull() ?: this.toDoubleOrNull() ?: default
}

fun String.sha256(): String {
    return hash(this, "SHA-256")
}