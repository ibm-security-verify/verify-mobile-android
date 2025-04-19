/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.extension

import io.ktor.client.statement.HttpResponse

/**
 *  Converts an HttpResponse object into a HttpResponseException object, encapsulating the HTTP
 *  status code with description and the original HttpResponse in a custom exception.
 */
fun HttpResponse.toResultFailure(message: String): HttpResponseException {
    return HttpResponseException(message, this)
}

class HttpResponseException(message: String, val response: HttpResponse) : Exception(message)
