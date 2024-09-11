/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Statuses(

    @SerialName("active")
    val active: PdStatus? = null,

    @SerialName("suspended")
    val suspended: PdStatus? = null,

    @SerialName("revoked")
    val revoked: PdStatus? = null
)
