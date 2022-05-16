/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core


fun String.toNumberOrNull(): Number? {

    return this.toIntOrNull() ?: this.toLongOrNull() ?: this.toFloatOrNull()
    ?: this.toDoubleOrNull()
}

fun String.toNumberOrDefault(default: Number): Number {

    return this.toIntOrNull() ?: this.toLongOrNull() ?: this.toFloatOrNull()
    ?: this.toDoubleOrNull() ?: default
}