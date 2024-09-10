/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttrCredChoice (
    
    @SerialName("names")
    val names: List<NameValue>,
    
    @SerialName("schema_id")
    val schemaId: String,
    
    @SerialName("cred_def_id")
    val credDefId: String

)