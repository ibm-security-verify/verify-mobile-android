/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.AuthorizationException
import com.ibm.security.verifysdk.core.ErrorResponse
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.mfa.MFAAttributeInfo
import com.ibm.security.verifysdk.mfa.MFAServiceDescriptor
import com.ibm.security.verifysdk.mfa.MFAServiceError
import com.ibm.security.verifysdk.mfa.NextTransactionInfo
import com.ibm.security.verifysdk.mfa.PendingTransactionInfo
import com.ibm.security.verifysdk.mfa.TransactionAttribute
import com.ibm.security.verifysdk.mfa.UserAction
import com.ibm.security.verifysdk.mfa.cloud.model.TransactionResult
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalSerializationApi::class)
class CloudAuthenticatorService(
    private var _accessToken: String,
    private var _refreshUri: URL,
    private var _transactionUri: URL,
    private var _authenticatorId: String
) : MFAServiceDescriptor {

    private val decoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private var _currentPendingTransaction: PendingTransactionInfo? = null
    private var transactionResult: TransactionResult? = null

    override val accessToken: String
        get() = _accessToken

    override val refreshUri: URL
        get() = _refreshUri

    override val transactionUri: URL
        get() = _transactionUri

    override val currentPendingTransaction: PendingTransactionInfo?
        get() = _currentPendingTransaction

    override val authenticatorId: String
        get() = _authenticatorId

    internal enum class TransactionFilter(val value: String) {
        NEXT_PENDING("?filter=id,creationTime,transactionData,authenticationMethods&search=state=%22PENDING%22&sort=-creationTime"),
        PENDING_BY_IDENTIFIER("?filter=id,creationTime,transactionData,authenticationMethods&search=state=\\u{22}PENDING\\u{22}&id=\\u{22}%@\\u{22}")
    }

    /**
     * Refreshes the token by making a network request to the refresh URI.
     *
     * @param refreshToken The refresh token to be used.
     * @param accountName Optional account name to be included in the request attributes.
     * @param pushToken Optional push token to be included in the request attributes.
     * @param additionalData Additional data to be included in the request (currently unused).
     * @return A [Result] containing the new [TokenInfo] if the request is successful, or an error if it fails.
     */
    override suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?
    ): Result<TokenInfo> {

        return try {
            val attributes = refreshTokenPrepareAttributes(accountName, pushToken)
            val data = refreshTokenPrepareRequestData(refreshToken, attributes)
            val response = refreshTokenMakeNetworkRequest(data)

            refreshTokenHandleResponse(response)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * Prepares the attributes map to be included in the request.
     *
     * @param accountName Optional account name to be included in the attributes.
     * @param pushToken Optional push token to be included in the attributes.
     * @return A mutable map of attributes with the provided account name and push token.
     */
    internal fun refreshTokenPrepareAttributes(
        accountName: String?,
        pushToken: String?
    ): MutableMap<String, Any> {
        return mutableMapOf<String, Any>().apply {
            putAll(MFAAttributeInfo.dictionary())
            remove("applicationName")
            accountName?.let { this["accountName"] = it }
            pushToken?.let { this["pushToken"] = it }
        }
    }

    /**
     * Makes a network request to refresh the token.
     *
     * @param data The request data to be sent.
     * @return The HTTP response from the server.
     */
    private suspend fun refreshTokenMakeNetworkRequest(data: Map<String, Any>): HttpResponse {
        return NetworkHelper.getInstance.post {
            url(refreshUri.toString())
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            parameter("metadataInResponse", true)
            setBody(data)
        }
    }

    /**
     * Handles the response from the token refresh request.
     *
     * @param response The HTTP response from the server.
     * @return A [Result] containing the new [TokenInfo] if the response indicates success, or an error if it fails.
     */
    private suspend fun refreshTokenHandleResponse(response: HttpResponse): Result<TokenInfo> {
        return if (response.status.isSuccess()) {
            Result.success(decoder.decodeFromString<TokenInfo>(response.bodyAsText()))
        } else {
            val errorResponse = response.body<ErrorResponse>()
            Result.failure(
                AuthorizationException(
                    response.status,
                    errorResponse.error,
                    errorResponse.errorDescription
                )
            )
        }
    }

    /**
     * Prepares the request data to be sent in the network request.
     *
     * @param refreshToken The refresh token to be included in the request.
     * @param attributes The attributes to be included in the request.
     * @return A map containing the refresh token and attributes.
     */
    private fun refreshTokenPrepareRequestData(
        refreshToken: String,
        attributes: Map<String, Any>
    ): Map<String, Any> {
        return mapOf(
            "refreshToken" to refreshToken,
            "attributes" to attributes
        )
    }

    /**
     * Retrieves the next pending transaction from the server.
     *
     * @param transactionID The ID of the transaction to retrieve. If null, retrieves the next pending transaction.
     * @return A [Result] containing the [NextTransactionInfo] if successful, or an error if unsuccessful.
     */
    override suspend fun nextTransaction(transactionID: String?): Result<NextTransactionInfo> {
        return try {
            val uri = nextTransactionBuildTransactionUri(transactionID)
            val response = nextTransactionMakeNetworkRequest(uri)

            nextTransactionHandleResponse(response)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * Builds the URI for retrieving the next pending transaction.
     *
     * @param transactionID The ID of the transaction to retrieve. If null, retrieves the next pending transaction.
     * @return The URI for retrieving the next pending transaction.
     */
    private fun nextTransactionBuildTransactionUri(transactionID: String?): URL {
        return if (transactionID != null) {
            URL("${transactionUri}${TransactionFilter.PENDING_BY_IDENTIFIER.value}/${transactionID}")
        } else {
            URL("${transactionUri}${TransactionFilter.NEXT_PENDING.value}")
        }
    }

    /**
     * Makes a network request using the given [uri] and returns the response.
     *
     * @param uri The URI to make the request to.
     * @return The HTTP response.
     */
    private suspend fun nextTransactionMakeNetworkRequest(uri: URL): HttpResponse {
        return NetworkHelper.getInstance.get {
            url(uri.toString())
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            bearerAuth(accessToken)
        }
    }

    /**
     * Handles the response from the server after making a request to retrieve the next pending transaction.
     *
     * @param response The HTTP response from the server.
     * @return A [Result] containing the [NextTransactionInfo] if successful, or an error if unsuccessful.
     */
    private suspend fun nextTransactionHandleResponse(response: HttpResponse): Result<NextTransactionInfo> {
        return if (response.status.isSuccess()) {
            parsePendingTransaction(response)
                .fold(
                    onSuccess = {
                        Result.success(it)
                    },
                    onFailure = {
                        Result.failure(it)
                    })
        } else {
            Result.failure(MFAServiceError.General(response.bodyAsText()))
        }
    }

    override suspend fun completeTransaction(
        userAction: UserAction,
        signedData: String
    ): Result<Unit> {

        return try {
            val pendingTransaction =
                currentPendingTransaction ?: throw MFAServiceError.InvalidPendingTransaction()

            val data = arrayOf(
                mapOf(
                    "id" to pendingTransaction.factorID.toString().lowercase(Locale.ROOT),
                    "userAction" to userAction.value,
                    "signedData" to (if (userAction == UserAction.VERIFY) signedData else null)
                )
            )

            val response = NetworkHelper.getInstance.post {
                url(pendingTransaction.postbackUri.toString())
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                bearerAuth(accessToken)
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(MFAServiceError.General(response.bodyAsText()))
            }
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    private suspend fun parsePendingTransaction(response: HttpResponse): Result<NextTransactionInfo> {

        return try {
            val responseBody = response.bodyAsText()
            val result: TransactionResult = try {
                decoder.decodeFromString(responseBody)
            } catch (e: Exception) {
                return Result.failure(MFAServiceError.DecodingFailed())
            }

            if (result.count == 0) {
                Result.success(NextTransactionInfo(null, 0))
            } else {
                createPendingTransaction(result)?.let {
                    Result.success(NextTransactionInfo(it, result.count))
                } ?: kotlin.run {
                    Result.failure(MFAServiceError.UnableToCreateTransaction())
                }
            }

        } catch (e: Throwable) {
            Result.failure(MFAServiceError.UnableToCreateTransaction())
        }
    }

    private fun createPendingTransaction(result: TransactionResult): PendingTransactionInfo? {
        // 1. Get the first transaction.
        val verificationInfo = result.verifications?.first() ?: return null

        // 2. Get the postback to the transaction.
        val postbackUri = URL(transactionUri, "verifications/${verificationInfo.id}")

        // 3. Get the message to display.
        val message = transactionMessage(verificationInfo.transactionInfo)

        // 4. Construct the factor that is used to look up the private key from the Keychain.
        val methodInfo = verificationInfo.methodInfo.firstOrNull() ?: return null

        // 5. Construct the transaction context information into additional data.
        val additionalData = createAdditionalData(verificationInfo.transactionInfo)

        return PendingTransactionInfo(
            id = verificationInfo.id,
            message = message,
            postbackUri = postbackUri,
            factorID = UUID.fromString(methodInfo.id) ?: UUID.randomUUID(),
            factorType = methodInfo.subType,
            dataToSign = verificationInfo.transactionInfo,
            timeStamp = verificationInfo.creationTime,
            additionalData = additionalData
        )
    }

    private fun createAdditionalData(transactionInfo: String): Map<TransactionAttribute, String> {
        val result: MutableMap<TransactionAttribute, String> = mutableMapOf()

        // Add the default type (of request) to the result.  Might be overriden if specified in additionalData.
        result.putIfAbsent(TransactionAttribute.Type, "PendingRequestTypeDefault")

        JSONObject(transactionInfo).let { transactionData ->
            transactionData.has("originIpAddress").let {
                result[TransactionAttribute.IPAddress] =
                    transactionData.optString("originIpAddress")
                transactionData.remove("originIpAddress")
            }

            transactionData.has("originUserAgent").let {
                result[TransactionAttribute.UserAgent] =
                    transactionData.optString("originUserAgent")
                transactionData.remove("originUserAgent")
            }

            transactionData.has("additionalData").let {
                val additionalData = JSONArray(transactionData.optString("additionalData"))
                val indicesToRemove = mutableListOf<Int>()

                for (i in 0 until additionalData.length()) {
                    val item = additionalData.getJSONObject(i)
                    val name = item.optString("name")

                    if (name.equals("type")) {
                        result[TransactionAttribute.Type] = item.optString("value")
                        indicesToRemove.add(i)
                    } else if (name.equals("originLocation")) {
                        result[TransactionAttribute.Location] = item.optString("value")
                        indicesToRemove.add(i)
                    } else if (name.equals("imageURL")) {
                        result[TransactionAttribute.Image] = item.optString("value")
                        indicesToRemove.add(i)
                    }
                }

                for (index in indicesToRemove.reversed()) {
                    additionalData.remove(index)
                }

                if (additionalData.length() > 0) {
                    result[TransactionAttribute.Custom] = additionalData.toString()
                }
            }
        }

        return result
    }

    private fun transactionMessage(value: String): String {
        JSONObject(value).let {
            return it.optString("message", "PendingRequestMessageDefault")
        }
    }
}