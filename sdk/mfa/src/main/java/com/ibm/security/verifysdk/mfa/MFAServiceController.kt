/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

class MFAServiceController(private val authenticator: MFAAuthenticatorDescriptor) {

    init {
        require(authenticator is OnPremiseAuthenticator || authenticator is CloudAuthenticator) {
            "Invalid authenticator type. Only OnPremiseAuthenticator or CloudAuthenticator is allowed."
        }
    }

    fun initiate(): MFAServiceDescriptor {

        if (authenticator is OnPremiseAuthenticator) {
            if (certificateTrust == null && authenticator.ignoreSSLCertificate) {
                certificateTrust = SelfSignedCertificateDelegate()
            }

            return OnPremiseAuthenticatorService(
                accessToken = authenticator.token.accessToken,
                refreshUri = authenticator.refreshUri,
                transactionUri = authenticator.transactionUri,
                clientId = authenticator.clientId,
                authenticatorId = authenticator.id,
                certificateTrust = certificateTrust
            )
        }

        return CloudAuthenticatorService(
            accessToken = authenticator.token.accessToken,
            refreshUri = authenticator.refreshUri,
            transactionUri = authenticator.transactionUri,
            authenticatorId = authenticator.id,
            certificateTrust = certificateTrust
        )
    }
}
