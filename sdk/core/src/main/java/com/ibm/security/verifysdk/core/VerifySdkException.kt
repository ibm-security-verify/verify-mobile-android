/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import java.lang.System.getProperty

/**
 * VerifySdkException is a generic exception that can be thrown when working with Verify SDKs.
 *
 * @since 3.0.0
 */
abstract class VerifySdkException(
    private val messageId: String,
    private val messageDescription: String,
    private val arguments: ArrayList<String>? = null,
    private val throwable: Throwable? = null
) {

    /**
     * Returns a string representation of the exception object.
     *
     * @since 3.0.0
     */
    override fun toString(): String {

        val newLine: String = getProperty("line.separator") ?: "\n"

        val stringBuilder = StringBuilder()
        stringBuilder.append(messageId)
        stringBuilder.append(newLine)
        stringBuilder.append(messageDescription)

        return stringBuilder.toString()
    }
}