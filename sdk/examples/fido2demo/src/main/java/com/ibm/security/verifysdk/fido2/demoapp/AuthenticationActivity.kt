/*
 *  Copyright contributors to the IBM Security Verify FIDO2 Sample App for Android project
 */
package com.ibm.security.verifysdk.fido2.demoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ibm.security.verify.fido2.Fido2Api
import com.ibm.security.verify.fido2.model.AssertionOptions
import com.ibm.security.verify.fido2.model.AuthenticatorAssertionResponse
import com.ibm.security.verify.fido2.model.PublicKeyCredentialRequestOptions
import com.ibm.security.verifysdk.core.KeystoreHelper
import io.ktor.http.Url
import io.ktor.http.hostWithPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.experimental.or

class AuthenticationActivity : AppCompatActivity() {

    private val fido2Api = Fido2Api()
    private val keyName = "61683285-900f-4bed-87e0-b83b5277ba93"
    private val reasons = arrayListOf(
        "Please confirm your pizza order.",
        "Verify your transfer of $2,877.34.",
        "Confirm you purchased a MacBook.",
        "Are you accessing the server room?",
        "Confirmation is required.",
        "Confirm your order of 10 widgets."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val sharedPreferences =
            getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

        val accessToken = sharedPreferences.getString("accessToken", null) ?: ""
        val relyingPartyUrl = sharedPreferences.getString("relyingPartyUrl", null) ?: ""
        val nickName = sharedPreferences.getString("nickName", null) ?: ""
        val createdAt = sharedPreferences.getString("createdAt", null) ?: ""
        val keyName = sharedPreferences.getString("keyName", null) ?: ""
        val userName = sharedPreferences.getString("userName", null)!!

        val relyingPartyUrlTextView: TextView = findViewById(R.id.text_view_relying_party_url)
        val nicknameTextView: TextView = findViewById(R.id.text_view_nickname)
        val createdAtTextView: TextView = findViewById(R.id.text_view_created_at)
        val initiateAuthentication: Button = findViewById(R.id.button_initiate_authentication)
        val removeAuthenticator: Button = findViewById(R.id.button_remove_authenticator)
        val transactionMessage: TextView = findViewById(R.id.edit_text_transaction_message)
        val allowTransaction: SwitchCompat = findViewById(R.id.allow_transaction_confirmation)

        allowTransaction.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transactionMessage.text = reasons.random()
            } else {
                transactionMessage.text = ""
            }
        }

        val url = Url(relyingPartyUrl)
        relyingPartyUrlTextView.text = url.hostWithPort
        nicknameTextView.text = nickName
        createdAtTextView.text = createdAt

        initiateAuthentication.setOnClickListener {

            lifecycleScope.launch {

                fido2Api.initiateAssertion(
                    assertionOptionsUrl = "$relyingPartyUrl/assertion/options",
                    authorization = "Bearer $accessToken",
                    AssertionOptions(userName, "preferred")
                )
                    .onSuccess {
                        sendAssertionOptionsResult(
                            it, relyingPartyUrl,
                            accessToken,
                            allowTransaction.isChecked,
                            transactionMessage.text.toString()
                        )
                    }

                    .onFailure {
                        print("Failure: $it.message")
                        displayError("${it.message}")
                    }
            }

        }

        removeAuthenticator.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Remove authenticator")
                .setMessage("Remove FIDO authenticator? If you proceed, you will need to remove the authenticator from the relying party.")
                .setPositiveButton("Remove") { _, _ ->
                    with(sharedPreferences.edit()) {
                        remove("accessToken")
                        remove("relyingPartyUrl")
                        remove("nickName")
                        remove("userName")
                        remove("email")
                        remove("displayName")
                        remove("createdAt")
                        remove("keyName")
                        apply()
                    }

                    KeystoreHelper.deleteKeyPair(keyName)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    private suspend fun sendAssertionOptionsResult(
        publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions,
        relyingPartyUrl: String,
        accessToken: String,
        allowTransaction: Boolean,
        transactionMessage: String
    ) {

        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("FIDO2 Demo")
            .setSubtitle(transactionMessage)
            .setNegativeButtonText("Cancel")

        var flags: Byte
        val message: String?

        if (allowTransaction) {
            flags = 0x01.toByte() // userPresence (UP)
            flags = (flags or 0x04)  // userVerification (UV)
            flags = (flags or 0x80.toByte())  // extensionData (ED)
            message = transactionMessage
        } else {
            flags = 0x01 // userPresence (UP)
            flags = (flags or 0x04)  // userVerification (UV)
            message = null
        }

        val authenticatorAssertionResponse: AuthenticatorAssertionResponse =
            fido2Api.buildAuthenticatorAssertionResponse(
                this@AuthenticationActivity,
                ContextCompat.getMainExecutor(this@AuthenticationActivity),
                promptInfoBuilder,
                keyName,
                flags,
                publicKeyCredentialRequestOptions,
                message
            )

        fido2Api.sendAssertion(
            assertionResultUrl = "$relyingPartyUrl/assertion/result",
            authorization = "Bearer $accessToken",
            authenticatorAssertionResponse
        )
            .onSuccess { assertionResultResponse ->
                println("Success: $assertionResultResponse")

                withContext(Dispatchers.Main) {
                    val intent = Intent(
                        this@AuthenticationActivity,
                        AuthenticationResultActivity::class.java
                    )

                    intent.putExtra(
                        "assertionResultResponse",
                        Json.encodeToString(assertionResultResponse)
                    )
                    startActivity(intent)
                }
            }

            .onFailure {
                println("Failure: ${it.message}")
                displayError("${it.message}")
            }

    }

    private fun displayError(error: String) {
        runOnUiThread {
            val errorDialogBuilder = AlertDialog.Builder(
                this@AuthenticationActivity
            )
            errorDialogBuilder.setTitle("Failure")
            errorDialogBuilder.setMessage(error)
            errorDialogBuilder.setCancelable(false)
            errorDialogBuilder.setPositiveButton("Ok", null)

            val errorDialog = errorDialogBuilder.create()
            errorDialog.show()

            val mPositiveButton = errorDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            mPositiveButton.setOnClickListener {
                errorDialog.cancel()
            }
        }
    }
}