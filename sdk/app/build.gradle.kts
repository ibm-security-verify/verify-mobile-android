plugins {
    id("com.android.application")
    id("ibm-verifysdk-plugin")
}

apply {
    from("../sonarqube.gradle")
    from("../jacoco.gradle")
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("org.slf4j:slf4j-jdk14:2.0.7")

    implementation(project(":core"))
    implementation(project(":adaptive"))
    implementation(project(":authentication"))
}
android {
    namespace = "com.ibm.security.verifysdk"
}
