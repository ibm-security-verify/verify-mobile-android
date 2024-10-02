package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.BaseApi
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.model.CredentialInfo
import com.ibm.security.verifysdk.dc.model.CredentialList
import com.ibm.security.verifysdk.dc.model.UpdateCredentialArgs
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
@ExperimentalDigitalCredentialsSdk
open class CredentialsApi(private val baseUrl: URL) : BaseApi() {

    open suspend fun getAll(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
    ): Result<CredentialList> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/credentials")
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
    ): Result<CredentialInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/credentials/${id}")
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
            URL("$baseUrl/diagency/v1.0/diagency/credentials/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            method = HttpMethod.Delete
        )
    }

    open suspend fun update(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL? = null,
        accessToken: String,
        id: String,
        updateCredentialArgs: UpdateCredentialArgs
    ): Result<CredentialInfo> {

        val localUrl: URL = url ?: run {
            URL("$baseUrl/diagency/v1.0/diagency/credentials/${id}")
        }

        return performRequest(
            httpClient = httpClient,
            url = localUrl,
            accessToken = accessToken,
            body = updateCredentialArgs,
            method = HttpMethod.Patch
        )
    }
}
