/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.Serializable

@Serializable
data class AgentReferenceList(

    val count: Int,
    val items: List<AgentReference>
)