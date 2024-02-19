/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import java.util.Locale

val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
val snakeRegex = "_[a-zA-Z]".toRegex()

fun String.toNumberOrNull(): Number? {

    return this.toIntOrNull() ?: this.toLongOrNull() ?: this.toDoubleOrNull()
}

fun String.toNumberOrDefault(default: Number): Number {

    return this.toIntOrNull() ?: this.toLongOrNull() ?: this.toDoubleOrNull() ?: default
}

fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.lowercase(Locale.getDefault())
}

fun String.snakeToCamelCase(): String {
    return snakeRegex.replace(this) { match ->
        match.value.replace("_", "").uppercase()
            .replaceFirstChar { firstChar ->
                if (firstChar.isLowerCase()) firstChar.titlecase(Locale.getDefault()) else firstChar.toString()
            }
    }
}

fun String.decodeBase32(): ByteArray {
    val map = ('A'..'Z') + ('2'..'7')
    val reverseMap = map.withIndex().associate { it.value to it.index }

    val paddingLength = when (val padding = this.count { it == '=' }) {
        0, 6 -> 0
        1, 3 -> 8 - padding
        2, 4 -> 16 - padding
        5 -> throw IllegalArgumentException("Invalid padding in base32 string")
        else -> throw IllegalArgumentException("Invalid base32 string")
    }

    val binaryString = this
        .replace("=", "")
        .map { reverseMap[it]!! }
        .joinToString("") { it.toString(2).padStart(5, '0') }
        .dropLast(paddingLength)

    return binaryString.chunked(8).map { it.toInt(2).toByte() }.toByteArray()
}