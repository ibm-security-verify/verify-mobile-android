/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
class BankingCredentialOfferView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credentialOffer: BankingCredentialOffer
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        val labelWidth = 100.dp

        Column(modifier = modifier) {
            LabelValueRow(
                label = "Type",
                value = "Bank account",
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
            LabelValueRow(
                label = "Name",
                value = credentialOffer.familyName,
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
            LabelValueRow(
                label = "Given name",
                value = credentialOffer.givenName,
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
            LabelValueRow(
                label = "Birth date",
                value = credentialOffer.birthDate,
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
            LabelValueRow(
                label = "Account number",
                value = credentialOffer.accountNumber,
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
            LabelValueRow(
                label = "Email address",
                value = credentialOffer.emailAddress,
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
        }
    }
}

@Serializable
private data class BankingCredentialOffer(
    @SerialName("family_name")
    val familyName: String,
    @SerialName("middle_name")
    val middleName: String,
    @SerialName("given_name")
    val givenName: String,
    @SerialName("birth_date")
    val birthDate: String,
    @SerialName("resident_address")
    val residentAddress: String,
    @SerialName("document_number")
    val documentNumber: String,
    @SerialName("resident_state")
    val residentState: String,
    @SerialName("resident_postal_code")
    val residentPostalCode: String,
    @SerialName("institution_number")
    val institutionNumber: String,
    @SerialName("transit_number")
    val transitNumber: String,
    @SerialName("account_number")
    val accountNumber: String,
    @SerialName("email_address")
    val emailAddress: String
)