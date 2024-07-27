package com.ibm.security.verifysdk.dpop.demoapp

import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.ibm.security.verifysdk.dpop.demoapp.ui.theme.DPoPDemoTheme
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.lang.JoseException
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAKeyGenParameterSpec
import java.util.Base64
import java.util.Locale

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "DPoP-Demo"
        const val RSA_KEY_NAME = "rsa-dpop-demo-key.com.ibm.security.verifysdk.dpop"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }

    private lateinit var networkHelper: NetworkHelper
    private lateinit var networkHelperCustomSSL: NetworkHelper
    private lateinit var dpopToken: DpopToken
    private lateinit var keyStore: KeyStore

    // Change these parameters according to your IBM Security Verify tenant
    private val tenant = "verify.ice.ibmcloud.com" // without protocol
    private val clientId = "b29c2bc2-a7ae-4cf7-b437-46d28f7a0121"
    private val clientSecret = "ZXBpYHG4gm"
    private val resourceServer = "192.168.42.23" // without protocol and port
    private val resourceServerPort = "8080"

    private val tokenEndpoint = String.format("https://%s/oauth2/token", tenant)
    private val resourceEndpoint =
        String.format("https://%s:%s/validate-token", resourceServer, resourceServerPort)

    private val accessToken = MutableLiveData("...")
    private val tokenValidation = MutableLiveData("...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DPoPDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Config()
                }
            }
        }

        networkHelper = NetworkHelper()
        networkHelperCustomSSL = NetworkHelper(resourceServer)
        keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
    }

    @Composable
    fun Config() {

        val token: String? by accessToken.observeAsState()
        val validation: String? by tokenValidation.observeAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
        )
        {

            Text(
                modifier = Modifier
                    .padding(all = 40.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Sample app to demonstrate the use of DPoP token",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                singleLine = true,
                value = tokenEndpoint,
                enabled = false,
                onValueChange = {},
                label = { Text("Token Endpoint") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                singleLine = true,
                value =  resourceEndpoint,
                enabled = false,
                onValueChange = {},
                label = { Text("Resource Endpoint") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                singleLine = true,
                value =  clientId,
                enabled = false,
                onValueChange = {},
                label = { Text("Client ID") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                singleLine = true,
                value =  clientSecret,
                enabled = false,
                onValueChange = {},
                label = { Text("Client secret") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                singleLine = true,
                value =  token ?: "...",
                enabled = false,
                onValueChange = {},
                label = { Text("Access token") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                singleLine = true,
                value =  validation ?: "...",
                enabled = false,
                onValueChange = {},
                label = { Text("Token validation") }
            )

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            )

            {
                Button(
                    onClick = {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) { requestDpopToken() }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 20.dp),
                    enabled = (token == "...")
                ) {
                    Text("Request DPoP token")

                }
                Button(
                    onClick = { }, // validateToken() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    enabled = (token != "..."),
                ) {
                    Text("Validate DPoP token")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ConfigPreview() {
        DPoPDemoTheme {
            Config()
        }
    }

    private fun getRsaSigningKey(): Key {

        if (keyStore.containsAlias(RSA_KEY_NAME)) {
            Log.d(TAG, "Key $RSA_KEY_NAME found in KeyStore")
        } else {
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                RSA_KEY_NAME,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            )
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
                .build()

            val keyPairGenerator =
                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE)
            keyPairGenerator.initialize(keyGenParameterSpec)
            Log.d(TAG, "Key $RSA_KEY_NAME generated")
            keyPairGenerator.generateKeyPair()
        }

        return keyStore.getKey(RSA_KEY_NAME, null)
    }

    private fun validateToken() {
        val headers = HashMap<String, String>()
        headers["DPoP"] = generateDpopHeader(
            htu = resourceEndpoint,
            htm = "GET",
            accessToken = dpopToken.accessToken
        )

//        apiServiceCustomSSL.validateDpopToken(
//            headers,
//            String.format("DPoP %s", dpopToken.accessToken),
//            resourceEndpoint
//        )
//            .enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    Log.d(TAG, String.format("Response code: %d", response.code()))
//                    if (response.isSuccessful) {
//                        Log.d(TAG, "DPoP token validation successful")
//                        tokenValidation.value =
//                            String.format(
//                                Locale.getDefault(),
//                                "%d - DPoP token validation successful",
//                                response.code()
//                            )
//                    } else {
//                        Log.d(TAG, "DPoP token validation failed")
//                        tokenValidation.value =
//                            String.format(
//                                Locale.getDefault(),
//                                "%d - DPoP token validation failed",
//                                response.code()
//                            )
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    throw (t)
//                }
//            })

    }

    private suspend fun requestDpopToken(): Result<DpopToken> {

        return try {
            val body = Parameters.build {
                append("client_id", clientId ?: "")
                append("client_secret", clientSecret ?: "")
                append("grant_type", "client_credentials")
                append("scope", "openid")
            }

            val response = networkHelper.client.post {
                url(tokenEndpoint)
                header(
                    "DPoP",
                    generateDpopHeader(htu = tokenEndpoint, htm = "POST", accessToken = null)
                )
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(body)
            }

            if (response.status.isSuccess()) {
                val token: DpopToken = response.body()
                accessToken.value = dpopToken.accessToken
                Result.success(token)
            } else {
                throw(Exception("HTTP error: ${response.status}"))
            }
        } catch (e: Exception) {
            throw(e)
        }
    }

    private fun generateDpopHeader(htu: String, htm: String, accessToken: String?): String {
        return try {
            val jwtClaims = JwtClaims()
            jwtClaims.setGeneratedJwtId()
            jwtClaims.setIssuedAtToNow()
            jwtClaims.setClaim("htm", htm)
            jwtClaims.setClaim("htu", htu)
            if (accessToken != null) {
                val bytes = accessToken.toByteArray(StandardCharsets.UTF_8)
                val messageDigest = MessageDigest.getInstance("SHA-256")
                messageDigest.update(bytes, 0, bytes.size)
                val digest = messageDigest.digest()
                val base64encodedFromDigest =
                    Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
                Log.d(TAG, "Token: $accessToken")
                Log.d(TAG, "Base64 encoded (digest): $base64encodedFromDigest")
                jwtClaims.setClaim("ath", base64encodedFromDigest)
            }
            val jws = JsonWebSignature()
            jws.payload = jwtClaims.toJson()
            jws.key = getRsaSigningKey()
            jws.algorithmHeaderValue = "RS256"
            jws.jwkHeader =
                RsaJsonWebKey(keyStore.getCertificate(RSA_KEY_NAME).publicKey as RSAPublicKey)
            jws.setHeader("typ", "dpop+jwt")
            val jwt: String = jws.compactSerialization
            Log.d(TAG, "JWT: $jwt")
            jwt
        } catch (e: JoseException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}