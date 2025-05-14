/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
class ResidentCredentialOfferView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credential: ResidentCredentialOffer
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {
        Column(modifier = modifier) {
            LabelValueRow(
                label = "Type",
                value = "Resident card",
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "First name",
                value = credential.credentialSubject.givenName,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Last name",
                value = credential.credentialSubject.familyName,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Birth country",
                value = credential.credentialSubject.birthCountry,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Serializable
internal data class ResidentCredentialOffer(
    val credentialSubject: CredentialSubject,
    val issuanceDate: String = "Not provided"
)

@Serializable
internal data class CredentialSubject(
    val id: String,
    val givenName: String,
    val familyName: String,
    val birthCountry: String
)