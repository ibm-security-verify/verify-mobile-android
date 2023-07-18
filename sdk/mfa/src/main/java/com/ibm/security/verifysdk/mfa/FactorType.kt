/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class FactorType {

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

    data class Totp(val value: TOTPFactorInfo) : FactorType()
    data class Hotp(val value: HOTPFactorInfo) : FactorType()
    data class Face(val value: FaceFactorInfo) : FactorType()
    data class Fingerprint(val value: FingerprintFactorInfo) : FactorType()
    data class UserPresence(val value: UserPresenceFactorInfo) : FactorType()

}

fun FactorType.valueType(): Factor {
    return when (this) {
        is FactorType.Totp -> this.value
        is FactorType.Hotp -> this.value
        is FactorType.Face -> this.value
        is FactorType.Fingerprint -> this.value
        is FactorType.UserPresence -> this.value
    }
}