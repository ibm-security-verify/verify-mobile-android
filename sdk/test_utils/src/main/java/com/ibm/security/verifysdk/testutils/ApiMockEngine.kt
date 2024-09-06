/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.testutils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class ApiMockEngine {

    fun get(): MockEngine = client.engine as MockEngine
    fun getEngine(): HttpClient = client

    private val defaultHeaders = Headers.build {
        append(HttpHeaders.Accept, ContentType.Application.Json.toString())
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                /* Add dummy request, because the MockEngine constructor requires at least one.
                 * It will be  deleted when client is configured for tests.
                 */
                if (request.url.encodedPath == "/") {
                    respond("", HttpStatusCode.OK, responseHeaders)
                } else {
                    val currentStackFrame = Throwable().stackTrace[1]
                    val fileName = currentStackFrame.fileName
                    val lineNumber = currentStackFrame.lineNumber - 9
                    respond("Check $fileName at line $lineNumber for further details", HttpStatusCode.InternalServerError, responseHeaders)
                }
            }
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val responseHeaders = headers {
        append("Content-Type", ContentType.Application.Json.toString())
    }

    fun addMockResponse(
        method: HttpMethod,
        urlPath: String,
        httpCode: HttpStatusCode,
        headers: Headers = defaultHeaders,
        responseBody: String? = null
    ) {
        this.get().let { engine ->
            engine.config.addHandler { request ->
                if (request.method != method) {
                    respondError(HttpStatusCode.NotFound)
                }

                if (request.url.encodedPath.trimEnd('/') == urlPath.trimEnd('/')) {
                    respond(responseBody ?: "", httpCode, headers)
                } else {
                    error("Unhandled ${request.url.encodedPath}")
                }
            }
        }
    }
}