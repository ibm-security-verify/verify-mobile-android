plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

apply(from = "$rootDir/common-config.gradle")
apply(from = "$rootDir/common-config-ktor.gradle")
apply(from = "$rootDir/common-publish.gradle")

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

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.junit)
}