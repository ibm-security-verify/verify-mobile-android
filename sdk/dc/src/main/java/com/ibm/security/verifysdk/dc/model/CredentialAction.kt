/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName

/**
 * The action to use when processing a credential offer.
 *
 * @property value The string representation of the action, as defined in the API specification.
 * @since 3.0.4
 */
enum class CredentialAction(val value: String) {
    @SerialName("accepted")
    ACCEPTED("accepted"),

    @SerialName("rejected")
    REJECTED("rejected"),
}