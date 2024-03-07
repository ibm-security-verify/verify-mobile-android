plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

configurations.all {
    resolutionStrategy {
        force("com.squareup.okio:okio-jvm:3.4.0")
    }
}

android {
    namespace = "com.ibm.security.verifysdk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ibm.security.verifysdk"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

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

configurations.all {
    resolutionStrategy {
        force("com.squareup.okio:okio-jvm:3.4.0")
    }
}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("org.slf4j:slf4j-jdk14:2.0.12")

    implementation(project(":core"))
    implementation(project(":fido2"))
    implementation(project(":adaptive"))
    implementation(project(":authentication"))
}
