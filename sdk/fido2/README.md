# IBM Security Verify FIDO2™ SDK for Android

The FIDO2 software development kit (SDK) is a native implementation of attestation and assertion ceremonies.  Essentially providing the equivalent
of WebAuthn's `navigator.credentials.create()` and `navigator.credentials.get()` for native mobile apps.  The FIDO2 SDK supports custom certificate attestation and authenticator extensions.

The SDK generates and parses FIDO2 conform data and is therefore not tied to the IBM ecosystem, but can communicate with any FIDO2 server.

In contrast to other FIDO2 SDK, especially the official [FIDO2 API for Android](https://developers.google.com/identity/fido/android/native-apps), this SDK has the following differences:
- Support of `txAuthSimple` extension
- Only supports ES256 keys
- Digital Asset Links (`assetlinks.json`) is not required


## Example
An [example](../../examples/fido2) application is available for the FIDO2 SDK.

## Getting started

### Installation

1. Download all files from [Releases](./releases/latest) for the `core` and `fido2` SDKs.

1. Install the SDKs to your local Maven repository (usually it's `~/.m2`):

```
SDK_VERSION = 3.0.1

### The name of the SDK: core | fido2
SDK = core 

### Install binary and pom file
mvn install:install-file -Dfile=$SDK-$SDK_VERSION.aar -DgroupId=com.ibm.security.verifysdk -DartifactId=$SDK -Dversion=$SDK_VERSION -Dpackaging=aar -DpomFile=$SDK-$SDK_VERSION.pom

### Install javadoc
mvn install:install-file -Dfile=$SDK-$SDK_VERSION-javadoc.jar -DgroupId=com.ibm.security.verifysdk -DartifactId=$SDK -Dversion=$SDK_VERSION -Dpackaging=jar -Dclassifier=javadoc

### Install sources
mvn install:install-file -Dfile=$SDK-$SDK_VERSION-sources.jar -DgroupId=com.ibm.security.verifysdk -DartifactId=$SDK -Dversion=$SDK_VERSION -Dpackaging=jar -Dclassifier=sources
``` 

### API documentation
The FIDO2 SDK API can be reviewed [here](https://ibm-security-verify.github.io/android/fido2/docs).

### Importing the SDK

1. Add your local Maven repository to the list of repositories in `settings.gradle`:
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}
```

1. Add the libraries to the dependencies list in `build.gradle` (app):
```gradle
dependencies {
    implementation("com.ibm.security.verifysdk:core:3.0.1")
    implementation("com.ibm.security.verifysdk:fido2:3.0.1")
    ...
}
```

Sync project with Gradle files. 


## Usage

For convenience, the SDK provides a network request handler. Alternativley, you can use your own handler and use the SDK to create and parse the data.

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

Pass the activity context, the dialog builder, the `publicKeyCredentialCreationOptions` from the previous network request and few other parameters to generate the `AuthenticatorAttestationResponse`. Due to the authentication dialog, this call needs to be wrapped in a coroutine.
```Kotlin
lifecycleScope.launch {
    val authenticatorAssertionResponse: AuthenticatorAttestationResponse =
        fido2Api.buildAuthenticatorAttestationResponse(
            this@RegistrationActivity,
            ContextCompat.getMainExecutor(this@RegistrationActivity),
            promptInfoBuilder,
            "6DC9F22D-2C0A-4461-B878-DE61E159EC61",
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
    attestationResultUrl = "$rpUrl/attestation/result",
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
    flags = 0x01.toByte() // userPresence (UP)
    flags = (flags or 0x04)  // userVerification (UV)
    flags = (flags or 0x80.toByte())  // extensionData (ED)
    message = transactionMessage
} else {
    flags = 0x01 // userPresence (UP)
    flags = (flags or 0x04)  // userVerification (UV)
    message = null
}
```

Pass the activity context, the dialog builder, the `publicKeyCredentialRequestOptions` from the previous network request and few other parameters to generate the `AuthenticatorAssertionResponse`. Due to the authentication dialog, this call needs to be wrapped in a coroutine.
```Koltin
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

## License
This package contains code licensed under the MIT License (the "License"). You may view the License in the [LICENSE](../../LICENSE) file within this package.
<br/><br/>
FIDO™ and FIDO2™  are  trademarks (registered in numerous countries) of FIDO Alliance, Inc. 
