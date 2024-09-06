/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.model.ConnectionInfo
import com.ibm.security.verifysdk.dc.model.ConnectionList
import com.ibm.security.verifysdk.dc.model.CreateConnectionArgs
import com.ibm.security.verifysdk.dc.model.UpdateConnectionArgs
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
open class ConnectionsApi(private val baseUrl: URL) : BaseApi() {

    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
    ): Result<ConnectionList> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/connections")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Get
        )
    }

    open suspend fun getOne(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String
    ): Result<ConnectionInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/connections/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Get
        )
    }

    open suspend fun delete(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String
    ): Result<Unit> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/connections/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Delete
        )
    }

    open suspend fun create(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        createConnectionArgs: CreateConnectionArgs
    ): Result<ConnectionInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/connections")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            body = createConnectionArgs,
            method = HttpMethod.Post
        )
    }

    open suspend fun update(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String,
        updateConnectionArgs: UpdateConnectionArgs
    ): Result<ConnectionInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/connections/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            body = updateConnectionArgs,
            method = HttpMethod.Patch
        )
    }

}