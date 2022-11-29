# IBM Security Verify SDK for Android

This repository is for active development of the IBM Security Verify Software Development Kit for Android.

## Getting started
Each Software Development Kit (SDK) is separate for you to choose from instead of one large IBM Security Verify SDK package. To get started with a specific SDK, see the README.md file located in the specific project folder.

## Prerequisites
* The SDK is written in Kotlin.
* To use the adaptive or multi-factor SDK an [IBM Security Verify](https://www.ibm.com/products/verify-for-consumer-iam) tenant or [IBM Security Verify Access](https://www.ibm.com/au-en/products/verify-access) is required.

## Software Development Kits
The following SDKs are currently offered in the package:

| SDK  |  Description | 
|---|---|
|  [Core](/sdk/core) | The IBM Security Verify Core SDK provides supporting functionality to other SDK, such as logging, Keystore services, abstract error structures and utility functions.  |
|  [Authentication](/sdk/authentication) | The IBM Security Verify Authentication SDK is an implementation of OAuth 2.0 and OIDC targeting mobile use cases.  |
|  [Adaptive](/sdk/adaptive) |  The IBM Security Verify Adaptive SDK provides device assessment. Based on cloud risk policies, authentication and authorization challenges can be evaluated. |  


## Download
SDK artifacts are available [here](/releases).

### Maven 
Store the files in your local maven repository. This is usually located at `~/.m2/repository/`.

Add `mavenLocal` at the top of your list of repositories:

```
buildscript {
    repositories {
        mavenLocal()
    }
}
```

Add this line for each SDK to your `build.gradle` file (app level):
    
    implementation 'com.github.ibm-security-verify:verify-sdk-android:<module>:<version>'


### Jitpack
In your project-level build.gradle file and add JitPack as a repository:

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
            }
        }

Then for each SDK, add the following line into your module-level build.gradle file:

    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:v3.0.0'

Replace the particular SDK - `core` in this example - with the name of the SDKs you would like to embed.

You can then use the JitPack website https://jitpack.io/ to generate a compile statement, based on the SDK’s GitHub url.

### Manually from GitHub release
Download the required SDK files from https://github.com/ibm-security-verify/verify-sdk-android/releases/latest,  store them into the lib folder and sync your project.