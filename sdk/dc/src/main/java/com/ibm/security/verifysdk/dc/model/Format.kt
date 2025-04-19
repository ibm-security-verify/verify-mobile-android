/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents various credential formats supported in a digital credentialing system.
 *
 * This class holds different representations of credentials, such as JWT-based and Linked Data Proof (LDP) formats.
 * It provides optional fields for different credential and presentation formats that may be used in verifiable credential exchanges.
 *
 * @property jwt A general JWT-based object representing a credential.
 * @property jwtVc A JWT-based Verifiable Credential (VC) representation.
 * @property jwtVcJson A JSON-encoded JWT-based Verifiable Credential.
 * @property jwtVp A JWT-based Verifiable Presentation (VP) representation.
 * @property jwtVpJson A JSON-encoded JWT-based Verifiable Presentation.
 * @property ldp A general Linked Data Proof (LDP) object.
 * @property ldpVc A Linked Data Proof representation of a Verifiable Credential.
 * @property ldpVp A Linked Data Proof representation of a Verifiable Presentation.
 * @property mdoc A Mobile Driving License (mDoc) or Mobile ID document representation.
 *
 * @since 3.0.7
 */
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

    @SerialName("mso_mdoc")
    val mdoc: MdocObject? = null,
)