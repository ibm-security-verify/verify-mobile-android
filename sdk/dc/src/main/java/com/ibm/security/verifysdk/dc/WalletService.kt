/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc

import com.ibm.security.verifysdk.authentication.api.OAuthProvider
import com.ibm.security.verifysdk.authentication.model.TokenInfo
import com.ibm.security.verifysdk.core.extension.baseUrl
import com.ibm.security.verifysdk.dc.api.AgentsApi
import com.ibm.security.verifysdk.dc.api.CredentialsApi
import com.ibm.security.verifysdk.dc.api.InvitationsApi
import com.ibm.security.verifysdk.dc.api.VerificationsApi
import com.ibm.security.verifysdk.dc.model.CredentialAction
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.model.CredentialPreviewInfo
import com.ibm.security.verifysdk.dc.model.CredentialState
import com.ibm.security.verifysdk.dc.model.InvitationProcessorRequest
import com.ibm.security.verifysdk.dc.model.PreviewDescriptor
import com.ibm.security.verifysdk.dc.model.UpdateCredentialArgs
import com.ibm.security.verifysdk.dc.model.UpdateVerificationArgs
import com.ibm.security.verifysdk.dc.model.VerificationAction
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import com.ibm.security.verifysdk.dc.model.VerificationPreviewInfo
import com.ibm.security.verifysdk.dc.model.VerificationState
import java.net.URL

/**
 * The [WalletService] class provides implementation for the [WalletServiceDescriptor] interface.
 * It offers various operations related to wallet management, including refreshing the access token,
 * processing invitations, managing credentials, and handling proof requests.
 *
 * This service interacts with the wallet API to perform actions such as fetching credentials,
 * processing proofs, and updating the status of credentials and verifications.
 *
 * **Key Operations**:
 * - Refresh access tokens using the provided refresh token.
 * - Preview and process invitations.
 * - Process proof requests such as generating, sharing, or rejecting proofs.
 * - Retrieve, update, and delete credentials.
 * - Delete agents associated with the wallet.
 *
 * **Error Handling**:
 * The service uses [Result] to wrap the responses of operations and will throw appropriate exceptions
 * in case of errors, such as network failures or invalid API responses.
 *
 * @param accessToken The access token to authenticate requests to the wallet service.
 * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
 * @param refreshUri The URI endpoint to refresh the access token.
 * @param baseUri The base URI for the wallet service API.
 * @param clientId The client identifier for OAuth authentication.
 * @param ignoreSsl Flag indicating whether to ignore SSL certificate verification (defaults to false).
 *                 This should only be used for local development and debugging. It should not be used
 *                 in production environments as it can expose the application to security risks.

 * @since 3.0.7
 */
