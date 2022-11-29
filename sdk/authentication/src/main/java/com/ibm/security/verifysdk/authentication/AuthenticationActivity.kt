//
// Copyright contributors to the IBM Security Verify Authentication SDK for Android project
//
package com.ibm.security.verifysdk.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

/**
 * 'Proxy' activity to handle the attempt to get an authorization code. The activity
 * is started by [OAuthProvider] and launches Chrome Custom Tabs (CCT) to initiate the authorization
 * code (AZN) flow using Proof Key for Code Exchange (PKCE).
 *
 * Upon successful user authentication, the authorization code is extracted from the redirect and
 * returned to the calling activity. In case of an error or when the user has dismissed CCT, an
 * exception is returned.
 *
 * @since 3.0.0
 */
internal class AuthenticationActivity : AppCompatActivity() {

    private val builder = CustomTabsIntent.Builder()
    private var url: String = ""
    private var code: String = ""
    private var hasAuthenticationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra("url")?.let {
            url = it
        }
        builder.setShowTitle(true)

        val customTabsIntent = builder.build()
        customTabsIntent.intent.data = Uri.parse(url)
        customTabsIntent.intent.putExtra(
            Intent.EXTRA_REFERRER,
            Uri.parse("android-app://" + applicationContext.packageName)
        )

        val getCode = this.activityResultRegistry.register(
            "code",
            ActivityResultContracts.StartActivityForResult()
        ) { }
        getCode.launch(customTabsIntent.intent)
    }

    override fun onResume() {
        super.onResume()

        if (hasAuthenticationStarted) {
            if (code.isNotEmpty()) {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("code", code)
                })
            } else {
                setResult(RESULT_CANCELED, Intent())
            }
            finish()
        } else {
            hasAuthenticationStarted = true
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.getQueryParameter("code")?.let {
            code = it
        }
    }
}