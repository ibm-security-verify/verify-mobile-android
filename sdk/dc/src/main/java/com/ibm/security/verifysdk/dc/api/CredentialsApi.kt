/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.model.CredentialList
import com.ibm.security.verifysdk.dc.model.UpdateCredentialArgs
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

/**
 * Provides an API interface for interacting with `credential` API endpoints in IBM Verify Identity
 * Digital Credentials.
 *
 * This class includes operations to retrieve, update, and delete credential records. It utilizes
 * an HTTP client to send authorized requests to a backend service and parse the responses into Kotlin data classes.
 *
 * Example usage:
 * ```
 * val api = CredentialsApi(baseUrl)
 * val result = api.getAll(accessToken = "token")
 * ```
 *
 * @constructor Creates an instance of [CredentialsApi] with the given base URL.
 * @param baseUrl The root URL used for sending API requests to the credential service.
 *
 * @see BaseApi
 * @see CredentialDescriptor
 * @see UpdateCredentialArgs
 *
 * @since 3.0.7
 */
@OptIn(ExperimentalSerializationApi::class)
@ExperimentalDigitalCredentialsSdk
open class CredentialsApi(private val baseUrl: URL) : BaseApi() {

    /**
     * Retrieves a list of credentials from the API.
     *
     * This function sends a GET request to the specified URL or the default URL for retrieving
     * a list of credentials. Upon a successful response, it returns a list of [CredentialDescriptor] objects.
     * If the request fails, it returns a failure result with an error message or exception.
     *
     * @param httpClient The HTTP client used to perform the request. Defaults to [NetworkHelper.getInstance].
     * @param url The URL to send the GET request to. If not provided, a default URL is used to retrieve the credentials.
     * @param accessToken The access token used for authentication. This token will be included as a bearer token in the request headers.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @return A [Result] containing either a list of [CredentialDescriptor] objects if the request is successful,
     *         or a failure result with an error if the request fails.
     *
     * @since 3.0.7
     */
    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
    ): Result<List<CredentialDescriptor>> {

        return performRequest<CredentialList>(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/credentials"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        ).mapCatching { it.items }
    }

    /**
     * Retrieves details of a specific credential by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the credential to retrieve.
     * @return A [Result] containing either an [CredentialDescriptor] on success or an error on failure.
     *
     * @since 3.0.7
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String
    ): Result<CredentialDescriptor> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/credentials/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        )
    }

    /**
     * Deletes a credential by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the credential to delete.
     * @return A [Result] containing either [Unit] on success or an error on failure.
     *
     * @since 3.0.7
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
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/credentials/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Delete
        )
    }

    /**
     * Update a credential by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the credential to update.
     * @param updateCredentialArgs The [UpdateCredentialArgs] to be sent in the request body for updating the credential.
     * @return A [Result] containing either [CredentialDescriptor] on success or an error on failure.
     *
     * @since 3.0.7
     */
    open suspend fun update(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String,
        updateCredentialArgs: UpdateCredentialArgs,
    ): Result<CredentialDescriptor> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/credentials/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            body = updateCredentialArgs,
            method = HttpMethod.Patch
        )
    }
}
