plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/common-config.gradle")
apply(from = "$rootDir/common-config-ktor.gradle")
apply(from = "$rootDir/common-publish.gradle")

dependencies {

    api(project(":sdk:core"))
    api(project(":sdk:authentication"))
    api(project(":sdk:dc:core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.jackson.core)
    implementation(libs.jackson.dataformat.cbor)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material)

    androidTestImplementation(project(":sdk:test_utils"))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}