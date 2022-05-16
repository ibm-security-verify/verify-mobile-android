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
        project.plugins.apply("org.owasp.dependencycheck")
        project.plugins.apply("org.jetbrains.dokka")
        project.plugins.apply("maven-publish")
        project.plugins.apply("com.hcl.security.appscan")
        project.plugins.apply("kotlinx-serialization")

        val androidExtension = project.extensions.getByName("android")
        if (androidExtension is BaseExtension) {
            androidExtension.apply {
                compileSdkVersion(31)
                defaultConfig {
                    targetSdk = 30
                    minSdk = 23
                    versionCode = 100
                    versionName = "3.0.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                project.configurations.all {
                    resolutionStrategy.failOnVersionConflict()
                    resolutionStrategy.preferProjectModules()
                }

                testOptions.unitTests {
                    isReturnDefaultValues = true
                }

                buildTypes {
                    getByName("debug") {
                        isTestCoverageEnabled = true
                    }
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
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                proguardFile
                            )
                        }
                    }
                }

                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }

        project.dependencies {
            add("androidTestImplementation", "androidx.test.ext:junit:1.1.1")
            add("androidTestImplementation", "androidx.test:core:1.2.0")
            add("androidTestImplementation", "androidx.test:runner:1.4.0")
            add("androidTestImplementation", "junit:junit:4.12")
            add("androidTestImplementation", "org.slf4j:slf4j-jdk14:1.7.32")
            add("androidTestImplementation", "org.mockito.kotlin:mockito-kotlin:4.0.0")
            add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
            add("implementation", "androidx.core:core-ktx:1.7.0")
            add("implementation", "org.jacoco:org.jacoco.core:0.8.7")
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:1.6.20")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.1")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.6.1")
            add("implementation", "org.slf4j:slf4j-api:1.7.32")
            add("implementation", "com.google.code.gson:gson:2.9.0")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            add("implementation","org.jetbrains.kotlinx:kotlinx-datetime:0.2.0")
            add("testImplementation", "junit:junit:4.12")
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
