/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@ExperimentalSerializationApi
@Serializable
data class DifPresentationRequest (

    @SerialName("presentation_definition")
//    val presentationDefinition: PresentationDefinitionV1,
    val presentationDefinition: JsonElement,

    @SerialName("options")
//    val options: DifPresentationOptions? = null
    val options: JsonElement
)
