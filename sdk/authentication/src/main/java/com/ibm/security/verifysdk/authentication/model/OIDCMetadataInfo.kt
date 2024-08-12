/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.authentication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The metadata that describes an OpenID Connect Provider configuration.
 *
 * @since 3.0.0
 */
@Serializable
data class OIDCMetadataInfo(

    val issuer: String,

    @SerialName("authorization_endpoint")
    val authorizationEndpoint: String,

    @SerialName("token_endpoint")
    val tokenEndpoint: String,

    @SerialName("userinfo_endpoint")
    val userinfoEndpoint: String,

    @SerialName("jwks_uri")
    val jwksUri: String,

    @SerialName("registration_endpoint")
    val registrationEndpoint: String,

    @SerialName("response_types_supported")
    val responseTypesSupported: Array<String>,

    @SerialName("response_modes_supported")
    val responseModesSupported: Array<String>,

    @SerialName("grant_types_supported")
    val grantTypesSupported: Array<String> = arrayOf("authorization_code", "implicit"),

    @SerialName("subject_types_supported")
    val subjectTypesSupported: Array<String>,

    @SerialName("id_token_signing_alg_values_supported")
    val idTokenSigningAlgValuesSupported: Array<String>,

    @SerialName("id_token_encryption_alg_values_supported")
    val idTokenEncryptionAlgValuesSupported: Array<String> = arrayOf("none"),

    @SerialName("id_token_encryption_enc_values_supported")
    val idTokenEncryptionEncValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of the JWS signing algorithms (alg Values) JWA supported by the
     * UserInfo Endpoint to encode the Claims in a JWT.
     */
    @SerialName("userinfo_signing_alg_values_supported")
    val userinfoSigningAlgValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of the JWE encryption algorithms (alg Values) JWA supported by
     * the UserInfo Endpoint to encode the Claims in a JWT.
     */
    @SerialName("userinfo_encryption_alg_values_supported")
    val userinfoEncryptionAlgValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of the JWE encryption algorithms (enc Values) JWA supported by
     * the UserInfo Endpoint to encode the Claims in a JWT.
     */
    @SerialName("userinfo_encryption_enc_values_supported")
    val userinfoEncryptionEncValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of the JWS signing algorithms (alg Values) supported by the
     * OpenID Connect Provider for Request Objects.
     */
    @SerialName("request_object_signing_alg_values_supported")
    val requestObjectSigningAlgValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of the JWE encryption algorithms (alg Values) supported by the
     * OpenID Connect Provider for Request Objects.
     */
    @SerialName("request_object_encryption_alg_values_supported")
    val requestObjectEncryptionAlgValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of the JWE encryption algorithms (enc Values) supported by the
     * OpenID Connect Provider for Request Objects.
     */
    @SerialName("request_object_encryption_enc_values_supported")
    val requestObjectEncryptionEncValuesSupported: Array<String> = arrayOf("none"),

    /**
     * An array containing a list of Client Authentication methods supported by this Token
     * Endpoint. The options are `client_secret_post`, `client_secret_basic`, `client_secret_jwt`,
     * and `private_key_jwt`.
     *
     * If omitted, the default Value is `client_secret_basic`.
     */
    @SerialName("token_endpoint_auth_methods_supported")
    val tokenEndpointAuthMethodsSupported: Array<String> = arrayOf("client_secret_basic"),

    /**
     * An array containing a list of the JWS signing algorithms (alg Values) supported by the
     * Token Endpoint for the signature on the JWT used to authenticate the Client at the Token
     * Endpoint for the `private_key_jwt` and `client_secret_jwt` authentication methods.
     */
    @SerialName("token_endpoint_auth_signing_alg_values_supported")
    val tokenEndpointAuthSigningAlgValuesSupported: Array<String>? = null,

    /**
     * An array containing a list of the display parameter Values that the OpenID Provider
     * supports.
     */
    @SerialName("display_values_supported")
    val displayValuesSupported: Array<String>? = null,

    /**
     * An array containing a list of the Claim Types that the OpenID Provider supports.
     *
     * If omitted, the default Value is `normal`.
     */
    @SerialName("claim_types_supported")
    val claimTypesSupported: Array<String> = arrayOf("normal"),

    /**
     * An array containing a list of the Claim Names of the Claims that the OpenID Provider
     * **may** be able to supply Values for.
     */
    @SerialName("claims_supported")
    val claimsSupported: Array<String>,

    /**
     * A URL of a page containing human-readable information that developers might want or need
     * to know when using the OpenID Provider.
     */
    @SerialName("service_documentation")
    val serviceDocumentation: String? = null,

    /**
     * An array of languages and scripts supported for Values in Claims being returned.
     */
    @SerialName("claims_locales_supported")
    val claimsLocalesSupported: Array<String>? = null,

    /**
     * An array of languages and scripts supported for the user interface.
     */
    @SerialName("ui_locales_supported")
    val uiLocalesSupported: Array<String>? = null,

    /**
     * A boolean Value specifying whether the OpenID Provider supports use of the claims
     * parameter, with `true` indicating support.
     *
     * If omitted, the default Value is `false`.
     */
    @SerialName("claims_parameter_supported")
    val claimsParameterSupported: Boolean = false,

    /**
     * A boolean Value specifying whether the OpenID Provider supports use of the request
     * parameter, with true indicating support.
     *
     * If omitted, the default Value is `false`.
     */
    @SerialName("request_parameter_supported")
    val requestParameterSupported: Boolean = false,

    /**
     * A boolean Value specifying whether the OpenID Provider supports use of the `request_uri`
     * parameter, with `true` indicating support.
     *
     * If omitted, the default Value is `true`.
     */
    @SerialName("request_uri_parameter_supported")
    val requestUriParameterSupported: Boolean = true,

    /**
     * A boolean Value specifying whether the OpenID Provider requires any `request_uri` Values
     * used to be pre-registered using the `request_uris` registration parameter. Pre-registration
     * is **required** when the Value is `true`.
     *
     * If omitted, the default Value is `false`.
     */
    @SerialName("require_request_uri_registration")
    val requireRequestUriRegistration: Boolean = false,

    /**
     * A URL that the OpenID Provider provides to the person registering the Client to read
     * about the OP's requirements on how the Relying Party can use the data provided by the
     * OpenID Provider.
     */
    @SerialName("op_policy_uri")
    val opPolicyUri: String? = null,

    /**
     * A URL that the OpenID Provider provides to the person registering the Client to read about
     * OpenID Provider's terms of service.
     */
    @SerialName("op_tos_uri")
    val opToSUri: String? = null
) {

    /**
     * {@inheritDoc}
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OIDCMetadataInfo

        if (issuer != other.issuer) return false
        if (authorizationEndpoint != other.authorizationEndpoint) return false
        if (tokenEndpoint != other.tokenEndpoint) return false
        if (userinfoEndpoint != other.userinfoEndpoint) return false
        if (jwksUri != other.jwksUri) return false
        if (registrationEndpoint != other.registrationEndpoint) return false
        if (!responseTypesSupported.contentEquals(other.responseTypesSupported)) return false
        if (!responseModesSupported.contentEquals(other.responseModesSupported)) return false
        if (!grantTypesSupported.contentEquals(other.grantTypesSupported)) return false
        if (!subjectTypesSupported.contentEquals(other.subjectTypesSupported)) return false
        if (!idTokenSigningAlgValuesSupported.contentEquals(other.idTokenSigningAlgValuesSupported)) return false
        if (!idTokenEncryptionAlgValuesSupported.contentEquals(other.idTokenEncryptionAlgValuesSupported)) return false
        if (!idTokenEncryptionEncValuesSupported.contentEquals(other.idTokenEncryptionEncValuesSupported)) return false
        if (!userinfoSigningAlgValuesSupported.contentEquals(other.userinfoSigningAlgValuesSupported)) return false
        if (!userinfoEncryptionAlgValuesSupported.contentEquals(other.userinfoEncryptionAlgValuesSupported)) return false
        if (!userinfoEncryptionEncValuesSupported.contentEquals(other.userinfoEncryptionEncValuesSupported)) return false
        if (!requestObjectSigningAlgValuesSupported.contentEquals(other.requestObjectSigningAlgValuesSupported)) return false
        if (!requestObjectEncryptionAlgValuesSupported.contentEquals(other.requestObjectEncryptionAlgValuesSupported)) return false
        if (!requestObjectEncryptionEncValuesSupported.contentEquals(other.requestObjectEncryptionEncValuesSupported)) return false
        if (!tokenEndpointAuthMethodsSupported.contentEquals(other.tokenEndpointAuthMethodsSupported)) return false
        if (tokenEndpointAuthSigningAlgValuesSupported != null) {
            if (other.tokenEndpointAuthSigningAlgValuesSupported == null) return false
            if (!tokenEndpointAuthSigningAlgValuesSupported.contentEquals(other.tokenEndpointAuthSigningAlgValuesSupported)) return false
        } else if (other.tokenEndpointAuthSigningAlgValuesSupported != null) return false
        if (displayValuesSupported != null) {
            if (other.displayValuesSupported == null) return false
            if (!displayValuesSupported.contentEquals(other.displayValuesSupported)) return false
        } else if (other.displayValuesSupported != null) return false
        if (!claimTypesSupported.contentEquals(other.claimTypesSupported)) return false
        if (!claimsSupported.contentEquals(other.claimsSupported)) return false
        if (serviceDocumentation != other.serviceDocumentation) return false
        if (claimsLocalesSupported != null) {
            if (other.claimsLocalesSupported == null) return false
            if (!claimsLocalesSupported.contentEquals(other.claimsLocalesSupported)) return false
        } else if (other.claimsLocalesSupported != null) return false
        if (uiLocalesSupported != null) {
            if (other.uiLocalesSupported == null) return false
            if (!uiLocalesSupported.contentEquals(other.uiLocalesSupported)) return false
        } else if (other.uiLocalesSupported != null) return false
        if (claimsParameterSupported != other.claimsParameterSupported) return false
        if (requestParameterSupported != other.requestParameterSupported) return false
        if (requestUriParameterSupported != other.requestUriParameterSupported) return false
        if (requireRequestUriRegistration != other.requireRequestUriRegistration) return false
        if (opPolicyUri != other.opPolicyUri) return false
        if (opToSUri != other.opToSUri) return false

        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        var result = issuer.hashCode()
        result = 31 * result + authorizationEndpoint.hashCode()
        result = 31 * result + tokenEndpoint.hashCode()
        result = 31 * result + userinfoEndpoint.hashCode()
        result = 31 * result + jwksUri.hashCode()
        result = 31 * result + registrationEndpoint.hashCode()
        result = 31 * result + responseTypesSupported.contentHashCode()
        result = 31 * result + responseModesSupported.contentHashCode()
        result = 31 * result + grantTypesSupported.contentHashCode()
        result = 31 * result + subjectTypesSupported.contentHashCode()
        result = 31 * result + idTokenSigningAlgValuesSupported.contentHashCode()
        result = 31 * result + idTokenEncryptionAlgValuesSupported.contentHashCode()
        result = 31 * result + idTokenEncryptionEncValuesSupported.contentHashCode()
        result = 31 * result + userinfoSigningAlgValuesSupported.contentHashCode()
        result = 31 * result + userinfoEncryptionAlgValuesSupported.contentHashCode()
        result = 31 * result + userinfoEncryptionEncValuesSupported.contentHashCode()
        result = 31 * result + requestObjectSigningAlgValuesSupported.contentHashCode()
        result = 31 * result + requestObjectEncryptionAlgValuesSupported.contentHashCode()
        result = 31 * result + requestObjectEncryptionEncValuesSupported.contentHashCode()
        result = 31 * result + tokenEndpointAuthMethodsSupported.contentHashCode()
        result = 31 * result + (tokenEndpointAuthSigningAlgValuesSupported?.contentHashCode() ?: 0)
        result = 31 * result + (displayValuesSupported?.contentHashCode() ?: 0)
        result = 31 * result + claimTypesSupported.contentHashCode()
        result = 31 * result + claimsSupported.contentHashCode()
        result = 31 * result + (serviceDocumentation?.hashCode() ?: 0)
        result = 31 * result + (claimsLocalesSupported?.contentHashCode() ?: 0)
        result = 31 * result + (uiLocalesSupported?.contentHashCode() ?: 0)
        result = 31 * result + claimsParameterSupported.hashCode()
        result = 31 * result + requestParameterSupported.hashCode()
        result = 31 * result + requestUriParameterSupported.hashCode()
        result = 31 * result + requireRequestUriRegistration.hashCode()
        result = 31 * result + (opPolicyUri?.hashCode() ?: 0)
        result = 31 * result + (opToSUri?.hashCode() ?: 0)
        return result
    }
}
