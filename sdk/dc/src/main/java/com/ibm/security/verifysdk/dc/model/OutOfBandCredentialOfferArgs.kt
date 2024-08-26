/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class OutOfBandCredentialOfferArgs (

    @SerialName("attributes")
    val attributes: Map<String, String>,

    @SerialName("cred_def_id")
    val credDefId: String? = null,

    @SerialName("schema_name")
    val schemaName: String? = null,

    @SerialName("schema_version")
    val schemaVersion: String? = null,

    @SerialName("comment")
    val comment: String? = null,

    @SerialName("credential_preview")
    val credentialPreview: JsonElement? = null
)
