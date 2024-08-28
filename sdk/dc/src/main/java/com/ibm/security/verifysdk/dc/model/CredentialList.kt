package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 *
 *
 * @param count
 * @param items
 */
@ExperimentalSerializationApi
@Serializable
data class CredentialList(

    @SerialName("count")
    val count: Int,

    @SerialName("items")
    val items: List<CredentialInfo>

)