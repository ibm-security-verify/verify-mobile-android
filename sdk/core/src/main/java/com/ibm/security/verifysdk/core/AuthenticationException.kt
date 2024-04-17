/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * This exception occurs when authentication has failed.
 *
 * @since 3.0.0
 */
@OptIn(ExperimentalSerializationApi::class)
class AuthenticationException(val code: HttpStatusCode, id: String, description: String) :
    VerifySdkException("{\"error\":\"$id\",\"errorDescription\":\"$description\"}")