@OptIn(ExperimentalDigitalCredentialsSdk::class)
class WalletService(
    override var accessToken: String,
    override var additionalHeaders: Map<String, String> = emptyMap(),
    override val refreshUri: URL,
    override val baseUri: URL,
    override val clientId: String,
    private val ignoreSsl: Boolean = false
) : WalletServiceDescriptor {

    /**
     * Refreshes the access token using the provided refresh token and optional metadata.
     *
     * This function communicates with the configured OAuth provider to request a new access token.
     * It optionally includes the `pushToken` and `accountName` as additional parameters in the request,
     * if provided. SSL verification can be bypassed based on the `ignoreSsl` configuration.
     *
     * @param refreshToken The refresh token to be used for obtaining a new access token.
     * @param accountName Optional name of the account to associate with the token request.
     * @param pushToken Optional push token to include for push notification support.
     *
     * @return A [Result] wrapping a [TokenInfo] object on success, or an error on failure.
     *
     * @since 3.0.7
     */
    override suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?
    ): Result<TokenInfo> {

        val additionalParameters = mutableMapOf<String, String>()
        pushToken?.let { additionalParameters["pushToken"] = it }
        accountName?.let { additionalParameters["accountName"] = it }

        val oauthProvider =
            OAuthProvider(
                clientId = clientId,
                additionalHeaders = additionalHeaders,
                additionalParameters = additionalParameters
            )

        oauthProvider.ignoreSsl = this.ignoreSsl

        return oauthProvider.refresh(url = refreshUri, refreshToken = refreshToken)
            .onSuccess {
                accessToken = it.accessToken
                Result.success(it)
            }
            .onFailure { Result.failure<TokenInfo>(it) }
    }

    override suspend fun previewInvitation(offerUrl: URL): PreviewDescriptor {
        return processInvitation(offerUrl)
    }

    /**
     * Processes a proof request based on the given action and verification preview information.
     *
     * This function handles three main actions related to a proof request:
     * - [VerificationAction.GENERATE]: Triggers the generation of a proof by processing the invitation URL,
     *   then updates the verification state to `PROOF_GENERATED`.
     * - [VerificationAction.SHARE]: Updates the verification state to `PROOF_SHARED`, indicating the proof
     *   has been sent.
     * - [VerificationAction.REJECT]: Updates the verification state to `FAILED`, indicating the user has
     *   declined to share the proof.
     *
     * @param verificationPreviewInfo The metadata and preview details of the proof request.
     * @param action The action to perform on the proof request: generate, share, or reject.
     *
     * @return A [VerificationInfo] object representing the updated state of the verification.
     *
     * @throws Exception if the API call fails or the verification state cannot be updated.
     *
     * @since 3.0.7
     */
    override suspend fun processProofRequest(
        verificationPreviewInfo: VerificationPreviewInfo,
        action: VerificationAction
    ): VerificationInfo {

        return when (action) {
            VerificationAction.GENERATE -> {
                processInvitation(URL(verificationPreviewInfo.url), forePreview = false)

                VerificationsApi(baseUri).update(
                    accessToken = accessToken,
                    id = verificationPreviewInfo.id,
                    updateVerificationArgs = UpdateVerificationArgs(state = VerificationState.PROOF_GENERATED)
                ).getOrThrow()
            }

            VerificationAction.SHARE -> {
                VerificationsApi(baseUri).update(
                    accessToken = accessToken,
                    id = verificationPreviewInfo.id,
                    updateVerificationArgs = UpdateVerificationArgs(state = VerificationState.PROOF_SHARED)
                ).getOrThrow()
            }

            VerificationAction.REJECT -> {
                VerificationsApi(baseUri).update(
                    accessToken = accessToken,
                    id = verificationPreviewInfo.id,
                    updateVerificationArgs = UpdateVerificationArgs(state = VerificationState.FAILED)
                ).getOrThrow()
            }
        }
    }

    /**
     * Retrieves all stored credentials of the specified type.
     *
     * This function fetches all credentials from the backend using the current access token,
     * then filters and casts them to the provided credential type [T]. Only credentials that
     * are compatible with the specified type will be included in the returned list.
     *
     * @param T The specific subclass of [CredentialDescriptor] to retrieve.
     * @param type The [Class] object representing the type [T] to filter and cast the credentials.
     *
     * @return A list of credentials of type [T], or an empty list if none are found or match.
     *
     * @throws Exception if the credential retrieval fails.
     *
     * @since 3.0.7
     */
    override suspend fun <T : CredentialDescriptor> retrieveCredentials(type: Class<T>): List<T> {

        return CredentialsApi(baseUri).getAll(accessToken = accessToken).fold(
            onSuccess = { result -> result.mapNotNull { type.cast(it) } },
            onFailure = { throw it },
        )
    }

    /**
     * Processes a credential offer by handling the associated invitation and applying the specified action.
     *
     * This function first processes the credential invitation URL (distinct from a standard connection invitation)
     * to move the offer into an `outbound_offer` state. After that, it applies the given [action]—either accepting
     * or rejecting the credential—by delegating to [processCredential] with the credential’s identifier.
     *
     * @param credentialPreviewInfo The preview information for the credential offer, including its ID and invitation URL.
     * @param action The action to apply to the credential—either [CredentialAction.ACCEPTED] or [CredentialAction.REJECTED].
     *
     * @return The updated [CredentialDescriptor] representing the credential after processing.
     *
     * @throws Exception if processing the invitation or updating the credential fails.
     *
     * @since 3.0.7
     */
    override suspend fun processCredential(
        credentialPreviewInfo: CredentialPreviewInfo,
        action: CredentialAction
    ): CredentialDescriptor {
        // Set the offer - not the invitation! - to an 'outbound_offer' state.
        processInvitation(URL(credentialPreviewInfo.url), forePreview = false)

        return processCredential(credentialPreviewInfo.id, action)
    }

    /**
    * Processes a credential by updating its state based on the given action.
    *
    * This function sends an update request to the backend to mark a credential
    * as either accepted or rejected. The state is determined by the [action] provided.
    * If the update is successful, the updated [CredentialDescriptor] is returned.
    *
    * @param credentialPreviewInfo The unique identifier of the credential to be updated.
    * @param action The action to apply to the credential—either [CredentialAction.ACCEPTED]
    * or [CredentialAction.REJECTED].
    *
    * @return The updated [CredentialDescriptor] reflecting the new state.
    *
    * @throws Exception if the update operation fails.
     *
     * @since 3.0.7
    */
    private suspend fun processCredential(
        credentialPreviewInfo: String,
        action: CredentialAction
    ): CredentialDescriptor {

        val updateCredentialArgs: UpdateCredentialArgs = when (action) {
            CredentialAction.ACCEPTED -> {
                UpdateCredentialArgs(
                    state = CredentialState.ACCEPTED
                )
            }

            CredentialAction.REJECTED -> {
                UpdateCredentialArgs(
                    state = CredentialState.REJECTED
                )
            }
        }

        return CredentialsApi(baseUri).update(
            accessToken = accessToken,
            id = credentialPreviewInfo,
            updateCredentialArgs = updateCredentialArgs
        ).getOrThrow()
    }

    /**
     * Deletes a credential identified by the given [identifier].
     *
     * This function sends a delete request to the backend to remove the credential
     * associated with the provided ID. If the operation fails, the exception is thrown.
     *
     * @param identifier The unique identifier of the credential to be deleted.
     *
     * @throws Exception if the deletion operation fails.
     *
     * @since 3.0.7
     */
    override suspend fun deleteCredential(identifier: String) {

        return CredentialsApi(baseUri).delete(accessToken = accessToken, id = identifier).fold(
            onSuccess = { },
            onFailure = { throw it },
        )
    }

    /**
     * Deletes an agent identified by the given [identifier].
     *
     * This function sends a delete request to the backend using the provided agent ID and current access token.
     * If the operation is successful, it completes silently. If the deletion fails, the encountered exception is thrown.
     *
     * @param identifier The unique identifier of the agent to be deleted.
     *
     * @throws Exception if the deletion operation fails.
     *
     * @since 3.0.7
     */
    override suspend fun deleteAgent(identifier: String) {

        return AgentsApi(baseUri).delete(accessToken = accessToken, id = identifier).fold(
            onSuccess = { },
            onFailure = { throw it },
        )
    }

    /**
    * Processes an invitation or offer URL and returns a [PreviewDescriptor] describing the result.
    *
    * This function sends the provided [offerUrl] to the backend to either preview (inspect) or fully
    * process the invitation, depending on the [forePreview] flag. It builds an [InvitationProcessorRequest]
    * with the URL and the inspection mode, then sends it to the appropriate [InvitationsApi] instance
    * based on the URL's base path.
    *
    * @param offerUrl The full URL representing the invitation or credential offer to be processed.
    * @param forePreview If `true`, the invitation is only inspected (previewed). If `false`, the invitation is fully processed.
    *
    * @return A [PreviewDescriptor] containing details about the processed invitation or offer.
    *
    * @throws Exception if the backend request fails or the invitation cannot be processed.
     *
     * @since 3.0.7
    */
    private suspend fun processInvitation(
        offerUrl: URL,
        forePreview: Boolean = true
    ): PreviewDescriptor {

        val invitationProcessorRequest = InvitationProcessorRequest(
            url = offerUrl.toString(),
            inspect = forePreview
        )

        return InvitationsApi(baseUri.baseUrl()).processInvitation(
            accessToken = this.accessToken,
            invitationProcessorRequest = invitationProcessorRequest,
        ).getOrThrow()
    }
}