/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc

import com.ibm.security.verifysdk.authentication.api.OAuthProvider
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.core.serializer.DefaultJson
import com.ibm.security.verifysdk.dc.api.AgentsApi
import com.ibm.security.verifysdk.dc.api.ConnectionsApi
import com.ibm.security.verifysdk.dc.api.CredentialsApi
import com.ibm.security.verifysdk.dc.api.InvitationsApi
import com.ibm.security.verifysdk.dc.api.VerificationsApi
import com.ibm.security.verifysdk.dc.model.CredentialState
import com.ibm.security.verifysdk.dc.model.VerificationState
import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException

/**
 * The [WalletProvider] class is responsible for initializing and providing a wallet instance.
 * It interacts with the wallet service by retrieving necessary data such as agent information, connections, invitations,
 * credentials, and verifications, after authenticating the user with their credentials.
 *
 * This class handles the full flow of initialization and data retrieval asynchronously using coroutines.
 * The primary method, [initiate], performs the necessary steps to set up a wallet by:
 * 1. Parsing the configuration and initializing OAuth authorization.
 * 2. Fetching the necessary wallet-related data (agents, connections, invitations, credentials, verifications).
 *
 * **Key Components**:
 * - [OAuthProvider] to handle OAuth authentication and retrieve the access token.
 * - Multiple API calls to fetch relevant data from the wallet service (e.g., agents, credentials, invitations).
 * - [Wallet] object returned after successful initialization, containing all the data necessary to interact with the wallet.
 *
 * **Error Handling**:
 * If any errors occur during the initialization or data retrieval process, relevant [WalletError] instances are thrown,
 * such as [WalletError.DataInitializationFailed] or [WalletError.FailedToParse].
 *
 * @param httpClient The HTTP client used for network requests (defaults to an instance from [NetworkHelper]).
 * @param jsonData The JSON data containing wallet initialization information.
 * @param ignoreSSLCertificate Flag indicating whether to ignore SSL certificate verification (defaults to false).
 *
 * @since 3.0.7
 */
@OptIn(ExperimentalSerializationApi::class, ExperimentalDigitalCredentialsSdk::class)
class WalletProvider(
    private val httpClient: HttpClient = NetworkHelper.getInstance,
    private val jsonData: String,
    private val ignoreSSLCertificate: Boolean = false
) {

    suspend fun initiate(
        name: String,
        username: String,
        password: String,
        pushToken: String? = null
    ): Wallet = coroutineScope {

        val walletInitializationInfo = try {
            DefaultJson.decodeFromString(WalletInitializationInfo.serializer(), jsonData)
        } catch (e: SerializationException) {
            throw WalletError.DataInitializationFailed()
        }

        val additionalParameters = mutableMapOf<String, String>().apply {
            if (pushToken != null) put("pushToken", pushToken)
        }

        val oauthProvider = OAuthProvider(
            clientId = walletInitializationInfo.clientId,
            additionalParameters = additionalParameters
        )

        oauthProvider.ignoreSsl = ignoreSSLCertificate

        val token = try {
            oauthProvider.authorize(
                url = walletInitializationInfo.tokenUrl,
                username = username,
                password = password,
                scope = arrayOf("openid")
            ).getOrThrow()
        } catch (e: Exception) {
            throw WalletError.FailedToParse()
        }

        val agentDeferred = async {
            AgentsApi(walletInitializationInfo.serviceBaseUrl).getOne(
                httpClient = httpClient,
                accessToken = token.accessToken
            )
        }

        val connectionDeferred = async {
            ConnectionsApi(walletInitializationInfo.serviceBaseUrl).getAll(
                httpClient = httpClient,
                accessToken = token.accessToken
            )
        }

        val invitationsDeferred = async {
            InvitationsApi(walletInitializationInfo.serviceBaseUrl).getAll(
                httpClient = httpClient,
                accessToken = token.accessToken
            )
        }

        val credentialsDeferred = async {
            CredentialsApi(walletInitializationInfo.serviceBaseUrl).getAll(
                httpClient = httpClient,
                accessToken = token.accessToken
            )
        }

        val verificationsDeferred = async {
            VerificationsApi(walletInitializationInfo.serviceBaseUrl).getAll(
                httpClient = httpClient,
                accessToken = token.accessToken,
                state = VerificationState.PASSED
            )
        }

        try {
            val agentInfo = agentDeferred.await().getOrThrow()
            val connections = connectionDeferred.await().getOrThrow()
            val invitations = invitationsDeferred.await().getOrThrow()
            val credentials = credentialsDeferred.await().getOrThrow()
            val verifications = verificationsDeferred.await().getOrThrow()

            Wallet(
                refreshUri = walletInitializationInfo.tokenUrl,
                baseUri = walletInitializationInfo.serviceBaseUrl,
                clientId = walletInitializationInfo.clientId,
                token = token,
                agent = agentInfo,
                connections = connections.toMutableList(),
                invitations = invitations.toMutableList(),
                credentials = credentials.filter { it.state == CredentialState.STORED }.toMutableList(),
                verifications = verifications.toMutableList()
            )
        } catch (e: Exception) {
            throw WalletError.General(e.toString())
        }
    }
}
