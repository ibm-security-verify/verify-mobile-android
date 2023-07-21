/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.authentication.TokenInfo
import java.net.URL

class CloudAuthenticatorService(override var accessToken: String,
                                override var refreshUri:URL,
                                override var transactionUri: URL,
                                val authenticatorId: String) : MFAServiceDescriptor {

    override var currentPendingTransaction: PendingTransactionInfo?
        get() = TODO("Not yet implemented")
        set(value) {}

    override suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?
    ): Result<TokenInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun nextTransaction(transactionID: String?): Result<NextTransactionInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun completeTransaction(
        userAction: UserAction,
        signedData: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}