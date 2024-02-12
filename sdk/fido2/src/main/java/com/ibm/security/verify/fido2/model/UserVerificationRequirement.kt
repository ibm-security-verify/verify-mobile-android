/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * An enum class representing the user verification requirement during a WebAuthn operation.
 *
 * @property value The string value representing the user verification requirement.
 */
@Serializable
enum class UserVerificationRequirement(val value: String) {
    REQUIRED("required"),
    PREFERRED("preferred"),
    DISCOURAGED("discouraged")
}
