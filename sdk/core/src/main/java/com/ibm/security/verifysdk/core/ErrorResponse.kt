/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@ExperimentalSerializationApi
@Serializable
data class ErrorResponse(

    @JsonNames("messageId")
    val error: String,

    @JsonNames("error_description", "errorDescription", "messageDescription")
    val errorDescription: String
)