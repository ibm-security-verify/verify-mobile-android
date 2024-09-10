/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.model.CreateVerificationArgs
import com.ibm.security.verifysdk.dc.model.UpdateVerificationArgs
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import com.ibm.security.verifysdk.dc.model.VerificationList
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
open class VerificationsApi(private val baseUrl: URL) : BaseApi() {

    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
    ): Result<VerificationList> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/verifications")
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
    ): Result<VerificationInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/verifications/${id}")
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
            URL("$baseUrl/diagency/v1.0/diagency/verifications/${id}")
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
        createVerificationArgs: CreateVerificationArgs
    ): Result<VerificationInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/verifications")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            body = createVerificationArgs,
            method = HttpMethod.Post
        )
    }

    open suspend fun update(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String,
        updateVerificationArgs: UpdateVerificationArgs
    ): Result<VerificationInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/verifications/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            body = updateVerificationArgs,
            method = HttpMethod.Patch
        )
    }
}