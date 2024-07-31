plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/common-config-demos.gradle")

android {
    namespace = "com.ibm.security.verifysdk.mfa.demoapp"
    defaultConfig {
        applicationId = "com.ibm.security.verifysdk.mfa.demoapp"

        manifestPlaceholders["auth_redirect_scheme"] = "verifysdk"
        manifestPlaceholders["auth_redirect_host"] = "callback"
        manifestPlaceholders["auth_redirect_path"] = ""
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
    implementation(project(":mfa"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.zxing.android.embedded)
    implementation(libs.slf4j.jdk14)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}