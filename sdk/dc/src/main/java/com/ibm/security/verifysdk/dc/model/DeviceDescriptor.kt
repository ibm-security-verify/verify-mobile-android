/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class DeviceDescriptor (

    @SerialName("os_type")
    val osType: OSType,

    @SerialName("app_type")
    val appType: AppType,

    @SerialName("env_type")
    val envType: EnvType,

    @SerialName("token")
    val token: String,

    @SerialName("ref")
    val ref: String,

    @SerialName("name")
    val name: String? = null
)