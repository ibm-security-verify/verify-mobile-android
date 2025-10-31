/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import io.ktor.http.HttpStatusCode
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AuthorizationExceptionTest {

    @Test
    fun constructor_happyPath_shouldReturnObject() {
        val e = AuthorizationException(HttpStatusCode.BadRequest, "ErrorId", "ErrorDescription")

        assertEquals("ErrorId", e.error)
        assertEquals("ErrorDescription", e.errorDescription)
        TestCase.assertEquals(HttpStatusCode.BadRequest, e.code)
        assertEquals("error: ErrorId\nerrorDescription: ErrorDescription", e.toString())
        assertEquals("TAG $serializedError", e.toString("TAG "))
    }

    private val serializedError = """
        {"error":"ErrorId","errorDescription":"ErrorDescription"}
    """.trimIndent()
}