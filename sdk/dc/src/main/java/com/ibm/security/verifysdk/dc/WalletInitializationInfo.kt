/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

@file:UseSerializers(URLSerializer::class)
package com.ibm.security.verifysdk.dc

import com.ibm.security.verifysdk.core.serializer.URLSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URL

/**
 * Represents the initialization details required to set up a wallet.
 *
 * This class contains essential information such as wallet name, ID, authentication details, and service URLs.
 *
 * @since 3.0.7
 */
@ExperimentalSerializationApi
@Serializable
data class WalletInitializationInfo(
    val name: String,
    val id: String,
    val serviceBaseUrl: URL,
    val clientId: String,
    val aznCode: String,

    @SerialName("oauthBaseUrl")
    @Serializable(with = TokenURLSerializer::class)
    val tokenUrl: URL
) {

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = URL::class)
    internal object TokenURLSerializer : KSerializer<URL> {
        override fun deserialize(decoder: Decoder): URL {
            val urlString = decoder.decodeString()

            return URL(
                if (urlString.endsWith("/token") || urlString.endsWith("/token/")) {
                    urlString
                } else {
                    "$urlString/token"
                }
            )
        }

        override fun serialize(encoder: Encoder, value: URL) {
            encoder.encodeString(value.toString())
        }
    }
}