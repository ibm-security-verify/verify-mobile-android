plugins {
    id("com.android.application")
    id("ibm-verifysdk-plugin")
}

apply {
    from("../sonarqube.gradle")
    from("../jacoco.gradle")
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")
    implementation("org.slf4j:slf4j-jdk14:1.7.32")

    implementation(project(":core"))
    implementation(project(":adaptive"))
}
