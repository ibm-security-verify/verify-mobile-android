/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.model.AgentReference
import com.ibm.security.verifysdk.dc.model.AgentReferenceList
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import java.net.URL

/**
 * `AgentsApi` is an API client used to interact with `agents` API endpoints in IBM Verify Identity
 * Digital Credentials.
 * It provides methods to retrieve information about all agents, a specific agent, delete an agent,
 * and retrieve a DID (Decentralized Identifier) for an agent.
 *
 * @param baseUrl The base URL of the API server.
 */
open class AgentsApi(private val baseUrl: URL) : BaseApi() {

    /**
     * Retrieves a list of all agents.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @return A [Result] containing either an [AgentReferenceList] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
    ): Result<AgentReferenceList> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/agents")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Get
        )
    }


    /**
     * Retrieves details of a specific agent by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param id The ID of the agent to retrieve.
     * @return A [Result] containing either an [AgentReference] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String
    ): Result<AgentReference> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/agents/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Get
        )
    }

    /**
     * Deletes an agent by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param id The ID of the agent to delete.
     * @return A [Result] containing either [Unit] on success or an error on failure.
     *
     * @since 3.0.4
     */
    open suspend fun delete(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String
    ): Result<Unit> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/agents/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Delete
        )
    }

    /**
     * Retrieves the DID (Decentralized Identifier) of a specific agent by its ID.
     *
     * @param httpClient The [HttpClient] used for making the request. Defaults to [NetworkHelper.getInstance].
     * @param url URL to override the default endpoint. Defaults to `null`.
     * @param accessToken The access token required for authorization.
     * @param id The ID of the agent whose DID is to be retrieved.
     * @return A [Result] containing either an [AgentReference] on success or an error on failure.
     *
     * @Since 3.0.4
     */
    open suspend fun getDid(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String
    ): Result<AgentReference> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/agents/${id}/did.json")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Get
        )
    }
}