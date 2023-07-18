/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

interface MFAServiceDescriptor : Actor {
    val accessToken: String
    val refreshUri: URL
    val transactionUri: URL
    val currentPendingTransaction: PendingTransactionInfo?

    suspend fun refreshToken(refreshToken: String, accountName: String?, pushToken: String?, additionalData: Map<String, Any>?): TokenInfo

    suspend fun nextTransaction(transactionID: String?): NextTransactionInfo

    suspend fun completeTransaction(userAction: UserAction, signedData: String)
}
