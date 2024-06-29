/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.authentication.test

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class ApiMockEngine {

    fun get(): MockEngine = client.engine as MockEngine

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                /* Add dummy request, because the MockEngine constructor requires at least one.
                 * It will be  deleted when client is configured for tests.
                 */
                if (request.url.encodedPath == "/") {
                    respond("", HttpStatusCode.OK, responseHeaders)
                } else {
                    respond("", HttpStatusCode.InternalServerError, responseHeaders)
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
}