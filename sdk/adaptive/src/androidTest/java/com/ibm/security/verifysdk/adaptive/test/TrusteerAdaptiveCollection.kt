/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive.test

// TODO: To run tests, import Trusteer SDK.
import android.content.Context
import com.ibm.security.verifysdk.adaptive.AdaptiveCollectionService

//import com.trusteer.tas.TAS_CLIENT_INFO
//import com.trusteer.tas.tasWired.*

/**
 * A structure storing the vendor ID, client ID, and client key of a Trusteer client used to
 * initiate the collection process.
 *
 * Useful for the [AdaptiveContext.start] method.
 *
 * @param vendorId The identifier of the Trusteer vendor.
 * @param clientId The identifier of the Trusteer client.
 * @param clientKey The key of the Trusteer client.
 *
 * Example usage:
 * ```
 * val trusteerCollection = TrusteerAdaptiveCollection("hcu.com", "hcu.bankingapp", "YMAQAABN...CNC4JULI")
 * ```
 *
 * @since 3.0.0
 */
class TrusteerAdaptiveCollection(
    /** The identifier of the vendor. */
    override val vendorId: String,
    /** The client identifier. */
    override val clientId: String,
    /** The client key. */
    override val clientKey: String
) : AdaptiveCollectionService {
    override fun start(context: Context, sessionId: String) {
//        val clientInfo = TAS_CLIENT_INFO()
//        clientInfo.vendorId = this.vendorId
//        clientInfo.clientId = this.clientId
//        clientInfo.clientKey = this.clientKey
//        println(clientInfo.toString())
//
//        val result = TasStart(context, clientInfo, TAS_INIT_NO_OPT, null, sessionId)
//        if (result != TAS_RESULT_SUCCESS) {
//            throw TrusteerOperationException(result)
//        }
        println("Trusteer start status: Success")
    }

    override fun stop() {
//        val result = TasStop()
//
//        if (result != TAS_RESULT_SUCCESS) {
//            throw TrusteerOperationException(result)
//        }
    }

    override fun reset(sessionId: String) {
//        val result = TasResetSession(sessionId)
//
//        if (result != TAS_RESULT_SUCCESS) {
//            throw TrusteerOperationException(result)
//        }
    }
}

/**
 * Initialises a new [TrusteerOperationException] from a `TAS_RESULT_*` code from
 * [com.trusteer.tas.TasDefs].
 *
 * @param code A `TAS_RESULT_*` code from [com.trusteer.tas.TasDefs] to initialise the exception
 * with.
 *
 * Example usage:
 * ```
 * throw TrusteerOperationException(TAS_RESULT_NETWORK_ERROR)
 * ```
 *
 * @since 3.0.0
 */
class TrusteerOperationException(code: Int) :
    Exception(TrusteerOperationError.from(code).errorDescription)

/**
 * Defines the return value when an adaptive start or stop operation occurs.
 *
 * Represents the type of error for handling [AdaptiveContext.start] and [AdaptiveContext.stop]
 * operations.
 *
 * @since 3.0.0
 */
enum class TrusteerOperationError {
    /** A general error occurred during the collection process. */
    GENERAL_ERROR,

    /** An internal error occurred. Contact support. */
    INTERNAL_ERROR,

    /** The argument to initiate the collection process were incorrect. */
    INCORRECT_ARGUMENTS,

    /** The reference DRA item was not found. */
    NOT_FOUND,

    /** No polling has been configured. */
    NO_POLLING,

    /** Time out occurred. */
    TIME_OUT,

    /** The TAS collection process not initialized */
    NOT_INITIALIZED,

    /** Licence not authorized to perform operation. */
    LICENCE_NOT_AUTHORIZED,

    /** The TAS collection process already initialized. */
    ALREADY_INITIALIZED,

    /** Architecture not supported. */
    ARCHITECTURE_NOT_SUPPORTED,

    /** Incorrect TAS setup. */
    INCORRECT_SETUP,

    /** An internal exception occurred. Contact support. */
    INTERNAL_EXCEPTION,

    /** Insufficient permissions for collection process. */
    INSUFFICIENT_PERMISSIONS,

    /** Missing permission in tas folder or tas folder does not exist. */
    MISSING_PERMISSION_IN_FOLDER,

    /** TAS collection disabled due to configuration options. */
    DISABLED_BY_CONFIGURATION,

    /** A network error occurred. */
    NETWORK_ERROR,

    /** The internal connection timed out. Contact support. */
    INTERNAL_CONNECTION_TIMEOUT,

