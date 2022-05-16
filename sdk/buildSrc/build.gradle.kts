plugins {
    `kotlin-dsl`
    jacoco
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    implementation("com.android.tools.build:gradle:7.1.2")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

gradlePlugin {
    plugins {
        register("ibm-verifysdk-plugin") {
            id = "ibm-verifysdk-plugin"
            implementationClass = "com.ibm.security.verifysdk.plugin.VerifySdkBuildPlugin"
            version = "1.0.0"
        }
    }
}