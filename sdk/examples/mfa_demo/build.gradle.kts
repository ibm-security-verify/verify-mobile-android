plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/common-config-demos.gradle")
apply(from = "$rootDir/common-config-ktor.gradle")

android {
    namespace = "com.ibm.security.verifysdk.mfa.demoapp"
    defaultConfig {
        applicationId = "com.ibm.security.verifysdk.mfa.demoapp"

        manifestPlaceholders["auth_redirect_scheme"] = "verifysdk"
        manifestPlaceholders["auth_redirect_host"] = "callback"
        manifestPlaceholders["auth_redirect_path"] = "/redirect"
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":mfa"))

    implementation(libs.zxing.android.embedded)

    androidTestImplementation(libs.androidx.espresso.core)
}