# IBM Security Verify Core SDK for Andriod

![SDK Version](https://img.shields.io/badge/IBM%20Security%20Verify%20Core%20SDK-3.0.0-blue.svg)
![Android Version](https://img.shields.io/badge/Android-12-green.svg)
![Android Version](https://img.shields.io/badge/Android%20API-31-green.svg)

The `Core` SDK provides common functionality across the other SDKs in the IBM Security Verify SDK
offering. It will evolve over time and currently offers a unified logging module, Keystore services
for creating and persisting private keys along with export functionality of the public key. The
error module is an abstract class for other SDKs to implement customised error details.

## Download

Artifacts are available [here](/releases).

### Gradle

Add this line for each SDK to your `build.gradle` file (app level):

`implementation 'com.ibm.security.verifysdk.core:core:3.0.0'`