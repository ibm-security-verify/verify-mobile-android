# IBM Security Verify Authentication SDK for Android

The IBM Security Verify Authentication Software Development Kit (SDK) enables applications to obtain limited access to an HTTP service by orchestrating an approval interaction between the resource owner and the HTTP service.


## Getting started

Artifacts are available [here](/releases).

### Gradle

Add this line to your `build.gradle` file (app level):

`implementation 'com.ibm.security.verifysdk.authentication:authentication:3.0.0'`

### API documentation
The Authentication SDK for Android API can be reviewed [here](https://ibm-security-verify.github.io/android/authentication/docs/).

## Usage

### Getting OpenId Configuration Metadata

Discover the authorization service configuration from a compliant OpenID Connect endpoint.

```kotlin
val clientId = "<your-clientId>"
val clientSecret = "<your-clientSecret>"
val url = URL("https://www.example.com/.well-known/openid-configuration")
val result = OAuthProvider(clientId, clientSecret).discover(url)

result.onSuccess { value: OIDCMetadataInfo ->
    log.debug(value.toString())

}
result.onFailure { exception: Throwable ->
    log.error(exception.toString())
}
```

### Authorization Code Flow (AZN)

Authorization code flow is obtained by using an authorization server as an intermediary between the client and resource owner.  The code can later be exchanged for an access token and refresh token.  Refer to [Authorization Code Grant](https://datatracker.ietf.org/doc/html/rfc6749#section-4.1) for more information.

### Authorization Code Flow (AZN) with PKCE

PKCE enhances the authorization code flow by introducing a secret created by the calling application that can be verified by the authorization server.  The secret, called the Code Verifier is hashed by the calling application into a Code Challenge; it is this value that is send to the authorization server.  A malicious attacker can only intercept the Authorization Code, but cannot exchange it for an access token without the Code Verifier.  The code can later be exchanged for an access token and refresh token.  Refer to [Proof Key for Code Exchange by OAuth Public Clients](https://datatracker.ietf.org/doc/html/rfc7636) for more information.
        

### Basic Resource Owner Password Credentials (ROPC) grant

Obtaining a token based on a username and password.

```kotlin
val url = URL("https://www.example.com/token")
val additionalHeaders = HashMap<String, String>()
val additionalParameters = HashMap<String, String>()

val oAuthProvider = OAuthProvider(clientId, clientSecret, additionalHeaders, additionalParameters)

val result = oAuthProvider.authorize(url = url, username = "testuser", password = "password", scope = arrayOf("name", "age"))

result.onSuccess { value: TokenInfo ->
    log.debug(value.toString())

}
result.onFailure { exception: Throwable ->
    log.error(exception.toString())
}
```

### Refreshing a Token

Refresh tokens are issued to the client by the authorization server and are used to obtain a new access token when the current access token becomes invalid or expires, or to obtain additional access tokens with identical or narrower scope.

```kotlin
val url = URL("https://www.example.com/token")
val result = oAuthProvider.refresh(url, refreshToken = token.refreshToken, scope = arrayOf("name"))

result.onSuccess { value: TokenInfo ->
    log.debug(value.toString())

}
result.onFailure { exception: Throwable ->
    log.error(exception.toString())
}
```


### Decoding the ID Token Claims
The ID token is an artifact that proves that the user has been authenticated introduced by [OpenID Connect (OIDC)](https://openid.net/specs/openid-connect-core-1_0.html#IDToken).  The ID token is obtained when `openid` is part of the scope in your authorization request.

The ID token is represented as a sequence of URL-safe parts separated by period ('.') characters.  Each part contains a base64url encoded string, for example:

`{header}.{claims}.{signature}`

```kotlin

val jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

// Claims is the second element of data in the string.
val claims = jwt.split(".")[1]
val data = String(Base64.decode(claims, Base64.DEFAULT), StandardCharsets.UTF_8)
```

NOTE: Best practice is to validate the claims against the signature before relying on them for other verifications. 3rd party libraries like [these](https://jwt.io/libraries) can provide the verification and decoding of the ID token.


## License
This package contains code licensed under the MIT License (the "License"). You may view the License in the [LICENSE](../../LICENSE) file within this package.
