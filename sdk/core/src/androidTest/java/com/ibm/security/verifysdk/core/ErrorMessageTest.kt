package com.ibm.security.verifysdk.core

import com.ibm.security.verifysdk.core.serializer.DefaultJson
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
internal class ErrorMessageTest {

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
        val errorMessage = DefaultJson.decodeFromString<ErrorMessage>(errorMessageResponse)

        val serialized = DefaultJson.encodeToJsonElement(ErrorMessage.serializer(), errorMessage)
        val errorMessage2 = DefaultJson.decodeFromJsonElement(ErrorMessage.serializer(), serialized)

        assertEquals(errorMessage.error, errorMessage2.error)
        assertEquals(errorMessage.errorDescription, errorMessage2.errorDescription)
    }

    private val errorMessageResponse = """
        {"error":"ErrorId","error_description":"ErrorDescription"}
    """.trimIndent()
}