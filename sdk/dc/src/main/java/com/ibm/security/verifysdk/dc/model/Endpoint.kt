/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Endpoint(

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String,

    @SerialName("pairwise")
    val pairwise: DidInfo,

    @SerialName("ext")
    val ext: Boolean,

    @SerialName("public")
    val `public`: DidInfo? = null,

    @SerialName("properties")
    val properties: Map<String, JsonElement>? = null,

    @SerialName("did")
    val did: String? = null,

    @SerialName("did_doc")
    val didDoc: JsonElement? = null,

    @SerialName("device")
    val device: DeviceDescriptor? = null
)