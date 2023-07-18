/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URL

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = URL::class)
object URLSerializer : KSerializer<URL> {
    override fun deserialize(input: Decoder): URL {
        return URL(input.decodeString())
    }

    override fun serialize(output: Encoder, obj: URL) {
        output.encodeString(obj.toString())
    }
}