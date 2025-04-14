/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the different statuses of a presentation definition.
 *
 * The `Statuses` class contains three optional fields: `active`, `suspended`, and `revoked`,
 * each representing a status of a presentation definition (or credential) at a specific point in time.
 * Each status is represented as a `PdStatus` object, which contains additional details about the specific status.
 *
 * @property active An optional [PdStatus] representing the active status of the presentation definition.
 *                  If `null`, the active status is not defined.
 * @property suspended An optional [PdStatus] representing the suspended status of the presentation definition.
 *                     If `null`, the suspended status is not defined.
 * @property revoked An optional [PdStatus] representing the revoked status of the presentation definition.
 *                   If `null`, the revoked status is not defined.
 *
 * @since 3.0.4
 */
@Serializable
data class Statuses(

    @SerialName("active")
    val active: PdStatus? = null,

    @SerialName("suspended")
    val suspended: PdStatus? = null,

    @SerialName("revoked")
    val revoked: PdStatus? = null
)
