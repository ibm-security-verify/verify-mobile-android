/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.model.ConnectionInfo
import com.ibm.security.verifysdk.dc.model.ConnectionInfoList
import com.ibm.security.verifysdk.dc.model.UpdateConnectionArgs
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
@ExperimentalDigitalCredentialsSdk
open class ConnectionsApi(private val baseUrl: URL) : BaseApi() {

    /**
     * Retrieves a list of connections from the API.
     *
     * This function sends a GET request to the specified URL or the default URL for retrieving
     * a list of connections. If the request is successful, it returns a list of [ConnectionInfo] objects.
     * If the request fails, it returns a failure result with the exception or error message.
     *
     * @param httpClient The HTTP client used to perform the request. Defaults to [NetworkHelper.getInstance].
     * @param url The URL to send the GET request to. If not provided, a default URL will be used.
     * @param accessToken The access token used for authentication. This will be included as a bearer token in the request headers.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @return A [Result] containing either a list of [ConnectionInfo] if the request is successful,
     *         or a failure result with an error if the request fails.
     *
     * @since 3.0.4
     */
    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null
    ): Result<List<ConnectionInfo>> {

        return performRequest<ConnectionInfoList>(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/connections"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        ).mapCatching { it.items }
    }

    /**
     * Retrieves details of a specific connection by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the connection to retrieve.
     * @return A [Result] containing either an [ConnectionInfo] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String
    ): Result<ConnectionInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/connections/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        )
    }

    /**
     * Deletes a connection by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the connection to delete.
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
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/connections/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Delete
        )
    }

    /**
     * Update a connection by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the connection to update.
     * @param updateConnectionArgs The [UpdateConnectionArgs] data to be sent in the request body for updating the connection.
     * @return A [Result] containing either [ConnectionInfo] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun update(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String,
        updateConnectionArgs: UpdateConnectionArgs
    ): Result<ConnectionInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/connections/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            body = updateConnectionArgs,
            method = HttpMethod.Patch
        )
    }
}