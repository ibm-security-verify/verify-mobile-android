/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FaceFactorInfo(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
    override val displayName: String = "Face ID",
    val keyName: String = "face",
    val algorithm: HashAlgorithmType = HashAlgorithmType.SHA1,
) : Factor