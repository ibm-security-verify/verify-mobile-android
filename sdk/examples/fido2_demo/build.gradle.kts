plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/common-config-demos.gradle")

android {
    namespace = "com.ibm.security.verifysdk.fido2.demoapp"
    defaultConfig {
        applicationId = "com.ibm.security.verifysdk.fido2.demoapp"
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":fido2"))

    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logging.interceptor)
    implementation(libs.material)
    implementation(libs.slf4j.jdk14)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit.v120)
    androidTestImplementation(libs.androidx.espresso.core.v360)
}