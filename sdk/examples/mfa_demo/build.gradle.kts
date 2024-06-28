/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.ibm.security.verifysdk.mfa.demoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ibm.security.verifysdk.mfa.demoapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.activity:activity:1.9.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(project(":core"))
    implementation(project(":mfa"))
    implementation(project(":authentication"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.slf4j:slf4j-jdk14:2.0.12")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}