/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * An enum class representing the preference for attestation conveyance during a WebAuthn operation.
 *
 * @property value The string value representing the attestation conveyance preference.
 */
@Serializable
enum class AttestationConveyancePreference(val value: String) {
    NONE("none"),
    INDIRECT("indirect"),
    DIRECT("direct")
}