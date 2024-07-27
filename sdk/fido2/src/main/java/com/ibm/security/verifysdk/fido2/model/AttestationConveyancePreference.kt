/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import com.ibm.security.verifysdk.fido2.model.AttestationConveyancePreference.DIRECT
import com.ibm.security.verifysdk.fido2.model.AttestationConveyancePreference.INDIRECT
import com.ibm.security.verifysdk.fido2.model.AttestationConveyancePreference.NONE
import kotlinx.serialization.Serializable

/**
 * An enumeration representing attestation conveyance preferences according to the FIDO2 specification.
 * Attestation conveyance preference specifies how the authenticator conveys attestation during registration.
 *
 * This enumeration provides three options:
 * - [NONE]: Indicates that the authenticator will not convey attestation during registration.
 * - [INDIRECT]: Indicates that the authenticator will convey attestation indirectly via the FIDO server.
 * - [DIRECT]: Indicates that the authenticator will convey attestation directly to the relying party.
 * 
 * @property value The string value representing the attestation conveyance preference.
 */
@Serializable
enum class AttestationConveyancePreference(val value: String) {
    /**
     * Indicates that the authenticator will not convey attestation during registration.
     */
    NONE("none"),

    /**
     * Indicates that the authenticator will convey attestation indirectly via the FIDO server.
     */
    INDIRECT("indirect"),

    /**
     * Indicates that the authenticator will convey attestation directly to the relying party.
     */
    DIRECT("direct")
}