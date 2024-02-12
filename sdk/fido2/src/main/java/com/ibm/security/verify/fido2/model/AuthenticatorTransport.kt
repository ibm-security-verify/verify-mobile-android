/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An enum class representing the transport modality of an authenticator.
 *
 * @property value The string value representing the authenticator transport modality.
 */
@Serializable
enum class AuthenticatorTransport(val value: String) {
    @SerialName("usb")
    USB("usb"),
    @SerialName("nfc")
    NFC("nfc"),
    @SerialName("ble")
    BLE("ble"),
    @SerialName("internal")
    INTERNAL("internal"),
    @SerialName("hybrid")
    HYBRID("hybrid")
}
