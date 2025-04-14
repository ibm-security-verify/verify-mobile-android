/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.model.UpdateVerificationArgs
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import com.ibm.security.verifysdk.dc.model.VerificationList
import com.ibm.security.verifysdk.dc.model.VerificationState
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
@ExperimentalDigitalCredentialsSdk
open class VerificationsApi(private val baseUrl: URL) : BaseApi() {

    /**
     * Retrieves a list of verifications from the API.
     *
     * This function sends a GET request to the specified URL or the default URL for retrieving
     * a list of verifications. If the request is successful, it returns a list of [VerificationInfo] objects.
     * In case of a failure, it returns a failure result with the associated error message or exception.
     *
     * @param httpClient The HTTP client used to perform the request. Defaults to [NetworkHelper.getInstance] if not provided.
     * @param url The URL to send the GET request to. If not provided, a default URL will be used to retrieve invitations.
     * @param accessToken The access token used for authentication. This token is added as a bearer token in the request headers.
     * @param state The verification state to filter the verifications by. Defaults to [VerificationState.PASSED].
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @return A [Result] containing either a list of [VerificationInfo] objects if the request is successful,
     *         or a failure result with an error message if the request fails.
     *
     * @since 3.0.4
     */
    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        state: VerificationState = VerificationState.PASSED
    ): Result<List<VerificationInfo>> {

        return performRequest<VerificationList>(
            httpClient = httpClient,
            headers = additionalHeaders,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/verifications?state=${state.value}"),
            accessToken = accessToken,
            method = HttpMethod.Get
        ).mapCatching { it.items }
    }

    /**
     * Retrieves details of a specific verification by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the verification to retrieve.
     * @return A [Result] containing either an [VerificationInfo] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String
    ): Result<VerificationInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/verifications/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        )
    }

    /**
     * Deletes an verification by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the verification to delete.
     * @return A [Result] containing either [Unit] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun delete(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String
    ): Result<Unit> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/verifications/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Delete
        )
    }

    /**
     * Update a verification by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the verification to update.
     * @param updateVerificationArgs The [UpdateVerificationArgs] to be sent in the request body for updating the credential.
     * @return A [Result] containing either [VerificationInfo] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun update(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String,
        updateVerificationArgs: UpdateVerificationArgs
    ): Result<VerificationInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/verifications/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            body = updateVerificationArgs,
            method = HttpMethod.Patch
        )
    }
}