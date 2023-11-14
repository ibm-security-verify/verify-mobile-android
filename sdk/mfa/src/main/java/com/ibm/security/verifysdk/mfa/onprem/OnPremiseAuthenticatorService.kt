/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.mfa.MFAServiceDescriptor
import com.ibm.security.verifysdk.mfa.NextTransactionInfo
import com.ibm.security.verifysdk.mfa.PendingTransactionInfo
import com.ibm.security.verifysdk.mfa.UserAction
import java.net.URL

class OnPremiseAuthenticatorService(
    override var accessToken: String,
    override var refreshUri: URL,
    override var transactionUri: URL,
    override var currentPendingTransaction: PendingTransactionInfo? = null,
    val clientId: String,
    val authenticatorId: String
) : MFAServiceDescriptor {

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