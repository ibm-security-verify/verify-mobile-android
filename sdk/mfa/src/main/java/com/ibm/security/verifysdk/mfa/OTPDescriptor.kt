/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.util.Date
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface OTPDescriptor {
    val secret: String
    val digits: Int
    val algorithm: HashAlgorithmType

    companion object {
        fun remainingTime(seconds: Double = 30.0): Int {
            val currentTimeRemaining = (Date().time / 1000 % seconds).toInt()
            return seconds.toInt() - currentTimeRemaining
        }
    }
}

fun OTPDescriptor.generatePasscode(from: ULong): String {

    val digitsPower = intArrayOf(1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000)

    fun computeAuthenticationCode(hashAlgorithm: HashAlgorithmType): String {
        val mac: Mac = Mac.getInstance(algorithm.toString())
        var macKey: SecretKeySpec = SecretKeySpec(
            (secret.replace(" ".toRegex(), "")).toByteArray(),
            algorithm.toString()
        )
        mac.init(macKey)
        val hmacResult: ByteArray = mac.doFinal(from.toLong().toByteArray())
        val offset: Int = hmacResult[hmacResult.size - 1].toInt() and 0x0f
        var binary: Int = hmacResult[offset].toInt() and 0x7f shl 24
        binary = binary or (hmacResult[offset + 1].toInt() and 0xff shl 16)
        binary = binary or (hmacResult[offset + 2].toInt() and 0xff shl 8)
        binary = binary or (hmacResult[offset + 3].toInt() and 0xff)

        return String.format("%0" + digits + "d", (binary % digitsPower[digits]))
    }

    return when (algorithm) {
        HashAlgorithmType.SHA1 -> computeAuthenticationCode(HashAlgorithmType.SHA1)
        HashAlgorithmType.SHA256 -> computeAuthenticationCode(HashAlgorithmType.SHA256)
        HashAlgorithmType.SHA384 -> computeAuthenticationCode(HashAlgorithmType.SHA384)
        HashAlgorithmType.SHA512 -> computeAuthenticationCode(HashAlgorithmType.SHA512)
    }
}