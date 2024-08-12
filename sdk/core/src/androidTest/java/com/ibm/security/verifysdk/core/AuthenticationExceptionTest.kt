/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import io.ktor.http.HttpStatusCode
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class AuthenticationExceptionTest {

    @Test
    fun constructor_happyPath_shouldReturnObject() {
        val e = AuthenticationException(HttpStatusCode.InternalServerError, "ErrorId", "ErrorDescription")

        assertEquals("ErrorId", e.error)
        assertEquals("ErrorDescription", e.errorDescription)
        assertEquals(HttpStatusCode.InternalServerError, e.code)
        assertEquals("error: ErrorId\nerrorDescription: ErrorDescription", e.toString())
        assertEquals("TAG $serializedError", e.toString("TAG "))
    }

    private val serializedError = """
        {"error":"ErrorId","errorDescription":"ErrorDescription"}
    """.trimIndent()
}