# IBM Verify Digital Credentials SDK for Android

The DC software development kit (SDK) provides functionality to support credential issuance and verifications in a mobile application.

## Getting started

### Overview

IBM Verify Identity Access enables businesses, governments, and individuals to issue, manage, and verify digital credentials with the Digital Credentials feature.
[IBM Verify Identity Access Digital Credentials configuration](https://www.ibm.com/docs/en/sva/11.0.0?topic=configuring-verify-identity-access-digital-credentials-configuration)

### Integrating with your project

See [here](../../README.md#integrating-with-your-project)

### API documentation

The Digital Credentials SDK API can be reviewed [here](https://ibm-verify.github.io/android/dc/docs/).

## Usage

This code below is from the [DC demo app](../examples/dc_demo/).

### Initializing a wallet

A wallet is the broker between a holder agent of a digital credential and other agents that may issue or request verification of a credential.

To initialize a wallet, your digital credentials service generates a QR code that contains specific endpoint information.  Ensure you include the
`Accept: image/png` and the `Authorization: Bearer <user_token>` headers.

```console
GET http://<hostname>/diagency/v1.0/diagency/.well-known/agency-configuration
```

```Kotlin
// Get access token
val oauthProvider = OAuthProvider(clientId = "abc123")
val token = oauthProvider.authorize(
        url = tokenUrl,
        username = "user",
        password = "password"
    ).getOrNull()

val qrScanResult = """
{
    "serviceBaseUrl": "https://sdk.verifyaccess.ibm.com/diagency",
    "oauthBaseUrl": "https://sdk.verifyaccess.ibm.com/oauth2"
}
"""

// Create the wallet provider
val walletProvider = WalletProvider(jsonData = qrScanResult)

// Initiate the wallet
val wallet = walletProvider.initiate(name = "John", username = "user", password = "password")

// Get a list of credentials types
wallet.credentials.forEach { 
    println(it.getTyp())
}
```

#### Persisting the wallet

The wallet holds credentials, invitations, connections and agent information.  

> NOTE: Invitation data is not retrieved when the wallet is first initialized.

The `Wallet` structure supports `Serializable` allowing the instance to be persisted to a storage model of your choosing. The following examples demonstrates using `Room` database and subsequently a `TypeConverter` to serialize and deserialize the `Wallet`.

##### Room database

```Kotlin
@Database(entities = [WalletEntity::class], version = 1)
@TypeConverters(WalletConverter::class)
abstract class DcDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    companion object {
        @Volatile
        private var INSTANCE: DcDatabase? = null

        fun getDatabase(
            context: Context
        ): DcDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    ...)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

##### Room database entity

```Kotlin
@Entity(tableName = "wallets")
data class WalletEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wallet: Wallet
)
```

##### Wallet entity type converter

```Kotlin
class WalletConverter {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun toString(wallet: Wallet?): String? {
        return wallet?.let { json.encodeToString(it) } // Convert Wallet to JSON String
    }

    @TypeConverter
    fun toWallet(walletJson: String?): Wallet? {
        return walletJson?.let { json.decodeFromString<Wallet>(it) } // Convert JSON String back to Wallet
    }
}
```

### Accepting a credential

A credential is offered to a holder agent which can be then added to the wallet.  Interactions with the wallet are performed using the `WalletService`.  The following example demonstrates the flow to preview and accept a credential.

```Kotlin
// Create a "WalletService" using the initialized wallet.
val walletService = WalletService(
    accessToken = walletEntity.wallet.token.accessToken,
    refreshUri = walletEntity.wallet.refreshUri,
    baseUri = walletEntity.wallet.baseUri,
    clientId = walletEntity.wallet.clientId
)
                
// Value from QR code scan.
val qrScanResult = "https://sdk.verifyaccess.ibm.com/diagency/a2a/v1/messages/<agent-id>>/invitation?id=<invitation-id>"

// Fetch the invitation details from the service
coroutineScope.launch {
    walletService?.previewInvitation(URL(credentialUrl))
        .onSuccess { data ->
            credentialPreviewInfo = data as CredentialPreviewInfo
            showPreviewDialog = true
        }
        .onFailure {
            errorTitle = "Error"
            errorMessage = "Failed to fetch data: ${it.message}"
            showErrorDialog = true
        }
}

// Use the `credentialPreviewInfo.jsonRepresentation` to display how a credential should look in the UI.
CredentialPreviewDialog(
    showPreviewDialog,
    credentialPreviewInfo.jsonRepresentation,
    onAccept = {
        showPreviewDialog = false
        coroutineScope.launch {
            ...
        }
    },
    onReject = {
        showPreviewDialog = false
    }
)

// Accept the credential
coroutineScope.launch {
    walletService.processCredential(credentialPreviewInfo)
        .onSuccess { credentialInfo ->
            walletViewModel.updateCredentials(
                walletManager.walletEntity,
                (walletManager.walletEntity.wallet.credentials + credentialInfo).toMutableList()
            )
        }
        .onFailure {
            errorTitle = "Error"
            errorMessage = "Failed to fetch data: ${it.message}"
            showErrorDialog = true
        }
}

// Store the credential
walletViewModel.updateCredentials(
    walletEntity,
    (walletEntity.walletEntity.wallet.credentials + credentialInfo).toMutableList()
)
```

### Verifying a credential

An agent can initiate a proof request via an invitation to verify a credential in a wallet.  The claims that are requested by the verifier are first generated, then shared for the verifier to validation.  Interactions with the wallet are performed using the `WalletService`.  The following example demonstrates the flow to preview an verification invitation, generate the claims and share with the verifier:

```Kotlin
// Create a "WalletService" using the initialized wallet.
val walletService = WalletService(
    accessToken = walletEntity.wallet.token.accessToken,
    refreshUri = walletEntity.wallet.refreshUri,
    baseUri = walletEntity.wallet.baseUri,
    clientId = walletEntity.wallet.clientId
)

// Generate the claims to submit
coroutineScope.launch {
    walletService.previewVerification(URL(verificationUrl))
        .onSuccess { verificationPreview ->
            ...
        }
        .onFailure {
            errorTitle = "Error"
            errorMessage = "Failed to fetch data: ${it.message}"
            showErrorDialog = true
        }
}

// Share the claims with the verifier
coroutineScope.launch {
    verificationPreviewInfo?.let {
        walletService.processProofRequest(it,
            VerificationAction.SHARE)
            ?.onSuccess { verificationInfo ->
                ...
            }
            ?.onFailure { error ->
                errorTitle = "Error"
                errorMessage = "Failed to fetch data: ${error.message}"
                showErrorDialog = true
            }
    }
}

// Store the verification
walletViewModel.updateVerification(
    walletEntity,
    (walletEntity.wallet.verifications + verificationInfo).toMutableList()
)
```

## License

This package contains code licensed under the MIT License (the "License"). You may view the License in the [LICENSE](../../LICENSE) file within this package.
