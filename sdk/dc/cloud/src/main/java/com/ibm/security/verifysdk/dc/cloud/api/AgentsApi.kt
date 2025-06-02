/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.core.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.cloud.model.AgentInfo
import com.ibm.security.verifysdk.dc.cloud.model.AgentInfoList
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

/**
 * `AgentsApi` is an API client used to interact with `agents` API endpoints in IBM Verify Identity
 * Digital Credentials.
 * It provides methods to retrieve information about all agents, a specific agent, delete an agent,
 * and retrieve a DID (Decentralized Identifier) for an agent.
 *
 * @param baseUrl The base URL of the API server.
 */
@ExperimentalDigitalCredentialsSdk
open class AgentsApi(private val baseUrl: URL) : BaseApi() {

    /**
     * Retrieves a list of all agents.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @return A [Result] containing either an [AgentInfo] on success or an error on failure.
     *
     * @since 3.0.7
     */
    @OptIn(ExperimentalSerializationApi::class)
    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null
    ): Result<List<AgentInfo>> {

        return performRequest<AgentInfoList>(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/agents"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        ).mapCatching { it.items }
    }


    /**
     * Retrieves details of a specific agent by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the agent to retrieve.
     * @return A [Result] containing either an [AgentInfo] on success or an error on failure.
     *
     * @since 3.0.7
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
        id: String
    ): Result<AgentInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/agents/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        )
    }

    /**
     * Retrieves details of a specific agent.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @return A [Result] containing either an [AgentInfo] on success or an error on failure.
     *
     * @since 3.0.9
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        additionalHeaders: Map<String, String>? = null,
    ): Result<AgentInfo> {

        return performRequest(
            httpClient = httpClient,
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/info"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Get
        )
    }

    /**
     * Deletes an agent by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param additionalHeaders Optional map of additional headers to include in the request. See [BaseApi.performRequest] for additional information
     * @param id The ID of the agent to delete.
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
            url = url ?: URL("$baseUrl/diagency/v1.0/diagency/agents/$id"),
            accessToken = accessToken,
            headers = additionalHeaders,
            method = HttpMethod.Delete
        )
    }
}