/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    val kotlinVersion by extra { "1.8.10" }

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.owasp:dependency-check-gradle:8.2.1")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3")
        classpath("gradle.plugin.com.hcl.security:appscan-gradle-plugin:1.0.8")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}

tasks.withType<Test>().configureEach  {
    maxParallelForks = 1
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs.plus("'-opt-in=kotlin.RequiresOptIn")
}