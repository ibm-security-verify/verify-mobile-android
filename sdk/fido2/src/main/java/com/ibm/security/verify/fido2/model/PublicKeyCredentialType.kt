/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * An enum class representing the type of a public key credential.
 *
 * @property value The string value representing the public key credential type.
 */
@Serializable
enum class PublicKeyCredentialType(val value: String) {
    @OptIn(ExperimentalSerializationApi::class)
    @JsonNames("public-key")
    @SerialName("public-key")
    PUBLIC_KEY("public-key")
}
