/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FingerprintFactorInfo(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
    override val displayName: String = "Fingerprint ID",
    val keyName: String = "fingerprint",
    val algorithm: HashAlgorithmType = HashAlgorithmType.SHA1,
) : Factor