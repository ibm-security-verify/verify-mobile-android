/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import com.ibm.security.verifysdk.core.extension.toNumberOrDefault
import com.ibm.security.verifysdk.core.extension.toNumberOrNull
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class StringExtKtTest {

    @Test
    fun toNumberOrNull() {
        assertEquals(42, "42".toNumberOrNull())
        assertEquals(4200000000, "4200000000".toLong().toString().toNumberOrNull())
        assertEquals(42.42424242, "42.42424242".toDouble().toString().toNumberOrNull())
        assertEquals(null, "".toNumberOrNull())
    }

    @Test
    fun toNumberOrDefault() {
        assertEquals(42, "42".toNumberOrDefault(99))
        assertEquals(4200000000, "4200000000".toLong().toString().toNumberOrDefault(99))
        assertEquals(42.42424242, "42.42424242".toDouble().toString().toNumberOrDefault(99))
        assertEquals(99, "".toNumberOrDefault(99))
    }
}