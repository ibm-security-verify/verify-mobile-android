/*
 *  Copyright contributors to the IBM Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import com.ibm.security.verifysdk.fido2.model.UserVerificationRequirement.DISCOURAGED
import com.ibm.security.verifysdk.fido2.model.UserVerificationRequirement.PREFERRED
import com.ibm.security.verifysdk.fido2.model.UserVerificationRequirement.REQUIRED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An enumeration representing user verification requirements according to the WebAuthn specification.
 * User verification requirement specifies the preferred level of user verification for authenticating a user.
 *
 * This enumeration provides three options:
 * - [REQUIRED]: Specifies that user verification is required for authentication.
 * - [PREFERRED]: Specifies a preference for user verification, but it is not required.
 * - [DISCOURAGED]: Specifies that user verification is discouraged for authentication.
 *
 * @property value The string value representing the user verification requirement.
 */
@Serializable
enum class UserVerificationRequirement(val value: String) {
    /**
     * Specifies that user verification is required for authentication.
     */
    @SerialName("required")
    REQUIRED("required"),

    /**
     * Specifies a preference for user verification, but it is not required.
     */
    @SerialName("preferred")
    PREFERRED("preferred"),

    /**
     * Specifies that user verification is discouraged for authentication.
     */
    @SerialName("discouraged")
    DISCOURAGED("discouraged")
}
