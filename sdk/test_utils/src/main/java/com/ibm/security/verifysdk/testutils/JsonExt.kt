/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.testutils

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.InputStreamReader

val json = Json {
    encodeDefaults = true
    explicitNulls = false
    ignoreUnknownKeys = false
    isLenient = true
}

fun loadJsonFromRawResource(resId: Int): JsonObject {
    val inputStream = InstrumentationRegistry.getInstrumentation().targetContext.resources.openRawResource(resId)
    val jsonString = InputStreamReader(inputStream).use { it.readText() }
    return json.parseToJsonElement(jsonString).jsonObject
}