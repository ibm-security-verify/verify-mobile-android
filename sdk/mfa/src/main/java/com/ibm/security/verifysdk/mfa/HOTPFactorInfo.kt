/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class HOTPFactorInfo(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
    override val displayName: String = "HMAC-based one-time password (HOTP)",
    override val secret: String,
    override val algorithm: HashAlgorithmType = HashAlgorithmType.SHA1,
    override val digits: Int = 6,
    private var _counter: Int = 1
) : Factor, OTPDescriptor {

    val counter: Int
        get() = _counter

    init {
        require(digits == 6 || digits == 8) { "Digits must be either 6 or 8." }
        require(_counter > 0) { "Counter must be greater than 0." }
    }

    fun generatePasscode(): String {
        val result = generatePasscode(counter.toULong())
        _counter++
        return result
    }
}