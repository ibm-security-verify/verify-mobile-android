/*
 *  Copyright contributors to the IBM Security Verify FIDO2 Sample App for Android project
 */

package com.ibm.security.verifysdk.fido2.demoapp.model

import kotlinx.serialization.Serializable

@Serializable
data class IvCreds(
    val name: String = "ivcreds-name",
    val username: String = "ivcreds-username",
    val email: String = "ivcreds-email",
    val AZN_CRED_PRINCIPAL_NAME: String
)
