# IBM Security Verify SDK for Android

This repository is for active development of the IBM Security Verify Software Development Kit for Android.

## Getting started
Each Software Development Kit (SDK) is seperate for you to choose from instead of one large IBM Security Verify package. To get started with a specific SDK, see the README.md file located in the specific project folder.

## Prerequisites
* The SDK is written in Kotlin.
* To use the adaptive or multi-factor SDK an [IBM Security Verify](https://www.ibm.com/products/verify-for-consumer-iam) tenant or [IBM Security Verify Access](https://www.ibm.com/au-en/products/verify-access) is required.

## Software Development Kits
The following SDKs are currently offered in the package:

| SDK  |  Description | 
|---|---|
|  [Core](/sdk/core) | The core SDK provides supporting functionality to other SDK, such as logging, Keystore services, abstract error structures and utility functions.  |
|  [Adaptive](/sdk/adaptive) |  The adaptive SDK provides device assessment. Based on cloud risk policies, authentication and authorization challenges can be evaluated. |  


## Download
Snapshots of the development version are available [here](/releases).

### Maven Central 
Add this line for each SDK to your `build.gradle` file (app level):
    
    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:3.0.0'


### Jitpack
In your project-level build.gradle file and add JitPack as a repository:

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
            }
        }

Then for each SDK, add the foloowing line into your module-level build.gradle file:

    implementation 'com.github.ibm-security-verify:verify-sdk-android:core:v3.0.0'

Replace the particular SDK - `core` in this example - with the name of the SDKs you would like to embed.

You can then use the JitPack website https://jitpack.io/ to generate a compile statement, based on the SDK’s GitHub url.

### Manually from GitHub release
Download the relevant files from https://github.com/ibm-security-verify/verify-sdk-android/releases, copy and past them into the lib folder and sync your project.