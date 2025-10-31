/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.extension

import android.net.Uri
import java.net.URL

fun URL.replace(pattern: String, replacement: String): URL {
    return URL(this.toString().replace(pattern, replacement))
}

fun URL.baseUrl(): URL {
    try {
        val uriBuilder = Uri.Builder()
        uriBuilder.scheme((this.protocol))
            .encodedAuthority(this.authority)
        return URL(uriBuilder.build().toString())
    } catch (_: Exception) {
        return this
    }
}