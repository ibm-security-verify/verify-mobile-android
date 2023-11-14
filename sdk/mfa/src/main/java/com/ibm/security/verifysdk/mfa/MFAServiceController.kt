/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.mfa.cloud.CloudAuthenticator
import com.ibm.security.verifysdk.mfa.cloud.CloudAuthenticatorService
import com.ibm.security.verifysdk.mfa.onprem.OnPremiseAuthenticator
import com.ibm.security.verifysdk.mfa.onprem.OnPremiseAuthenticatorService

class MFAServiceController(private val authenticator: MFAAuthenticatorDescriptor) {

    init {
        require(authenticator is OnPremiseAuthenticator || authenticator is CloudAuthenticator) {
            "Invalid authenticator type. Only OnPremiseAuthenticator or CloudAuthenticator is allowed."
        }
    }

    fun initiate(): MFAServiceDescriptor {

        when (authenticator) {
            is OnPremiseAuthenticator -> return OnPremiseAuthenticatorService(
                accessToken = authenticator.token.accessToken,
                refreshUri = authenticator.refreshUri,
                transactionUri = authenticator.transactionUri,
                clientId = authenticator.clientId,
                authenticatorId = authenticator.id
            )

            is CloudAuthenticator -> return CloudAuthenticatorService(
                _accessToken = authenticator.token.accessToken,
                _refreshUri = authenticator.refreshUri,
                _transactionUri = authenticator.transactionUri,
                authenticatorId = authenticator.id
            )

            else -> throw MFARegistrationError.InvalidFormat
        }
    }
}
