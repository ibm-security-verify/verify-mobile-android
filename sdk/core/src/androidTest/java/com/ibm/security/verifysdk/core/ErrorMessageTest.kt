package com.ibm.security.verifysdk.core

import junit.framework.TestCase.assertEquals
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
internal class ErrorMessageTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun getError() {
        val errorMessage = ErrorMessage("ErrorId", "ErrorDescription")
        assertEquals("ErrorId", errorMessage.error)
    }

    @Test
    fun getErrorDescription() {
        val errorMessage = ErrorMessage("ErrorId", "ErrorDescription")
        assertEquals("ErrorDescription", errorMessage.errorDescription)
    }

    @Test
    fun toString_shouldReturnString() {
        val errorMessage = ErrorMessage("ErrorId", "ErrorDescription")
        val jsonElement = errorMessage.toString()

        assertEquals("ErrorMessage(error=ErrorId, errorDescription=ErrorDescription)", jsonElement)
    }

    @Test
    fun serialize() {
        val errorMessage = json.decodeFromString<ErrorMessage>(errorMessageResponse)

        val serialized = json.encodeToJsonElement(ErrorMessage.serializer(), errorMessage)
        val errorMessage2 = json.decodeFromJsonElement(ErrorMessage.serializer(), serialized)

        assertEquals(errorMessage.error, errorMessage2.error)
        assertEquals(errorMessage.errorDescription, errorMessage2.errorDescription)
    }

    private val errorMessageResponse = """
        {"error":"ErrorId","error_description":"ErrorDescription"}
    """.trimIndent()
}