/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = HashAlgorithmType::class)
object HashAlgorithmTypeSerializer : KSerializer<HashAlgorithmType> {
    override fun deserialize(decoder: Decoder): HashAlgorithmType {
        return HashAlgorithmType.fromString(decoder.decodeString())
    }
    override fun serialize(encoder: Encoder, value: HashAlgorithmType) {
        encoder.encodeString(value.toString())
    }
}