/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import java.net.URL

fun URL.replace(pattern: String, replacement: String) : URL {
    return URL(this.toString().replace(pattern, replacement))
}