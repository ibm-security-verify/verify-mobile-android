plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/common-config.gradle")
apply(from = "$rootDir/common-config-ktor.gradle")
apply(from = "$rootDir/common-publish.gradle")

android {
    defaultConfig {
        // for IBM Verify Authentication SDK
        manifestPlaceholders["auth_redirect_scheme"] = "verifysdk"
        manifestPlaceholders["auth_redirect_host"] = "callback"
        manifestPlaceholders["auth_redirect_path"] = "/redirect"
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":authentication"))

    implementation(libs.androidx.biometric)
    implementation(libs.androidx.core.ktx)
    implementation(libs.jackson.core)
    implementation(libs.jackson.dataformat.cbor)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.rootbeer.lib)

    androidTestImplementation(libs.androidx.espresso.core)
}