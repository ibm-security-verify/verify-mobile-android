/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import com.ibm.security.verifysdk.dc.cloud.serializer.PreviewSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * An interface representing a descriptor for a preview of a credential or invitation.
 *
 * This interface defines the basic structure for any object that serves as a preview descriptor.
 * It includes properties such as the ID, URL, label, comment, and a JSON representation of the
 * preview, which can be used for further processing or display.
 *
 * @property id The unique identifier of the preview.
 * @property url The URL associated with the preview.
 * @property label An optional label for the preview.
 * @property comment An optional comment that provides additional context about the preview.
 * @property jsonRepresentation An optional JSON element representing the preview in a structured format.
 *
 * @since 3.0.7
 */
@Serializable(with = PreviewSerializer::class)
interface PreviewDescriptor {
    val id: String
    val url: String
    val label: String?
    val comment: String?
    val jsonRepresentation: JsonElement?
}