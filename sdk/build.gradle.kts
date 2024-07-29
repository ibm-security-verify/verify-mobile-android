// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.dokka)
    java
    jacoco
}

subprojects {

    apply(from = "$rootDir/jacoco.gradle")

    apply {
        plugin("maven-publish")
        plugin("org.jetbrains.dokka")
    }
    val dokkaPlugin by configurations
    dependencies {
        dokkaPlugin(rootProject.libs.versioning.plugin)
    }

    project.configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            preferProjectModules()
            force(rootProject.libs.jackson.woodstox.core)  // https://mvnrepository.com/artifact/com.fasterxml.woodstox/woodstox-core/6.2.4
        }
    }

//    tasks.jacocoTestReport {
//        dependsOn(tasks.test)
//    }
}