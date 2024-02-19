/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents the entity associated with a public key credential relying party (RP).
 *
 * @property id The identifier associated with the relying party. Can be null.
 * @property name The name of the relying party.
 * @property icon The icon URL of the relying party. Default is null.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicKeyCredentialRpEntity(
    var id: String?,
    val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val icon: String? = null
) {
    init {
        this.id ?: name
    }
}
