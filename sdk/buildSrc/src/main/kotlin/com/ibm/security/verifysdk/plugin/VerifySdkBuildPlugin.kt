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

class VerifySdkBuildPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("kotlin-android")
        project.plugins.apply("kotlinx-serialization")
        project.plugins.apply("maven-publish")
        project.plugins.apply("org.jetbrains.dokka")
        project.plugins.apply("org.owasp.dependencycheck")
        project.plugins.apply("com.github.ben-manes.versions")

        val androidExtension = project.extensions.getByName("android")
        if (androidExtension is BaseExtension) {
            androidExtension.apply {
                compileSdkVersion(34)
                defaultConfig {
                    targetSdk = 34
                    minSdk = 29
                    versionCode = 101
                    versionName = "3.0.1"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    manifestPlaceholders["auth_redirect_scheme"] = "verifysdk"
                    manifestPlaceholders["auth_redirect_host"] = "callback"
                    manifestPlaceholders["auth_redirect_path"] = ""
                }

                packagingOptions {
                    resources.excludes.add("META-INF/LICENSE*.md")
                }

                project.configurations.all {
                    resolutionStrategy {
                        failOnVersionConflict()
                        preferProjectModules()
                        force("com.fasterxml.woodstox:woodstox-core:6.4.0")
                        force("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
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

        // Unit tests: JUnit5
        // Instrumentation tests: JUnit4
        
        project.configurations.all {
            resolutionStrategy {
                force("com.squareup.okio:okio-jvm:3.4.0")
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

        val kotlin_coroutines = "1.7.3"
        val kotlin_lib = "1.9.22"
        val square = "4.12.0"

        project.dependencies {

            add("androidTestImplementation", "androidx.test.ext:junit:1.1.5")
            add("androidTestImplementation", "androidx.test:core:1.5.0")
            add("androidTestImplementation", "androidx.test:rules:1.5.0")
            add("androidTestImplementation", "androidx.test:runner:1.5.0")
            add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.5.1")
            add("androidTestImplementation", "androidx.test.uiautomator:uiautomator:2.2.0")
            add("androidTestImplementation", "junit:junit:4.13.1")
            add("androidTestImplementation", "org.junit.jupiter:junit-jupiter")    // JUnit5
            add("androidTestImplementation", "org.mockito.kotlin:mockito-kotlin:5.2.1")
            add("androidTestImplementation", "com.squareup.okhttp3:mockwebserver:$square")
            add("androidTestImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlin_coroutines")
            add("androidTestImplementation", platform("org.junit:junit-bom:5.8.2"))          // JUnit5
            add("androidTestImplementation", "org.slf4j:slf4j-jdk14:2.0.7")

            add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.0.4")

            add("implementation", "androidx.biometric:biometric:1.2.0-alpha05")
            add("implementation", "androidx.core:core-ktx:1.12.0")
            add("implementation", "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
            add("implementation", "com.google.code.gson:gson:2.9.0")
            add("implementation", "org.jacoco:org.jacoco.core:0.8.8")
            add("implementation", "com.squareup.retrofit2:retrofit:2.9.0")
            add("implementation", "com.squareup.retrofit2:converter-gson:2.9.0")
            add("implementation", "com.squareup.okhttp3:okhttp:$square")
            add("implementation", "com.squareup.okhttp3:logging-interceptor:$square")
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_lib")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlin_coroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$kotlin_coroutines")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            add("implementation", "org.slf4j:slf4j-api:2.0.7")
            add("implementation", "androidx.browser:browser:1.7.0")
            add("implementation", "com.fasterxml.jackson.core:jackson-core:2.16.1")
            add("implementation", "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.16.1")

            add("testImplementation", "junit:junit:4.13.1") // JUnit4 for Adaptive SDK
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
