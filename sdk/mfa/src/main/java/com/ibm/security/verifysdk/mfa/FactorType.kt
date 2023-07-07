/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.util.UUID

sealed class FactorType {

    abstract val value: FactorValue

    val id: UUID
        get() = when (this) {
            is FactorType.Totp -> value.id
            is FactorType.Hotp -> value.id
            is FactorType.Face -> value.id
            is FactorType.Fingerprint -> value.id
            is FactorType.UserPresence -> value.id
        }

    val displayName: String
        get() = when (this) {
            is FactorType.Totp -> value.displayName
            is FactorType.Hotp -> value.displayName
            is FactorType.Face -> value.displayName
            is FactorType.Fingerprint -> value.displayName
            is FactorType.UserPresence -> value.displayName
        }

    data class Totp(override val value: FactorValue.Totp) : FactorType()
    data class Hotp(override val value: FactorValue.Hotp) : FactorType()
    data class Face(override val value: FactorValue.Face) : FactorType()
    data class Fingerprint(override val value: FactorValue.Fingerprint) : FactorType()
    data class UserPresence(override val value: FactorValue.UserPresence) : FactorType()

}