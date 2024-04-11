plugins {
    id("com.android.library")
    id("ibm-verifysdk-plugin")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
}

val moduleArtifactId = "mfa"
val moduleGroupId = "com.ibm.security.verifysdk"
val moduleVersion = android.defaultConfig.versionName
val moduleName = "IBM Security Verify SDK"
val moduleNameUrl = "https://github.com/ibm-security-verify/verify-sdk-android"
val moduleLicenseName = "MIT License"
val moduleLicenseUrl = "https://github.com/ibm-security-verify/verify-sdk-android/blob/main/LICENSE"
val moduleScmConnection = "scm:git:git://github.com/ibm-security-verify/verify-sdk-android.git"
val moduleScmDeveloperConnection =
    "scm:git:ssh://github.com/ibm-security-verify/verify-sdk-android.git"
val moduleScmUrl = "https://github.com/ibm-security-verify/verify-sdk-android"

apply {
    from("../jacoco.gradle")
}
android {
    namespace = "com.ibm.security.verifysdk.mfa"
    testNamespace = "com.ibm.security.verifysdk.mfa.test"
}

dependencies {
    implementation(project(":core"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.scottyab:rootbeer-lib:0.1.0")
    implementation(project(mapOf("path" to ":authentication")))
    implementation("androidx.core:core-ktx:1.10.1")
}

tasks {
    register("androidJavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        from("${layout.buildDirectory}/javadoc")
        dependsOn(dokkaJavadoc)
    }
    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

// To-do: move to VerifySdkBuildPlugin
tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>().configureEach {
    outputFormatter = "html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "dependencyUpdatesTask"

    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("mavenAndroid") {
            artifactId = moduleArtifactId
            groupId = moduleGroupId
            version = moduleVersion

            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            artifact(tasks.getByName("androidJavadocJar"))
            artifact(tasks.getByName("androidSourcesJar"))

            pom {
                name.set(moduleName)
                description.set("Description")
                url.set(moduleNameUrl)

                licenses {
                    license {
                        name.set(moduleLicenseName)
                        url.set(moduleLicenseUrl)
                    }
                }
                scm {
                    connection.set(moduleScmConnection)
                    developerConnection.set(moduleScmDeveloperConnection)
                    url.set(moduleScmUrl)
                }

                withXml {
                    fun groovy.util.Node.addDependency(dependency: Dependency, scope: String) {
                        appendNode("dependency").apply {
                            appendNode("groupId", dependency.group)
                            appendNode("artifactId", dependency.name)
                            appendNode("version", dependency.version)
                            appendNode("scope", scope)
                        }
                    }

                    asNode().appendNode("dependencies").let { dependencies ->
                        // List all "api" dependencies as "compile" dependencies
                        configurations.api.get().allDependencies.forEach {
                            dependencies.addDependency(it, "compile")
                        }
                        // List all "implementation" dependencies as "runtime" dependencies
                        configurations.implementation.get().allDependencies.forEach {
                            dependencies.addDependency(it, "runtime")
                        }
                    }
                }
            }
        }
    }
}
