/*
 *  Copyright contributors to the IBM Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import com.ibm.security.verifysdk.fido2.model.AuthenticatorTransport.BLE
import com.ibm.security.verifysdk.fido2.model.AuthenticatorTransport.HYBRID
import com.ibm.security.verifysdk.fido2.model.AuthenticatorTransport.INTERNAL
import com.ibm.security.verifysdk.fido2.model.AuthenticatorTransport.NFC
import com.ibm.security.verifysdk.fido2.model.AuthenticatorTransport.USB
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An enumeration representing authenticator transport options according to the FIDO2 specification.
 * Authenticator transport specifies the method by which the authenticator communicates with the client.
 *
 * This enumeration provides several options:
 * - [USB]: Represents authenticators communicating over USB.
 * - [NFC]: Represents authenticators communicating over Near Field Communication (NFC).
 * - [BLE]: Represents authenticators communicating over Bluetooth Low Energy (BLE).
 * - [INTERNAL]: Represents authenticators embedded within the client platform.
 * - [HYBRID]: Represents authenticators capable of communication over multiple transport methods.

 * @property value The string value representing the authenticator transport modality.
 *
 */
@Serializable
enum class AuthenticatorTransport(val value: String) {
    /**
     * Represents authenticators communicating over USB.
     */
    @SerialName("usb")
    USB("usb"),

    /**
     * Represents authenticators communicating over Near Field Communication (NFC).
     */
    @SerialName("nfc")
    NFC("nfc"),

    /**
     * Represents authenticators communicating over Bluetooth Low Energy (BLE).
     */
    @SerialName("ble")
    BLE("ble"),

    /**
     * Represents authenticators embedded within the client platform.
     */
    @SerialName("internal")
    INTERNAL("internal"),

    /**
     * Represents authenticators capable of communication over multiple transport methods.
     */
    @SerialName("hybrid")
    HYBRID("hybrid")
}
