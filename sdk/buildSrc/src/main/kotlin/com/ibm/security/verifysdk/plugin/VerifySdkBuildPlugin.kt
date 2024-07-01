/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.embeddedKotlinVersion
import org.gradle.kotlin.dsl.extra
class VerifySdkBuildPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("kotlin-android")
        project.plugins.apply("kotlinx-serialization")
        project.plugins.apply("maven-publish")
        project.plugins.apply("org.jetbrains.dokka")
        project.plugins.apply("com.github.ben-manes.versions")

        val androidExtension = project.extensions.getByName("android")
        if (androidExtension is BaseExtension) {
            androidExtension.apply {
                compileSdkVersion(34)
                buildFeatures.buildConfig = true
                defaultConfig {
                    targetSdk = 34
                    minSdk = 28
                    versionCode = 102
                    versionName = "3.0.2"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    manifestPlaceholders["auth_redirect_scheme"] = "verifysdk"
                    manifestPlaceholders["auth_redirect_host"] = "callback"
                    manifestPlaceholders["auth_redirect_path"] = ""
                    buildConfigField("String", "VERSION_NAME", "\"3.0.2\"")
                }

                packagingOptions {
                    resources.excludes.add("META-INF/LICENSE*.md")
                }

                project.configurations.all {
                    resolutionStrategy {
                        failOnVersionConflict()
                        preferProjectModules()
                        force("com.fasterxml.woodstox:woodstox-core:6.4.0")
                        force("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
                        force("io.netty:netty-codec-http2:4.1.111.Final") // because of CVE-2023-44487 in netty-codec-http2-4.1.93.Final
                    }
                }

                testOptions.unitTests {
                    isReturnDefaultValues = true
                }

                buildTypes {
                    getByName("debug") {
                        isTestCoverageEnabled = true
                    }
                }

                packagingOptions {
                    resources.excludes.add("META-INF/DEPENDENCIES")
                }

                val proguardFile = "proguard-rules.pro"
                when (this) {
                    is LibraryExtension -> defaultConfig {
                        consumerProguardFiles(proguardFile)
                    }
                    is AppExtension -> buildTypes {
                        getByName("release") {
                            isMinifyEnabled = true
                            isShrinkResources = true
                            isDebuggable = false
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                proguardFile
                            )
                        }
                    }
                }

                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }

        // https://github.com/Kotlin/dokka/issues/3472
        project.configurations.matching { it.name.startsWith("dokka") }.configureEach {
            resolutionStrategy.eachDependency {
                if (requested.group.startsWith("com.fasterxml.jackson")) {
                    useVersion("2.15.3")
                }
            }
        }
        
        project.dependencies {

            val kotlinVersion = "2.0.0"
            val kotlinCoroutines = "1.8.1"
            val square = "4.12.0"
            val ktorVersion = "2.3.12"

            add("androidTestImplementation", "androidx.test.ext:junit:1.2.0")
            add("androidTestImplementation", "androidx.test:core:1.6.0")
            add("androidTestImplementation", "androidx.test:rules:1.6.0")
            add("androidTestImplementation", "androidx.test:runner:1.6.0")
            add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.6.0")
            add("androidTestImplementation", "androidx.test.uiautomator:uiautomator:2.3.0")
            add("androidTestImplementation", "junit:junit:4.13.2")
            add("androidTestImplementation", "org.junit.jupiter:junit-jupiter")    // JUnit5
            add("androidTestImplementation", "org.mockito.kotlin:mockito-kotlin:5.3.1")
            add("androidTestImplementation", "com.squareup.okhttp3:mockwebserver:$square")
            add("androidTestImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutines")
            add("androidTestImplementation", platform("org.junit:junit-bom:5.10.2"))          // JUnit5
            add("androidTestImplementation", "org.slf4j:slf4j-jdk14:2.0.13")
            add("androidTestImplementation", "io.ktor:ktor-client-mock:$ktorVersion")

            add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.0.4")

            add("implementation", "androidx.core:core-ktx:1.13.1")
            add("implementation", "org.jacoco:org.jacoco.core:0.8.12")
            add("implementation", "com.squareup.okhttp3:logging-interceptor:$square")

            add("implementation", "io.ktor:ktor-client-core:$ktorVersion")
            add("implementation", "io.ktor:ktor-client-logging:$ktorVersion")
            add("implementation", "io.ktor:ktor-client-serialization:$ktorVersion")
            add("implementation", "io.ktor:ktor-client-auth:$ktorVersion")
            add("implementation", "io.ktor:ktor-client-okhttp:$ktorVersion")
            add("implementation", "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            add("implementation", "io.ktor:ktor-client-content-negotiation:$ktorVersion")
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinCoroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$kotlinCoroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
            add("implementation", "org.slf4j:slf4j-api:2.0.13")
            add("implementation", "androidx.biometric:biometric:1.2.0-alpha05")
            add("implementation", "androidx.browser:browser:1.8.0")
            add("implementation", "com.fasterxml.jackson.core:jackson-core:2.17.1")
            add("implementation", "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.17.1")

            add("testImplementation", "junit:junit:4.13.2") // JUnit4 for Adaptive SDK
//            add("testImplementation", "org.json:json:20220320")             // Using json in unit tests
        }
    }

    private fun BaseExtension.applyProguardSettings() {
        val proguardFile = "proguard-rules.pro"
        when (this) {
            is LibraryExtension -> defaultConfig {
                consumerProguardFiles(proguardFile)
            }
            is AppExtension -> buildTypes {
                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        proguardFile
                    )
                }
            }
        }
    }
}
