// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.dokka)
    java
    jacoco
    alias(libs.plugins.neotech.rootcoverage) apply true
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.compose) apply false
}

// used for release naming and in mfa SDK
extra["versionName"] = "3.0.8"
extra["versionCode"] = "109"

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        preferProjectModules()
        force(rootProject.libs.jackson.dataformat.xml) // transitive dependency on woodstox-core:6.2.4
    }
}

rootCoverage {
 excludes = listOf(
     "**/R.class",
     "**/R\$*.class",
     "**/*Companion*.class",
     "**/*Function0*.class",
     "**/BuildConfig.*",
     "**/Manifest*.*",
     "**/*Test*.*",
     "android/**/*.*",
     "**/*\$Lambda$*.*",  // Jacoco can't handle several "$" in class names.
     "**/*\$inlined$*.*"  // Kotlin specific classes Jacoco can't handle.
 )

 generateHtml = true
 generateXml = true
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal() // Adjust the threshold for branch coverage
            }
        }
    }
}

subprojects {

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
            force(rootProject.libs.netty.codec.http2)
            force("io.netty:netty-handler-proxy:4.1.118.Final")
            force(rootProject.libs.jackson.woodstox.core)  // https://mvnrepository.com/artifact/com.fasterxml.woodstox/woodstox-core/6.2.4 
            force("com.google.guava:guava:32.0.1-jre")
            force("commons-io:commons-io:2.14.0")
            force("com.google.protobuf:protobuf-java:3.25.5")
            force("com.google.protobuf:protobuf-javalite:3.25.5")
        }
    }

    /**
     * List all first-level dependencies for a specific module.
     */
    tasks.register("listFirstLevelDependencies") {
        doLast {
            configurations["releaseCompileClasspath"]
                .resolvedConfiguration
                .firstLevelModuleDependencies
                .forEach { dependency ->
                    println("${dependency.moduleGroup}:${dependency.moduleName}:${dependency.moduleVersion}")
                }
        }
    }

    /**
     * List all dependencies for a specific module.
     */
    tasks.register<DependencyReportTask>("allDeps")
}