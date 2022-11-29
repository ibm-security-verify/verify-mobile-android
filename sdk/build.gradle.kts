import org.gradle.model.internal.core.ModelNodes.withType

/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.7.20")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("org.owasp:dependency-check-gradle:7.3.0")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3")
        classpath("gradle.plugin.com.hcl.security:appscan-gradle-plugin:1.0.5")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
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