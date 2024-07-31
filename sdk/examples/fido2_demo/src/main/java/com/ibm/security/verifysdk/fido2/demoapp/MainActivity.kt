/*
 *  Copyright contributors to the IBM Security Verify FIDO2 Sample App for Android project
 */
package com.ibm.security.verifysdk.fido2.demoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ibm.security.verifysdk.core.KeystoreHelper
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.fido2.Fido2Api
import okhttp3.logging.HttpLoggingInterceptor


class MainActivity : AppCompatActivity() {

    private val keyName = "61683285-900f-4bed-87e0-b83b5277ba93"
    private val fido2Api = Fido2Api()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkHelper.customLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        if (KeystoreHelper.exists(keyName).not()) {
            fido2Api.createKeyPair(
                keyName = keyName,
                authenticationRequired = true,
                invalidatedByBiometricEnrollment = false
            )
        }

        findViewById<Button>(R.id.button_get_started_isva).setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        NetworkHelper.initialize()
        redirectToAuthenticationIfLoggedIn()
    }

    private fun redirectToAuthenticationIfLoggedIn() {
        val sharedPreferences =
            getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

        val accessToken = sharedPreferences.getString("accessToken", "") ?: ""
        if (accessToken.isNotEmpty()) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
    }
}