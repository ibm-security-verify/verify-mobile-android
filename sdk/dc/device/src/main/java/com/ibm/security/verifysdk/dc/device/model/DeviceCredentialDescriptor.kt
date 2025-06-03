/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.device.model

import com.ibm.security.verifysdk.dc.core.CredentialDescriptor
import com.ibm.security.verifysdk.dc.core.CredentialFormat
import com.ibm.security.verifysdk.dc.core.CredentialRole
import com.ibm.security.verifysdk.dc.core.CredentialState
import com.ibm.security.verifysdk.dc.core.DID
import com.ibm.security.verifysdk.dc.device.serializer.DeviceCredentialSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable(with = DeviceCredentialSerializer::class)
abstract class DeviceCredentialDescriptor : CredentialDescriptor {

    abstract override val id: String

    @SerialName("format")
    abstract override val format: CredentialFormat

    abstract override val role: CredentialRole

    abstract override val state: CredentialState

    abstract override val issuerDid: DID

    abstract override val jsonRepresentation: JsonElement?
}