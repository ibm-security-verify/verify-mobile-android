/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class FactorType {

    val id: UUID
        get() = when (this) {
            is Totp -> value.id
            is Hotp -> value.id
            is Face -> value.id
            is Fingerprint -> value.id
            is UserPresence -> value.id
        }

    val displayName: String
        get() = when (this) {
            is Totp -> value.displayName
            is Hotp -> value.displayName
            is Face -> value.displayName
            is Fingerprint -> value.displayName
            is UserPresence -> value.displayName
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