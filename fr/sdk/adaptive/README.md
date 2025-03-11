# IBM Security Verify Adaptive SDK pour Android

![Version SDK](https://img.shields.io/badge/IBM%20Security%20Verify%20Adaptive%20SDK-3.0.0-blue.svg)
![Version Android](https://img.shields.io/badge/Android-12-green.svg)
![Version Android](https://img.shields.io/badge/Android%20API-31-green.svg)

La composante adaptative permet d'évaluer l'appareil. Les défis en matière d'authentification et d'autorisation peuvent être évalués sur la base des politiques de risque du nuage.

## Mise en route

### Prérequis

- Installer et configurer le
    [Proxy SDK](https://github.com/IBM-Security/adaptive-proxy-sdk-javascript) sur un serveur Node en exécutant `npm install adaptive-proxy-sdk`

- Générer et télécharger le SDK Trusteer via le portail d'administration IBM Security Verify pour l'application.

Voir [Embarquer une application native](https://docs.verify.ibm.com/verify/docs/on-boarding-a-native-application)

### Télécharger

#### Maven Central
Ajoutez cette ligne pour chaque composant à votre fichier `build.gradle` (au niveau de l'application):

    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:3.0.0'

#### Jitpack
Dans le fichier build.gradle de votre projet, ajoutez JitPack comme dépôt :

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
            }
        }

Ensuite, pour chaque SDK, ajoutez la ligne suivante dans votre fichier build.gradle au niveau de l'application :

    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:v3.0.0'

Remplacez le SDK particulier - `core` dans cet exemple - par le nom des SDK que vous souhaitez intégrer.

Vous pouvez ensuite utiliser le site web de JitPack https://jitpack.io/ pour générer une déclaration de compilation, basée sur l'url GitHub du SDK.

#### Manuellement à partir de la version GitHub
Téléchargez les fichiers appropriés sur https://github.com/ibm-security-verify/verify-sdk-android/releases copiez-les et collez-les dans le dossier lib et synchronisez votre projet.


### Documentation sur les API
L'API des composants adaptatifs peut être consultée [ici.](https://ibm-security-verify.github.io/android/adaptive/docs/)


### Paramètres de configuration de Trusteer

Pour lancer une analyse de la collecte des dispositifs, vous devez initialiser une structure `TrusteerCollectionService`.  Cette structure fait partie du zip Trusteer que vous pouvez obtenir via la configuration de votre locataire ou via le [portail IBM Security Verify Developer Portal.](https://www.ibm.com/docs/en/security-verify?topic=applications-accessing-security-verify-developer-portal)  Le zip Trusteer comprendra également vos `vendorId`, `clientId` et `clientKey`.


## Utilisation

### Démarrer le service de collecte
Pour commencer la collecte, une instance `TrusteerAdaptiveCollection` est assignée à `AdaptiveContext.collectionService`.

```kotlin
/** The vendor collection to be assigned to [AdaptiveContext.collectionService]. */
val trusteerCollection = TrusteerAdaptiveCollection(
    vendorId = "<your_vendor_id>",
    clientId = "<your_client_id>",
    clientKey = "<your_client_key>"
)

AdaptiveContext.collectionService = trusteerCollection
AdaptiveContext.start(targetContext)
```

### Arrêter le service de collecte
```kotlin
// Stop the collection process.
AdaptiveContext.stop()
```

### Mise en œuvre de AdaptiveDelegate

Le protocole `AdaptiveDelegate` doit être mis en œuvre afin d'exposer les fonctions `assessment`, `generate` et `evaluate`.

```kotlin
// Implementing the `AdaptiveDelegate` interface as a singleton
object Adaptive: AdaptiveDelegate {

  // Implement the `assessment` function
  override fun assessment(sessionId: String, evaluationContext: String, completion: (Result<AdaptiveResult>) -> Unit) {
    // Send a request to the server to perform risk assessment for the given session ID using the Proxy SDK.
  }

  // Implement the `generate` method
  override fun generate(factor: AssessmentFactor, transactionId: String, completion: (Result<OtpGenerateResult?>) -> Unit) {
    // Send a request to the server to generate a verification for the given `factor` using the Proxy SDK.
  }

  // Implement the `evaluate` method
  override fun evaluate(evaluation: FactorEvaluation, evaluationContext: String, completion: (Result<AdaptiveResult>) -> Unit) {
    // Send a request to the server to evaluate a verification for the given `FactorEvaluation` using the Proxy SDK.
  }
}
```

### Effectuer une évaluation des risques

L'objectif de la fonction `assessment` est de lancer une évaluation des risques via le [Proxy SDK](https://github.com/IBM-Security/adaptive-sdk-javascript). La mise en œuvre de la fonction `assessment` doit envoyer une demande au Proxy SDK.

À la réception de la demande, le serveur doit appeler la fonction
[`assess`](https://github.com/IBM-Security/adaptive-sdk-javascript/tree/develop#assess-a-policy) et réagir en conséquence.

Une fois la réponse reçue, elle peut être classée dans l'une des structures suivantes : `AllowAssessmentResult`, `DenyAssessmentResult` ou `RequiresAssessmentResult`.

```kotlin
  // Perform risk assessment
  Adaptive.assessment(AdaptiveContext.sessionId, evaluationContext = "profile") { result ->
    // Error during assessment
    result.onFailure { println("Error: ${it.message}") }

    // Successful assessment
    result.onSuccess {
      when(it) {
        is AllowAssessmentResult -> { /* `allow` result */ }
        is RequiresAssessmentResult -> { /* `requires` result */ }
        else -> { /* `deny` result */ }
      }
    }
  }
```

### Effectuer une génération de facteurs

La fonction `generate` permet de générer une vérification `AssessmentFactor` via le [Proxy SDK](https://github.com/IBM-Security/adaptive-sdk-javascript).

L'implémentation de cette fonction doit envoyer une requête à un serveur utilisant le Proxy SDK. À la réception de la demande, le serveur doit appeler la fonction de proxy SDK [`generateEmailOTP`](https://github.com/IBM-Security/adaptive-sdk-javascript/tree/develop#generate-an-email-otp-verification) ou [`generateSMSOTP`](https://github.com/IBM-Security/adaptive-sdk-javascript/tree/develop#generate-an-sms-otp-verification) du Proxy SDK. La méthode à appeler doit correspondre à un type `AssessmentFactor` de la propriété factor. En général, le serveur ne répond pas après avoir effectué ces vérifications.

Les sites `AssessmentFactor` actuellement pris en charge pour la génération sont `Factor.EMAIL_OTP` et `Factor.SMS_OTP`.

```kotlin
  // Create a `AssessmentFactor` instance
  val assessmentFactor = AllowedFactor(Factor.SMS_OTP)

  // Generate verification
  // (The `transactionId` is received from the `assessment` method on a `requires` status.)
  Adaptive.generate(assessmentFactor, transactionId) { result ->
    // Error during generation
    result.onFailure { println("Couldn't generate SMS OTP.") }

    // Successful generation
    result.onSuccess { /* SMS OTP successfully sent, correlation received. */ }
  }
```

### Effectuer une évaluation des facteurs

L'implémentation de cette fonction doit envoyer une requête à un serveur utilisant le Proxy SDK. À la réception de la demande, le serveur doit appeler la fonction [`evaluateUsernamePassword`](https://github.com/IBM-Security/adaptive-sdk-javascript/tree/develop#evaluate-a-username-password-verification)
ou [`evaluateOTP`](https://github.com/IBM-Security/adaptive-sdk-javascript/tree/develop#evaluate-an-otp-verification)
et réagir en conséquence. La méthode à appeler doit dépendre de l'instance de `FactorEvaluation` (soit
`PasswordEvaluation` ou `OneTimePasscodeEvaluation`.

Une fois la réponse reçue, elle peut être classée dans l'une des catégories suivantes : `AllowAssessmentResult`, `DenyAssessmentResult` ou
`RequiresAssessmentResult` à transmettre à la fonction `completion`.

```kotlin
  // Create a `FactorEvaluation` instance
  // (The `transactionId` is received from the `assessment` method on a `requires` status.)
  val passwordEvaluation = PasswordEvaluation(transactionId = transactionId, username = "username", password = "password")

  // Evaluate a factor verification
  Adaptive.evaluate(passwordEvaluation, evaluationContext = "profile") { result ->
    // Error during evaluation
    result.onFailure { println("Couldn't evaluate username/password.") }

    // Successful evaluation
    result.onSuccess {
      when(it) {
        is AllowAssessmentResult -> { /* `allow` result */ }
        else -> { /* `deny` result */ }
      }
    }
  }
```

## Licence
Ce paquet contient du code sous licence MIT (la "Licence"). Vous pouvez consulter la licence dans le fichier [LICENSE](../../LICENSE) de ce paquet.

<!-- v2.3.7 : caits-prod-app-gp_webui_20241231T140338-2_en_fr -->