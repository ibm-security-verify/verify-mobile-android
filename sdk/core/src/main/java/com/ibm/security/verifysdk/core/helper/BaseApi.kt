/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.helper

import com.ibm.security.verifysdk.core.extension.toResultFailure
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import java.net.URL

abstract class BaseApi() {

    /**
     * JSON configuration for parsing and serializing JSON data.
     *
     * This configuration object allows for customization of JSON parsing
     * and serialization behavior. It includes options to set leniency and
     * ignore unknown keys during JSON parsing.
     *
     * @property isLenient A flag indicating whether lenient parsing is enabled.
     *                     Lenient parsing allows parsing of malformed JSON by ignoring
     *                     unexpected tokens.
     * @property ignoreUnknownKeys A flag indicating whether unknown keys encountered
     *                              during parsing should be ignored.
     */
    protected val decoder = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    protected suspend inline fun <reified T> performRequest(
        httpClient: HttpClient = NetworkHelper.getInstance,
        url: URL,
        accessToken: String? = null,
        method: HttpMethod = HttpMethod.Get,
        body: Any? = null
    ): Result<T> {
        return try {
            val response = httpClient.request {
                url(url)
                this.method = method
                accessToken?.let { bearerAuth(it) }
                accept(ContentType.Application.Json)
                body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                }
            }

            if (response.status.isSuccess()) {
                when (response.status) {
                    HttpStatusCode.NoContent -> {
                        Result.success(Unit as T)
                    }

                    else -> {
                        Result.success(decoder.decodeFromString<T>(response.bodyAsText()))
                    }
                }

            } else {
                Result.failure(response.toResultFailure())
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}