/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc

import com.ibm.security.verifysdk.authentication.model.TokenInfo
import com.ibm.security.verifysdk.dc.model.CredentialAction
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.model.CredentialPreviewInfo
import com.ibm.security.verifysdk.dc.model.PreviewDescriptor
import com.ibm.security.verifysdk.dc.model.VerificationAction
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import com.ibm.security.verifysdk.dc.model.VerificationPreviewInfo
import java.net.URL

/**
 * Interface that defines the required methods and properties for interacting with a wallet service.
 *
 * This interface provides operations for managing access tokens, refreshing tokens, handling credentials,
 * and processing various wallet-related requests like invitations and proof requests.
 *
 * The methods in this interface are asynchronous and are meant to be used in a coroutine context.
 *
 * @property accessToken The access token for authenticating requests to the wallet service.
 * @property additionalHeaders  A map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
 * @property refreshUri The URI used to refresh the access token.
 * @property baseUri The base URI of the wallet service API.
 * @property clientId The client identifier used for authenticating requests to the wallet service.
 *
 * @since 3.0.4
 */
interface WalletServiceDescriptor {
    var accessToken: String
    var additionalHeaders: Map<String, String>
    val refreshUri: URL
    val baseUri: URL
    val clientId: String

    suspend fun refreshToken(
        refreshToken: String,
        accountName: String? = null,
        pushToken: String? = null
    ): Result<TokenInfo>

    suspend fun deleteAgent(identifier: String)

    suspend fun previewInvitation(offerUrl: URL): PreviewDescriptor

    suspend fun processProofRequest(
        verificationPreviewInfo: VerificationPreviewInfo,
        action: VerificationAction = VerificationAction.GENERATE
    ): VerificationInfo

    suspend fun <T : CredentialDescriptor> retrieveCredentials(type: Class<T>): List<T>
    suspend fun processCredential(
        credentialPreviewInfo: CredentialPreviewInfo,
        action: CredentialAction = CredentialAction.ACCEPTED
    ): CredentialDescriptor

    suspend fun deleteCredential(identifier: String)

}