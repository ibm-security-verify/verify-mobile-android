/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc.device

import com.ibm.security.verifysdk.dc.device.model.DeviceCredentialDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class Wallet (
    val name: String,
    val credentials: MutableList<DeviceCredentialDescriptor>
)