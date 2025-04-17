/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.model

import com.ibm.security.verifysdk.dc.model.VerificationAction.GENERATE
import com.ibm.security.verifysdk.dc.model.VerificationAction.REJECT
import com.ibm.security.verifysdk.dc.model.VerificationAction.SHARE
import kotlinx.serialization.Serializable

/**
 * Enum class representing the different actions that can be performed during a verification process.
 *
 * This enum defines three possible actions:
 * - [GENERATE] - Generate the credential proof for verification.
 * - [SHARE] - Share the credential proof for verification.
 * - [REJECT] - The request to prove a credential has been rejected.
 *
 * @since 3.0.7
 */
@Serializable
enum class VerificationAction {
    GENERATE,
    SHARE,
    REJECT;
}