    /** Certificate error. Contact support. */
    CERTIFICATE_ERROR;

    companion object {
        /**
         * Initializes the [TrusteerOperationError] enum type from an [Int] representing a
         * `TAS_RESULT_*`.
         * @param value The integer of a `TAS_RESULT_*`.
         *
         * Example usage:
         * ```
         * val trusteereOperationError = TrusteerOperationError.from(TAS_RESULT_NETWORK_ERROR)
         * ```
         */
        fun from(value: Int): TrusteerOperationError {
            return when (value) {
//                TAS_RESULT_GENERAL_ERROR -> GENERAL_ERROR
//                TAS_RESULT_INTERNAL_ERROR -> INTERNAL_ERROR
//                TAS_RESULT_WRONG_ARGUMENTS -> INCORRECT_ARGUMENTS
//                TAS_RESULT_DRA_ITEM_NOT_FOUND -> NOT_FOUND
//                TAS_RESULT_NO_POLLING -> NO_POLLING
//                TAS_RESULT_TIMEOUT -> TIME_OUT
//                TAS_RESULT_NOT_INITIALIZED -> NOT_INITIALIZED
//                TAS_RESULT_UNAUTHORIZED -> LICENCE_NOT_AUTHORIZED
//                TAS_RESULT_ALREADY_INITIALIZED -> ALREADY_INITIALIZED
//                TAS_RESULT_ARCH_NOT_SUPPORTED -> ARCHITECTURE_NOT_SUPPORTED
//                TAS_RESULT_INCORRECT_SETUP -> INCORRECT_SETUP
//                TAS_RESULT_INTERNAL_EXCEPTION -> INTERNAL_EXCEPTION
//                TAS_RESULT_INSUFFICIENT_PERMISSIONS -> INSUFFICIENT_PERMISSIONS
//                TAS_RESULT_MISSING_PERMISSIONS_IN_FOLDER -> MISSING_PERMISSION_IN_FOLDER
//                TAS_RESULT_DISABLED_BY_CONFIGURATION -> DISABLED_BY_CONFIGURATION
//                TAS_RESULT_NETWORK_ERROR -> NETWORK_ERROR
//                TAS_RESULT_CONNECTION_INTERNAL_TIMEOUT -> INTERNAL_CONNECTION_TIMEOUT
//                TAS_RESULT_PINPOINT_CERTIFICATE_PROBLEM -> CERTIFICATE_ERROR
                else -> GENERAL_ERROR
            }
        }
    }
}

val TrusteerOperationError.errorDescription: String
    /**
     * Gets the error description of a [TrusteerOperationError].
     *
     * Example usage:
     * ```
     * TrusteerOperationError.NETWORK_ERROR.errorDescription // Returns "A network error occurred."
     * ```
     */
    get() {
        return when (this) {
            TrusteerOperationError.GENERAL_ERROR -> "A general error occurred during the collection process."
            TrusteerOperationError.INTERNAL_ERROR,
            TrusteerOperationError.INTERNAL_EXCEPTION -> "An internal error occurred. Contact support."
            TrusteerOperationError.INCORRECT_ARGUMENTS -> "The argument to initiate the collection process were incorrect."
            TrusteerOperationError.NOT_FOUND -> "The reference DRA item was not found."
            TrusteerOperationError.NO_POLLING -> "No polling has been configured."
            TrusteerOperationError.TIME_OUT,
            TrusteerOperationError.INTERNAL_CONNECTION_TIMEOUT -> "Time out occurred."
            TrusteerOperationError.NOT_INITIALIZED -> "The TAS collection process not initialized."
            TrusteerOperationError.LICENCE_NOT_AUTHORIZED -> "Licence not authorized to perform operation."
            TrusteerOperationError.ALREADY_INITIALIZED -> "The TAS collection process already initialized."
            TrusteerOperationError.ARCHITECTURE_NOT_SUPPORTED -> "Architecture not supported."
            TrusteerOperationError.INCORRECT_SETUP -> "Incorrect TAS setup."
            TrusteerOperationError.INSUFFICIENT_PERMISSIONS -> "Insufficient permissions for collection process."
            TrusteerOperationError.MISSING_PERMISSION_IN_FOLDER -> "Missing permission in tas folder or tas folder does not exist."
            TrusteerOperationError.DISABLED_BY_CONFIGURATION -> "TAS collection disabled due to configuration options."
            TrusteerOperationError.NETWORK_ERROR -> "A network error occurred."
            TrusteerOperationError.CERTIFICATE_ERROR -> "Certificate error. Contact support."
        }
    }