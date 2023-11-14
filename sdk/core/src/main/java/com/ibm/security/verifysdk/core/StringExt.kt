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