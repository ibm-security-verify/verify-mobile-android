package com.ibm.security.verify.fido2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
