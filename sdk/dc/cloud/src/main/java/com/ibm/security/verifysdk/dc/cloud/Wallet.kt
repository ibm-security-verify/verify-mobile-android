/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc

import com.ibm.security.verifysdk.authentication.model.TokenInfo
import com.ibm.security.verifysdk.core.serializer.URLSerializer
import com.ibm.security.verifysdk.dc.cloud.model.AgentInfo
import com.ibm.security.verifysdk.dc.cloud.model.ConnectionInfo
import com.ibm.security.verifysdk.dc.cloud.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.cloud.model.InvitationInfo
import com.ibm.security.verifysdk.dc.cloud.model.VerificationInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
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
    @Serializable(with = URLSerializer::class)
    val refreshUri: URL,
    @Serializable(with = URLSerializer::class)
    val baseUri: URL,
    val clientId: String,
    var clientSecret: String? = null,
    var token: TokenInfo,
    val agent: AgentInfo,
    var connections: MutableList<ConnectionInfo>,
    var invitations: MutableList<InvitationInfo>,
    var credentials: MutableList<CredentialDescriptor>, // pragma: allowlist secret
    var verifications: MutableList<VerificationInfo>
) : WalletDescriptor