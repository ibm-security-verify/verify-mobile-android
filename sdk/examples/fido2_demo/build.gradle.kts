plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/common-config-demos.gradle")
apply(from = "$rootDir/common-config-ktor.gradle")

android {
    namespace = "com.ibm.security.verifysdk.fido2.demoapp"
    defaultConfig {
        applicationId = "com.ibm.security.verifysdk.fido2.demoapp"
    }

    project.configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            preferProjectModules()
            force("io.netty:netty-codec-http2:4.1.111.Final") // because of CVE-2023-44487 in netty-codec-http2-4.1.93.Final
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":fido2"))

    implementation(libs.androidx.biometric)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.logging.interceptor)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}