/*
 *  Copyright contributors to the IBM Security Verify FIDO2 Sample App for Android project
 */
package com.ibm.security.verifysdk.fido2.demoapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ibm.security.verify.fido2.model.AssertionResultResponse
import io.ktor.util.decodeBase64Bytes
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


class AuthenticationResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_result)

        intent.getStringExtra("assertionResultResponse")?.let {

            val assertionResultResponse = Json.decodeFromString<AssertionResultResponse>(it)

            val credentialData =
                assertionResultResponse.additionalData?.get("attributes")?.jsonObject?.get("credentialData")?.jsonObject
            val fidoLoginDetails =
                credentialData?.getValue("fidoLoginDetails")?.jsonPrimitive?.content.orEmpty()
            val fidoLoginDetailsJson = Json.parseToJsonElement(fidoLoginDetails).jsonObject

            assertionResultResponse.additionalData?.get("user")?.jsonObject?.getValue("name")?.jsonPrimitive?.content?.let { userName ->
                findViewById<TextView>(R.id.text_view_username).text = userName
            }

            credentialData?.getValue("displayName")?.jsonPrimitive?.content?.let { displayName ->
                findViewById<TextView>(R.id.text_view_display_name).text = displayName
            }

            credentialData?.getValue("email")?.jsonPrimitive?.content?.let { email ->
                findViewById<TextView>(R.id.text_view_email).text = email
            }

            credentialData?.getValue("AUTHENTICATOR_ICON")?.jsonPrimitive?.content?.let { image ->
                val decodedString: ByteArray =
                    image.substring(image.indexOf(",") + 1).decodeBase64Bytes()
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(decodedByte)
            }

            fidoLoginDetailsJson["requestData"]?.jsonObject?.get("registration")?.jsonObject?.get("nickname")?.jsonPrimitive?.let { nickname ->
                findViewById<TextView>(R.id.text_view_nickname).text = nickname.content
            }

            fidoLoginDetailsJson["requestData"]?.jsonObject?.get("authData")?.jsonObject?.get("extensions")
                ?.let { extensions ->
                    extensions.jsonObject?.getValue("txAuthSimple")?.jsonPrimitive?.let { txAuthSimple ->
                        println("Transaction: $txAuthSimple")
                        findViewById<TextView>(R.id.text_view_transaction).text =
                            txAuthSimple.content
                    }
                }
        }

        findViewById<Button>(R.id.button_close).setOnClickListener {
            startActivity(
                Intent(
                    this@AuthenticationResultActivity,
                    AuthenticationActivity::class.java
                )
            )
        }
    }
}