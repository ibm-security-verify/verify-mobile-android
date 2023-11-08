plugins {
    id("com.android.application")
    id("ibm-verifysdk-plugin")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("../sonarqube.gradle")
    from("../jacoco.gradle")
}

configurations.all {
    resolutionStrategy {
        force("com.squareup.okio:okio-jvm:3.4.0")
    }
}


dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("org.slf4j:slf4j-jdk14:2.0.7")

    implementation(project(":mfa"))
    implementation(project(":core"))
    implementation(project(":adaptive"))
    implementation(project(":authentication"))
    implementation("androidx.core:core-ktx:1.10.1")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}

android {
    namespace = "com.ibm.security.verifysdk"
}
android {
    namespace = "com.ibm.security.verifysdk"
}
