/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class TOTPFactorInfo(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
    override val displayName: String = "Time-based one-time password (TOTP)",
    override val secret: String,
    override val algorithm: HashAlgorithmType = HashAlgorithmType.SHA1,
    override val digits: Int = 6,
    val period: Int = 30
) : Factor, OTPDescriptor {

    init {
        require(digits == 6 || digits == 8) { "Digits must be either 6 or 8." }
        require(period in 10..300) { "Period must be between 10 and 300 (inclusive)." }
    }

    fun generatePasscode(): String {
        val timeInterval = Date().time / 1000
        val value = (timeInterval / period).toULong()
        return generatePasscode(value)
    }
}