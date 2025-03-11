# Module IBM Security Verify FIDO2(TM SDK pour Android

Le SDK IBM Security Verify FIDO2™ pour Android est une implémentation native des cérémonies d'attestation et d'assertion FIDO
cérémonies d'assertion. Le SDK fournit essentiellement l'équivalent de
 `navigator.credentials.create()` et `navigator.credentials.get()` de WebAuthn's pour les applications mobiles natives
avec des exigences de sécurité spécifiques. Il se distingue nettement du
Google [FIDO2 API pour Android](https://developers.google.com/identity/fido/android/native-apps),
de la manière suivante :

- Le SDK Verify FIDO2 pour Android crée toujours des clés ES256 liées à l'appareil à l'aide de l'API Android
    [KeyPairGenerator API](https://developer.android.com/reference/kotlin/java/security/KeyPairGenerator.html).
   Le SDK Verify FIDO2 pour Android s'intègre à la gestion des justificatifs d'identité de la plateforme et permet
   la création d'informations d'identification, y compris d'informations d'identification synchronisées dans n'importe quel fournisseur d'informations d'identification enregistré.
- Le SDK Verify FIDO2 pour Android permet l'intégration d'extensions telles que l'application `txAuthSimple`
   qui facilite les opérations de type "ce que vous voyez est ce que vous signez". L'API FIDO2 pour Android
   sert uniquement à l'authentification et ne permet pas de modifier le texte affiché dans les boîtes de dialogue d'authentification du système
   d'authentification du système.
- Le SDK Verify FIDO2 pour Android permet d'intégrer votre propre mise en œuvre de l'attestation si vous le souhaitez
   si vous le souhaitez. L'API FIDO2 pour Android s'appuie actuellement sur Google Play Store pour fournir une
   attestation. Cette fonction n'est pas toujours disponible sur tous les appareils Android.

Ce SDK Verify FIDO2 pour Android est bien adapté aux développeurs d'applications mobiles purement natives
qui souhaitent fournir uniquement des clés liées à l'appareil dans des scénarios où l'utilisation de clés d'accès synchronisées
synchronisées, par exemple, n'est pas appropriée.

## Exemple

Un [exemple d'application](https://github.com/ibm-security-verify/verify-sdk-android/tree/main/sdk/examples/fido2)
est disponible pour le SDK Verify FIDO2 pour Android.

## Utilisation

Pour plus de commodité, le SDK fournit un gestionnaire de requêtes réseau. Vous pouvez également utiliser votre propre
et utiliser le SDK pour créer et analyser les données.

### Attestation

#### Initier l'attestation

Pour obtenir les options d'attestation, effectuez une requête HTTPS auprès d'une partie se fiant à la loi
 `POST <server>/attestation/options`. L'appel doit être exécuté dans une coroutine.

```Kotlin
val fido2Api = Fido2Api()

lifecycleScope.launch {
    fido2Api.initiateAttestation(
        attestationOptionsUrl = "$relyingPartyUrl/attestation/options",
        authorization = accessToken,
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

#### Envoyer une demande d'attestation

Créez une demande d'attestation en utilisant le site `PublicKeyCredentialCreationOptions` de la section précédente
précédente.

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

Transmettre le contexte de l'activité, le générateur de dialogue, le `publicKeyCredentialCreationOptions` de la demande de réseau précédente et d'autres paramètres pour générer le
demande de réseau précédente et d'autres paramètres pour générer le `AuthenticatorAttestationResponse`.
En raison du dialogue d'authentification, cet appel doit être intégré dans une coroutine.

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

Envoyez l'adresse `AuthenticatorAttestationResponse` au point de terminaison `POST <server>/attestation/result` :

```Kotlin
fido2Api.sendAttestation(
    attestationResultUrl = "$relyingPartyUrl/attestation/result",
    authorization = accessToken,
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

#### Initier l'affirmation

Pour obtenir des options d'assertion, effectuez une requête HTTPS à un point de terminaison de la partie se fiant à l'information (relying party)
 `POST <server>/assertion/options` avec `userVerification = preferred`.

```Kotlin
lifecycleScope.launch {
    fido2Api.initiateAssertion(
        assertionOptionsUrl = "$relyingPartyUrl/assertion/options",
        authorization = accessToken,
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

#### Envoyer une demande d'affirmation

Créez un site `AuthenticatorAssertionResponse` en utilisant le site `PublicKeyCredentialRequestOptions` de la section précédente
section précédente.

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

Transmettre le contexte de l'activité, le générateur de dialogue, le `publicKeyCredentialRequestOptions` de la demande de réseau précédente et d'autres paramètres pour générer le
demande de réseau précédente et d'autres paramètres pour générer le `AuthenticatorAssertionResponse`. En raison du dialogue d'authentification, cet appel doit être enveloppé dans une coroutine
du dialogue d'authentification, cet appel doit être enveloppé dans une coroutine.

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

Envoyez l'adresse `authenticatorAssertionResponse` au point de terminaison `POST <server>/assertion/result` :

```Kotlin
lifecycleScope.launch {
    fido2Api.sendAssertion(
        assertionResultUrl = "$relyingPartyUrl/assertion/result",
        authorization = accessToken,
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

### Licence

FIDO™ et FIDO2™ sont des marques (déposées dans de nombreux pays) de FIDO Alliance, Inc.

# Paquet com.ibm.security.verifysdk.fido2

<!-- v2.3.7 : caits-prod-app-gp_webui_20241231T140325-8_en_fr -->