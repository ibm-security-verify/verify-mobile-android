/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
enum class HashAlgorithmType(private val rawValue: String) {
    SHA1("SHA1"),
    SHA256("SHA256"),
    SHA384("SHA384"),
    SHA512("SHA512");

    override fun toString(): String {
        return rawValue
    }

    companion object {
        /**
         * Accepts "SHAx" and "HmacSHAx" (and other variations). The latter is required by Java itself
         * (https://docs.oracle.com/javase/7/docs/api/javax/crypto/Mac.html), whereas the first one
         * support Google's Key Uri Format
         * (https://github.com/google/google-authenticator/wiki/Key-Uri-Format#algorithm).
         */
        fun fromString(rawValue: String): HashAlgorithmType? {
            return when (rawValue.uppercase(Locale.ROOT)) {
                "SHA1", "HMACSHA1", "RSASHA1", "SHA1WITHRSA" -> SHA1
                "SHA256", "HMACSHA256", "RSASHA256", "SHA256WITHRSA" -> SHA256
                "SHA384", "HMACSHA384", "RSASHA384", "SHA384WITHRSA" -> SHA384
                "SHA512", "HMACSHA512", "RSASHA512", "SHA512WITHRSA" -> SHA512
                else -> null
            }
        }
    }
}