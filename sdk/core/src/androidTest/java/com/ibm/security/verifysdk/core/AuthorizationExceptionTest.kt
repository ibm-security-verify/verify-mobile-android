/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class AuthorizationExceptionTest {

    @Test
    fun constructor_happyPath_shouldReturnObject() {
        val e = AuthorizationException("ErrorId", "ErrorDescription")

        assertEquals("ErrorId", e.error)
        assertEquals("ErrorDescription", e.errorDescription)
        assertEquals(serializedError, e.errorMessage)
        assertEquals("error: ErrorId\nerrorDescription: ErrorDescription", e.toString())
        assertEquals("TAG $serializedError", e.toString("TAG"))
    }

    private val serializedError = """
        {"error":"ErrorId","errorDescription":"ErrorDescription"}
    """.trimIndent()
}