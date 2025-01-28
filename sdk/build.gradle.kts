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
}

// used for release naming and in mfa SDK
extra["versionName"] = "3.0.6"
extra["versionCode"] = "107"

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
            force(rootProject.libs.netty.codec.http2) // CVE-2023-44487 in netty-codec-http2-4.1.93.Final
            force(rootProject.libs.jackson.woodstox.core)  // https://mvnrepository.com/artifact/com.fasterxml.woodstox/woodstox-core/6.2.4
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
}