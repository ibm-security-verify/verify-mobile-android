/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Format(

    @SerialName("jwt")
    val jwt: JwtObject? = null,

    @SerialName("jwt_vc")
    val jwtVc: JwtObject? = null,

    @SerialName("jwt_vc_json")
    val jwtVcJson: JwtObject? = null,

    @SerialName("jwt_vp")
    val jwtVp: JwtObject? = null,

    @SerialName("jwt_vp_json")
    val jwtVpJson: JwtObject? = null,

    @SerialName("ldp")
    val ldp: LdpObject? = null,

    @SerialName("ldp_vc")
    val ldpVc: LdpObject? = null,

    @SerialName("ldp_vp")
    val ldpVp: LdpObject? = null,

    @SerialName("di")
    val di: DiObject? = null,

    @SerialName("di_vc")
    val diVc: DiObject? = null,

    @SerialName("di_vp")
    val diVp: DiObject? = null,

    @SerialName("undefined")
    val undefined: SdJwtObject? = null
)
