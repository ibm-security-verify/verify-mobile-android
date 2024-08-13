// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.dokka)
    java
    jacoco
}

extra["versionName"] = "3.0.3"
extra["versionCode"] = "104"

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        preferProjectModules()
        force(rootProject.libs.jackson.dataformat.xml) // transitive dependency on woodstox-core:6.2.4
    }
}


/**
 * Custom task customConnectedAndroidTest that depends on running
 * connectedAndroidTest in all modules except the listed ones.
 */
tasks.register("customConnectedAndroidTest") {
    dependsOn(
        subprojects
            .filter { project ->
                !project.name.contains("_demo")
            }
            .flatMap { project ->
                project.tasks.matching { task ->
                    task.name == "connectedAndroidTest"
                }
            }
    )
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

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            preferProjectModules()
            force(rootProject.libs.netty.codec.http2) // CVE-2023-44487 in netty-codec-http2-4.1.93.Final
            force(rootProject.libs.jackson.woodstox.core)  // https://mvnrepository.com/artifact/com.fasterxml.woodstox/woodstox-core/6.2.4
        }
    }

//    tasks.jacocoTestReport {
//        dependsOn(tasks.test)
//    }
}