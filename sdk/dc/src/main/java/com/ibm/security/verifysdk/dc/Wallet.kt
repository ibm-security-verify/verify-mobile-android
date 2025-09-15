/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

@file:UseSerializers(URLSerializer::class)
package com.ibm.security.verifysdk.dc

import com.ibm.security.verifysdk.authentication.model.TokenInfo
import com.ibm.security.verifysdk.core.serializer.URLSerializer
import com.ibm.security.verifysdk.dc.model.AgentInfo
import com.ibm.security.verifysdk.dc.model.ConnectionInfo
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.model.InvitationInfo
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

/**
 * Represents a digital wallet containing credentials, connections, and verification details.
 *
 * This class is used to manage a wallet's authentication, agent information, and stored
 * digital credentials. It provides the necessary details to interact with a credential
 * ecosystem, including connection management and verification tracking.
 *
 * @property refreshUri The URL used to refresh the wallet's authentication token.
 * @property baseUri The base URL of the wallet service.
 * @property clientId The client identifier used for authentication.
 * @property clientSecret An optional client secret for authentication, if required.
 * @property token The authentication token used to access wallet services.
 * @property agent Information about the agent managing the wallet.
 * @property connections A mutable list of active connections associated with the wallet.
 * @property invitations A mutable list of pending invitations for establishing new connections.
 * @property credentials A mutable list of digital credentials stored in the wallet.
 * @property verifications A mutable list of verification processes associated with the wallet.
 *
 * **Serialization:**
 * - The `refreshUri` and `baseUri` properties use a custom serializer (`URLSerializer`) to handle URL serialization.
 *
 * @since 3.0.7
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Wallet (
    val refreshUri: URL,
    val baseUri: URL,
    val clientId: String,
    val clientSecret: String? = null,
    val token: TokenInfo,
    val agent: AgentInfo,
    val connections: MutableList<ConnectionInfo>,
    val invitations: MutableList<InvitationInfo>,
    val credentials: MutableList<CredentialDescriptor>, // pragma: allowlist secret
    val verifications: MutableList<VerificationInfo>
)