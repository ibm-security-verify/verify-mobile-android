/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.helper

import com.ibm.security.verifysdk.core.serializer.DefaultJson
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.MovedPermanently
import io.ktor.http.HttpStatusCode.Companion.PermanentRedirect
import io.ktor.http.HttpStatusCode.Companion.SeeOther
import io.ktor.http.HttpStatusCode.Companion.TemporaryRedirect
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import java.net.URL

abstract class BaseApi() {

    /**
     * JSON decoder instance configured for flexible parsing.
     *
     * This decoder is set up with the following settings:
     * - `encodeDefaults = true`: Includes default values when encoding.
     * - `explicitNulls = false`: Omits `null` values from the output.
     * - `ignoreUnknownKeys = true`: Allows parsing JSON objects with extra, unknown keys.
     * - `isLenient = true`: Permits relaxed JSON formatting, such as unquoted keys or single quotes.
     */


    /**
     * Performs an HTTP request and handles the response, parsing it into the specified type.
     *
     * This function executes an HTTP request using the provided HTTP client and various request parameters.
     * It decodes the response body into a specified type [T] if the request is successful, or returns a failure
     * result with a detailed error message in case of a failed response.
     *
     * ### Authorization Header Handling:
     * - If the provided [headers] do **not** include the `Authorization` header, and an [accessToken] is provided,
     *   the function will automatically apply a `Bearer` token in the `Authorization` header.
     * - If the `Authorization` header **is** already present in [headers], the [accessToken] is **ignored** and not applied.
     * - This allows callers to fully override the authorization behavior by manually specifying the `Authorization` header.
     *
     * @param httpClient The HTTP client to use for the request. Defaults to the instance provided by [NetworkHelper.getInstance].
     * @param method The HTTP method to use (e.g., GET, POST). Defaults to [HttpMethod.Get].
     * @param url The URL to which the request is sent. This parameter is required.
     * @param accessToken An optional bearer token for authentication. If provided, it is added to the request headers.
     * @param headers An optional map of additional headers to include in the request.
     * @param contentType The content type for the request body. Defaults to [ContentType.Application.Json].
     * @param body The body of the request, which will be serialized and sent along with the request. This parameter is optional.
     *
     * @return A [Result] containing either the parsed response body as type [T] if successful,
     *         or a failure result with an error message in case of failure.
     */
    protected suspend inline fun <reified T> performRequest(
        httpClient: HttpClient = NetworkHelper.getInstance,
        method: HttpMethod = HttpMethod.Get,
        url: URL,
        accessToken: String? = null,
        headers: Map<String, String>? = null,
        contentType: ContentType = ContentType.Application.Json,
        body: Any? = null
    ): Result<T> = safeRunCatchingSuspend {
        val response = httpClient.request {
            url(url)
            this.method = method
            headers?.forEach { (key, value) -> this.headers.append(key, value) }
            if (headers?.containsKey(HttpHeaders.Authorization) != true) {
                accessToken?.let {
                    bearerAuth(it)
                }
            }
            accept(ContentType.Application.Json)
            body?.let { requestBody ->
                contentType(contentType)
                setBody(requestBody)
            }
        }

        when {
            response.status == HttpStatusCode.NoContent -> {
                Unit as T
            }

            response.status in setOf(
                PermanentRedirect,
                MovedPermanently,
                SeeOther,
                TemporaryRedirect
            ) -> {
                val location = response.headers[HttpHeaders.Location]
                    ?: throw IllegalStateException("Missing 'Location' header")
                location as T
            }

            response.status.isSuccess() -> {
                DefaultJson.decodeFromString<T>(response.bodyAsText())
            }

            else -> {

                val errorText = response.bodyAsText()
                val errorResponse = safeRunCatchingSuspend {
                    DefaultJson.decodeFromString<ErrorResponse>(errorText)
                }.getOrNull()

                throw ErrorResponse(
                    statusCode = response.status,
                    message = errorResponse?.message
                        ?: "HTTP error: ${response.status.value} ${response.status.description}",
                    responseBody = errorText
                )
            }
        }
    }
}

class ErrorResponse(
    val statusCode: HttpStatusCode,
    override val message: String,
    val responseBody: String? = null
) : Exception(message)