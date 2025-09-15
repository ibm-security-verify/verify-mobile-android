/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import com.ibm.security.verifysdk.core.serializer.DefaultJson
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * VerifySdkException is a generic exception that can be thrown when working with Verify SDKs.
 *
 * @since 3.0.0
 */
@ExperimentalSerializationApi
@Suppress("MemberVisibilityCanBePrivate")
abstract class VerifySdkException(
    private val errorMessage: String,
    private val throwable: Throwable? = null
) : Throwable(throwable) {

    val error: String
    val errorDescription: String

    init {
        val decodedErrorMessage = DefaultJson.decodeFromString<ErrorMessage>(errorMessage)
        error = decodedErrorMessage.error
        errorDescription = decodedErrorMessage.errorDescription
    }

    /**
     * Returns a string representation of the exception object.
     *
     * @since 3.0.0
     */
    override fun toString(): String {

        val newLine: String = System.lineSeparator() ?: "\n"

        val stringBuilder = StringBuilder()
        stringBuilder.append("error: $error")
        stringBuilder.append(newLine)
        stringBuilder.append("errorDescription: $errorDescription")
        return stringBuilder.toString()
    }

    /**
     * Returns a string representation of the exception object and adding the prefix.
     *
     * @param prefix  a prefix to be added before of the error message
     *
     * @since 3.0.0
     */
    fun toString(prefix: String): String {

        val stringBuilder = StringBuilder()
        stringBuilder.append(prefix)
        stringBuilder.append(errorMessage)

        return stringBuilder.toString()
    }
}