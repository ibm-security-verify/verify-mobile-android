/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc

import kotlinx.serialization.Serializable

@Serializable
data class QrCode(
    var type : String? = null,
    var data : QrCodeData?   = QrCodeData()
)