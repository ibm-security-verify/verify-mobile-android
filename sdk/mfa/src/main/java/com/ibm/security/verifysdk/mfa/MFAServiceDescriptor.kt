/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.NetworkHelper
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL

interface MFAServiceDescriptor {
    val authorizationHeader: String
    val refreshUri: URL
    val transactionUri: URL
    val currentPendingTransaction: PendingTransactionInfo?

    suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?
    ): Result<TokenInfo>

    suspend fun nextTransaction(transactionID: String? = null): Result<NextTransactionInfo>

    suspend fun completeTransaction(userAction: UserAction, signedData: String): Result<Unit>
}

typealias NextTransactionInfo = Pair<PendingTransactionInfo?, Int>

suspend fun MFAServiceDescriptor.login(loginUri: URL, code: String): Result<Unit> {
    val body: RequestBody = "{\"lsi\":$code}".toRequestBody("text/plain".toMediaTypeOrNull())

    val decoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    return try {
        val response = NetworkHelper.getInstance.post {
            url(loginUri.toString())
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            bearerAuth(authorizationHeader)
            setBody(body)
        }

        if (response.status.isSuccess()) {
            Result.success(decoder.decodeFromString<Unit>(response.bodyAsText()))
        } else {
            Result.failure(MFAServiceError.General(response.bodyAsText()))
        }
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
