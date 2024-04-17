/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

/**
 * Represents information about a Time-based One-Time Password (TOTP) factor.
 *
 * This data class provides details necessary for generating and managing TOTP-based authentication factors.
 *
 * @param id The unique identifier for the TOTP factor.
 * @param displayName The display name for the TOTP factor.
 * @param secret The secret key used for generating TOTP codes.
 * @param algorithm The hash algorithm used for generating TOTP codes.
 * @param digits The number of digits in the generated TOTP codes. Reducing the number of digits for
 *  *                  OTP validation below 6 presents a potential security risk.
 * @param period The time period, in seconds, for which TOTP codes are valid.
 */
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
        require(period in 10..300) { "Period must be between 10 and 300 (inclusive)." }
        require(digits > 0) { "Digits must be > 0" }

        // The request to support digits < 6 was submitted by a customer (see CI-145331).
        require(digits <= 6 || digits == 8) { " Digits must be 1, 2, 3, 4, 5, 6 or 8"}
    }

    /**
     * Generates a TOTP passcode based on the current time interval.
     *
     * @return The generated TOTP passcode.
     */
    @SuppressWarnings
    fun generatePasscode(): String {
        return generatePasscode(Date().time / 1000L)
    }

    @SuppressWarnings
    override fun generatePasscode(time: Long): String {
        return super.generatePasscode(time / period / 1000L)
    }
}