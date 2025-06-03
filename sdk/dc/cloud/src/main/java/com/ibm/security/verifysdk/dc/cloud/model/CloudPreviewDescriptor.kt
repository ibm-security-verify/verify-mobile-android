/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import com.ibm.security.verifysdk.dc.cloud.serializer.CloudPreviewSerializer
import com.ibm.security.verifysdk.dc.core.PreviewDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable(with = CloudPreviewSerializer::class)
abstract class CloudPreviewDescriptor : PreviewDescriptor {

    abstract override val id: String
    abstract override val url: String
    abstract override val label: String?
    abstract override val comment: String?
    abstract override val jsonRepresentation: JsonElement?
}