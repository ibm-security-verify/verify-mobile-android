package com.ibm.security.verifysdk.authentication.demoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ibm.security.verifysdk.authentication.CodeChallengeMethod
import com.ibm.security.verifysdk.authentication.PKCEHelper
import com.ibm.security.verifysdk.authentication.api.OAuthProvider
import com.ibm.security.verifysdk.authentication.demoapp.ui.theme.IBMSecurityVerifySDKTheme
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

class MainActivity : ComponentActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val clientId = "bcc767c7-9a18-4059-9949-7f1f23a76779"
    private val host = "sdk.verify.ibm.com"
    private val issuer = "https://$host/v1.0/endpoint/default/authorize"
    private val redirect = "https://$host/callback"
    private val tokenEndpoint = "https://$host/v1.0/endpoint/default/token"
    private val oAuthProvider = OAuthProvider(clientId = clientId)
    private val codeVerifier = PKCEHelper.generateCodeVerifier()
    private val codeChallenge = PKCEHelper.generateCodeChallenge(codeVerifier)
    private val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IBMSecurityVerifySDKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        coroutineScope.async {
            oAuthProvider.authorizeWithBrowser(
                URL(issuer),
                redirect,
                codeChallenge = codeChallenge,
                method = CodeChallengeMethod.S256,
                scope = arrayOf("openid"),
                state = "init",
                activity = this@MainActivity
            ).onSuccess { code ->
                log.info("--> Authorization code: $code")
                oAuthProvider.authorize(
                    httpClient = NetworkHelper.getInstance,
                    url = URL(tokenEndpoint),
                    redirectUrl = URL(redirect),
                    code,
                    codeVerifier
                )
                    .onSuccess { token ->
                        log.info("--> Token: $token")
                    }
                    .onFailure {
                        log.error("--> Error: $it")
                    }
            }
                .onFailure {
                    log.error("--> Error: $it")
                }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IBMSecurityVerifySDKTheme {
        Greeting("Android")
    }
}