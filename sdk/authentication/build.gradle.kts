plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

apply(from = "$rootDir/common-config.gradle")
apply(from = "$rootDir/common-config-ktor.gradle")
apply(from = "$rootDir/common-publish.gradle")

android {
    defaultConfig {
        manifestPlaceholders["auth_redirect_scheme"] = "verifysdk"
        manifestPlaceholders["auth_redirect_host"] = "callback"
        manifestPlaceholders["auth_redirect_path"] = "/redirect"
    }
}

dependencies {

    implementation(project(":core"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logging.interceptor)
    implementation(libs.material)

    androidTestImplementation(project(":test_utils"))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}