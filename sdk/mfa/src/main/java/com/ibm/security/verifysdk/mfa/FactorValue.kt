/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.util.UUID

sealed class FactorValue {
    abstract val id: UUID
    abstract val displayName: String

    data class Totp(override val id: UUID, override val displayName: String) : FactorValue()
    data class Hotp(override val id: UUID, override val displayName: String) : FactorValue()
    data class Face(override val id: UUID, override val displayName: String) : FactorValue()
    data class Fingerprint(override val id: UUID, override val displayName: String) : FactorValue()
    data class UserPresence(override val id: UUID, override val displayName: String) : FactorValue()
}