/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.mfa.cloud.CloudAuthenticatorService
import com.ibm.security.verifysdk.mfa.cloud.model.CloudAuthenticator
import com.ibm.security.verifysdk.mfa.onprem.OnPremiseAuthenticatorService
import com.ibm.security.verifysdk.mfa.onprem.model.OnPremiseAuthenticator

class MFAServiceController(private val authenticator: MFAAuthenticatorDescriptor) {

    init {
        require(authenticator is OnPremiseAuthenticator || authenticator is CloudAuthenticator) {
            "Invalid authenticator type. Only OnPremiseAuthenticator or CloudAuthenticator is allowed."
        }
    }

    fun initiate(): MFAServiceDescriptor {

        when (authenticator) {
            is OnPremiseAuthenticator -> return OnPremiseAuthenticatorService(
                _accessToken = authenticator.token.accessToken,
                _refreshUri = authenticator.refreshUri,
                _transactionUri = authenticator.transactionUri,
                _clientId = authenticator.clientId,
                _authenticatorId = authenticator.id,
                _ignoreSslCertificate = authenticator.ignoreSSLCertificate
            )

            is CloudAuthenticator -> return CloudAuthenticatorService(
                _accessToken = authenticator.token.accessToken,
                _refreshUri = authenticator.refreshUri,
                _transactionUri = authenticator.transactionUri,
                _authenticatorId = authenticator.id
            )

            else -> throw MFARegistrationError.InvalidFormat
        }
    }
}
