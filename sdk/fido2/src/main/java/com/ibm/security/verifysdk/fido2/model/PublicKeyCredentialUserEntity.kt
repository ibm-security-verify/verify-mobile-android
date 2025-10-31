/*
 *  Copyright contributors to the IBM Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents the entity associated with a public key credential user.
 *
 * @property id The identifier associated with the user.
 * @property displayName The display name of the user.
 * @property name The name of the user.
 * @property icon The icon URL of the user. Defaults to null.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicKeyCredentialUserEntity(
    val id: String,
    val displayName: String,
    val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val icon: String? = null
)
