/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

sealed class MFAServiceError : Error() {

    class InvalidSigningHash : MFAServiceError()
    class InvalidPendingTransaction : MFAServiceError()
    class SerializationFailed : MFAServiceError()
    class InvalidDataResponse : MFAServiceError()
    class DecodingFailed : MFAServiceError()
    class UnableToCreateTransaction : MFAServiceError()
    data class General(override val message: String) : MFAServiceError()

    val errorDescription: String?
        get() = localizedDescription

    private val localizedDescription: String
        get() = when (this) {
            is InvalidSigningHash -> "The signing hash algorithm was invalid."
            is InvalidPendingTransaction -> "No pending transaction was available to complete."
            is SerializationFailed -> "Serialization conversion failed."
            is InvalidDataResponse -> "The response data was invalid."
            is DecodingFailed -> "The JSON decoding operation failed."
            is UnableToCreateTransaction -> "Unable to create the pending transaction."
            is General -> message
        }
}
