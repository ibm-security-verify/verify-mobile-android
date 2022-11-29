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
    implementation("com.android.tools.build:gradle:7.3.1")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
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