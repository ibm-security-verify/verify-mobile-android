/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */
package com.ibm.security.verifysdk.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@ExperimentalSerializationApi
@Serializable
data class ErrorMessage(
    @JsonNames("messageId")
    val error: String = "No identifier available",

    @JsonNames("error_description", "errorDescription", "messageDescription")
    val errorDescription: String = "No description available"
)  : Exception("$error : $errorDescription")