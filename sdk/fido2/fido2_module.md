# Module FIDO2 SDK for Android

The FIDO2 software development kit (SDK) is a native implementation of attestation and assertion ceremonies.  Essentially providing the equivalent
of WebAuthn's `navigator.credentials.create()` and `navigator.credentials.get()` for native mobile apps.  The FIDO2 SDK supports custom certificate attestation and authenticator extensions.

In contrast to the [FIDO2 API for Android](https://developers.google.com/identity/fido/android/native-apps), our SDK has the following differences:
- Exclusive support for ES256 keys ensures heightened cryptographic security.
- Seamless integration of the `txAuthSimple` extension enhances authentication capabilities.
- Elimination of the necessity for a Digital Asset Links file (`assetlinks.json`) streamlines deployment processes and enhances overall flexibility.


## Example
An [example](https://github.com/ibm-security-verify/verify-sdk-android/tree/main/sdk/examples/fido2) application is available for the FIDO2 SDK.

## Usage

For convenience, the SDK provides a network request handler. Alternatively, you can use your own handler and use the SDK to create and parse the data.

### Attestation

#### Initiate attestation

To get the attestation options, perform a HTTPS request to a relying party endpoint  `POST <server>/attestation/options`. The call needs to be executed in a coroutine.

```Kotlin
val fido2Api = Fido2Api()

lifecycleScope.launch {
    fido2Api.initiateAttestation(
        attestationOptionsUrl = "$relyingPartyUrl/attestation/options",
        authorization = "Bearer $accessToken",
        AttestationOptions(displayName = "Thomas J. Watson")
    )
        .onSuccess { publicKeyCredentialCreationOptions ->
            println("Success: $publicKeyCredentialCreationOptions")
            // handle PublicKeyCredentialCreationOptions
        }   
        .onFailure {
            println("Failure: $it.message")
        }
}
```

#### Send attestation request

Create an attestation request using the `PublicKeyCredentialCreationOptions` from the previous section.

```Kotlin
// Construct builder for the biometric authentication dialog
val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("FIDO2 Demo")
            .setSubtitle("User Verification")
            .setNegativeButtonText("Cancel")

// Define flags for Attestation Credential Data
var flags: Byte = 0x01  // userPresence (UP)
flags = (flags or 0x04) // userVerification (UV)
flags = (flags or 0x40) // attestedCredentialData (AT)
```

Pass the activity context, the dialog builder, the `publicKeyCredentialCreationOptions` from the previous network request and other parameters to generate the `AuthenticatorAttestationResponse`. Due to the authentication dialog, this call needs to be wrapped in a coroutine.
```Kotlin
lifecycleScope.launch {
    val authenticatorAssertionResponse: AuthenticatorAttestationResponse =
        fido2Api.buildAuthenticatorAttestationResponse(
            this@RegistrationActivity,
            ContextCompat.getMainExecutor(this@RegistrationActivity),
            promptInfoBuilder,
            "ABCDEFGH-1234-5678-IJKL-MNOPQRSTUVWX",
            keyName,
            flags,
            publicKeyCredentialCreationOptions,
            nickName
        )
}
```

Send the `AuthenticatorAttestationResponse` to the `POST <server>/attestation/result` endpoint:
```Kotlin
fido2Api.sendAttestation(
    attestationResultUrl = "$relyingPartyUrl/attestation/result",
    authorization = "Bearer $accessToken",
    authenticatorAssertionResponse
)
    .onSuccess { attestationResultResponse ->
        println("Success: $attestationResultResponse")
        // handle AttestationResultResponse
    }
    .onFailure {
        println("Failure: $it.message")
    }
```

### Assertion

#### Initiate assertion

To get assertion options, perform a HTTPS request to a relying party endpoint `POST <server>/assertion/options` with `userVerification = preferred`.

```Kotlin
lifecycleScope.launch {
    fido2Api.initiateAssertion(
        assertionOptionsUrl = "$relyingPartyUrl/assertion/options",
        authorization = "Bearer $accessToken",
        AssertionOptions(userName, "preferred")
    )
        .onSuccess { publicKeyCredentialRequestOptions ->
            println("Success: $publicKeyCredentialRequestOptions")
            // handle PublicKeyCredentialRequestOptions
        }

        .onFailure {
            println("Failure: $it.message")
        }
}
```

#### Send assertion request

Create an `AuthenticatorAssertionResponse` using the `PublicKeyCredentialRequestOptions` from the previous section.

```Kotlin
// Construct builder for the biometric authentication dialog
val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("FIDO2 Demo")
            .setSubtitle(transactionMessage)
            .setNegativeButtonText("Cancel")

// Define flags for Attestation Credential Data
var flags: Byte
val message: String?

// Add transaction message if enabled
if (allowTransaction) {
    flags = 0x01.toByte()               // userPresence (UP)
    flags = (flags or 0x04)             // userVerification (UV)
    flags = (flags or 0x80.toByte())    // extensionData (ED)
    message = transactionMessage
} else {
    flags = 0x01            // userPresence (UP)
    flags = (flags or 0x04) // userVerification (UV)
    message = null
}
```

Pass the activity context, the dialog builder, the `publicKeyCredentialRequestOptions` from the previous network request and other parameters to generate the `AuthenticatorAssertionResponse`. Due to the authentication dialog, this call needs to be wrapped in a coroutine.
```Kotlin
lifecycleScope.launch {
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
}
```

Send the `authenticatorAssertionResponse` to the `POST <server>/assertion/result` endpoint:
```Kotlin
lifecycleScope.launch {
    fido2Api.sendAssertion(
            assertionResultUrl = "$relyingPartyUrl/assertion/result",
            authorization = "Bearer $accessToken",
            authenticatorAssertionResponse
        )
            .onSuccess { assertionResultResponse ->
                println("Success: $assertionResultResponse")
                // handle AssertionResultResponse
            }

            .onFailure {
                println("Failure: ${it.message}")
            }
}
```

# Package com.ibm.security.verifysdk.fido2
