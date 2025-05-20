/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.cloud.model.InvitationInfo
import com.ibm.security.verifysdk.dc.cloud.model.InvitationList
import com.ibm.security.verifysdk.dc.cloud.model.InvitationProcessorRequest
import com.ibm.security.verifysdk.dc.cloud.model.PreviewDescriptor
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

/**
 * Provides an API interface for managing and interacting with `invitations` API endpoints in IBM Verify Identity
 * Digital Credentials.
 *
 * This class allows clients to retrieve, delete, and process invitation records.
 * It communicates with a backend service via HTTP requests and handles response serialization.
 *
 * Example usage:
 * ```
 * val api = InvitationsApi(baseUrl)
 * val invitations = api.getAll(accessToken = "your-token")
 * ```
 *
 * @constructor Initializes a new instance of [InvitationsApi] using the provided base URL.
 * @param baseUrl The root URL for accessing the invitation endpoints.
 *
 * @see BaseApi
 * @see InvitationInfo
 * @see InvitationProcessorRequest
 * @see PreviewDescriptor
 *
 * @since 3.0.7
 */
@OptIn(ExperimentalSerializationApi::class)
@ExperimentalDigitalCredentialsSdk
open class InvitationsApi(private val baseUrl: URL) : BaseApi() {

    /**
     * Retrieves a list of invitations from the API.
     *
     * This function sends a GET request to the specified URL or the default URL for retrieving
     * a list of invitations. If the request is successful, it returns a list of [InvitationInfo] objects.
     * In case of a failure, it returns a failure result with the associated error message or exception.
     *
     * @param httpClient The HTTP client used to perform the request. Defaults to [NetworkHelper.getInstance] if not provided.
     * @param url The URL to send the GET request to. If not provided, a default URL will be used to retrieve invitations.
     * @param accessToken The access token used for authentication. This token is added as a bearer token in the request headers.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information.
     * @return A [Result] containing either a list of [InvitationInfo] objects if the request is successful,
     *         or a failure result with an error message if the request fails.
     *
     * @since 3.0.7
     */
    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null
    ): Result<List<InvitationInfo>> {

        return performRequest<InvitationList>(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/invitations"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        ).mapCatching { it.items }
    }

    /**
     * Retrieves details of a specific invitations by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the invitation to retrieve.
     * @return A [Result] containing either an [InvitationInfo] on success or an error on failure.
     *
     * @since 3.0.7
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String
    ): Result<InvitationInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/invitations/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        )
    }

    /**
     * Processes an invitation by sending a request to the invitation processor API.
     *
     * This function sends a PUT request to the specified URL or the default URL for processing an invitation.
     * It sends an [InvitationProcessorRequest] in the request body and returns a [PreviewDescriptor] object if the
     * operation is successful. In case of a failure, it returns a failure result with the associated error message or exception.
     *
     * @param httpClient The HTTP client used to perform the request. Defaults to [NetworkHelper.getInstance] if not provided.
     * @param url The URL to send the PUT request to. If not provided, a default URL will be used to process the invitation.
     * @param accessToken The access token used for authentication. This token is included as a bearer token in the request headers.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param invitationProcessorRequest The request body containing the data needed to process the invitation.
     *
     * @return A [Result] containing either a [PreviewDescriptor] object if the request is successful,
     *         or a failure result with an error message if the request fails.
     *
     * @since 3.0.7
     */
    open suspend fun processInvitation(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        invitationProcessorRequest: InvitationProcessorRequest
    ): Result<PreviewDescriptor> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/invitation_processor"),
            accessToken = accessToken,

            headers = additionalHeaders,
            body = invitationProcessorRequest,
            method = HttpMethod.Put
        )
    }

    /**
     * Deletes an invitation by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the invitation to delete.
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
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/invitations/$id"),
            accessToken = accessToken,

            headers = additionalHeaders,
            method = HttpMethod.Delete
        )
    }
}
