# IBM Security Verify SDK for Android

This repository is for active development of the IBM Security Verify Software Development Kit for Android.

## Getting started

Each Software Development Kit (SDK) is separate for you to choose from instead of one large IBM Security Verify SDK package. To get started with a specific SDK, see the README.md file located in the specific project folder.

## Prerequisites

* The SDK is written in Kotlin.
* To use the `dc` or `mfa` SDK an [IBM Verify](https://www.ibm.com/products/verify-for-consumer-iam) tenant or [IBM Verify Identity Access](https://www.ibm.com/au-en/products/verify-access) is required.

## Software Development Kits

The following SDKs are currently offered in the package:

| Component | Description |
| ----------- | ----------- |
| [FIDO2](sdk/fido2) | The FIDO2™ component is a native implementation of attestation and assertion ceremonies.  Essentially providing the equivalent of WebAuthn's `navigator.credentials.create()` and `navigator.credentials.get()` for native mobile apps.|
| [Adaptive](sdk/adaptive) | The IBM Security Verify Adaptive SDK provides device assessment. Based on cloud risk policies, authentication and authorization challenges can be evaluated.|
| [Core](sdk/core) | The IBM Security Verify Core SDK provides common Keychain and networking functionality across the other components in the IBM Security Verify SDK offering.|
| [Authentication](sdk/authentication) | The IBM Security Verify Authentication SDK is an implementation of OAuth 2.0 and OIDC targeting mobile use cases.|
| [MFA](sdk/mfa) | The IBM Security Verify MFA SDK provides multi-factor authentication support for creating authenticators and processing transactions.|
| [DC](sdk/dc) | The IBM Security Verify DC SDK supporting digital credentials in a mobile Wallet.|

## Download

SDK artifacts are available [here](/releases).

### Maven

Store the files in your local maven repository. This is usually located at `~/.m2/repository/`.

Add `mavenLocal` at the top of your list of repositories:

```gradle
buildscript {
    repositories {
        mavenLocal()
    }
}
```

Add this line for each SDK to your `build.gradle` file (app level):

```gradle
implementation 'com.github.ibm-security-verify:verify-sdk-android:<module>:<version>'
```

### Jitpack

In your project-level build.gradle file and add JitPack as a repository:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        }
    }
```

Then for each SDK, add the following line into your module-level build.gradle file:

```gradle
    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:v3.0.0'
```

Replace the particular SDK - `core` in this example - with the name of the SDKs you would like to embed.

You can then use the JitPack website https://jitpack.io/ to generate a compile statement, based on the SDK’s GitHub url.

### Manually from GitHub release

Download the required SDK files from https://github.com/ibm-security-verify/verify-sdk-android/releases/latest,  store them into the lib folder and sync your project.