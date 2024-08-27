/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
@file:Suppress("unused")

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.model.InvitationInfo
import com.ibm.security.verifysdk.dc.model.InvitationList
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
open class InvitationsApi(private val baseUrl: URL) : BaseApi() {

    open suspend fun delete(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String
    ): Result<Unit> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/invitations/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Delete
        )
    }

    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String
    ): Result<InvitationList> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/invitations")
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
    ): Result<InvitationInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/invitations/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Get
        )
    }
}
