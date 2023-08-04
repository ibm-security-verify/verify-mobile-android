/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.NetworkHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import kotlin.io.encoding.Base64

interface MFAServiceDescriptor {
    val accessToken: String
    val refreshUri: URL
    val transactionUri: URL
    val currentPendingTransaction: PendingTransactionInfo?

    fun setCurrentPendingTransaction(value: PendingTransactionInfo?)

    suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?
    ): Result<TokenInfo>

    suspend fun nextTransaction(transactionID: String?): Result<NextTransactionInfo>

    suspend fun completeTransaction(userAction: UserAction, signedData: String): Result<Unit>
}

typealias NextTransactionInfo = Pair<PendingTransactionInfo?, Int>

suspend fun MFAServiceDescriptor.login(loginUri: URL, code: String): Result<Unit> {
    val body: RequestBody = "{\"lsi\":$code}".toRequestBody("text/plain".toMediaTypeOrNull())

    return try {
        NetworkHelper.handleApi(
            NetworkHelper.networkApi().login(
                loginUri.toString(),
                accessToken,
                body
            )
        )

    } catch (e: Throwable) {
        Result.failure(e)
    }
}

suspend fun MFAServiceDescriptor.completeTransaction(
    userAction: UserAction = UserAction.VERIFY,
    factor: FactorType
) {
    val pendingTransaction =
        currentPendingTransaction ?: throw MFAServiceError.InvalidPendingTransaction()
    val value = factorNameAndAlgorithm(factor)
        ?: throw MFAServiceError.General("Invalid factor to perform signing.")
    var signedData = ""

    // TODO: check whether this is correct - it does not seem to provide the right key name

    if (userAction == UserAction.VERIFY) {
        signedData = sign(
            keyName = value.first,
            algorithm = value.second.name,
            dataToSign = pendingTransaction.dataToSign
        )
    }

    completeTransaction(userAction = userAction, signedData = signedData)
}
