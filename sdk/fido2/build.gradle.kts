import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    id("com.android.library")
    id("ibm-verifysdk-plugin")
}

val moduleArtifactId = "fido2"
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
    namespace = "com.ibm.security.verifysdk.fido2"
}
dependencies {
    implementation(project(":core"))
}

tasks {
    register("androidJavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        from("${project.layout.buildDirectory}/javadoc")
        dependsOn(dokkaJavadoc)
    }
    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
}

// To-do: move to VerifySdkBuildPlugin
configure<org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension> {
    failOnError = false
    skipConfigurations.add("lintClassPath")
}

// To-do: move to VerifySdkBuildPlugin
tasks.withType<DependencyUpdatesTask>().configureEach {
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

tasks.withType<DokkaTask>().configureEach {
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "(c) 2024 IBM"
        separateInheritedMembers = false
        mergeImplicitExpectActualDeclarations = false
    }
    dokkaSourceSets {
        named("main") {
            moduleName.set("IBM Security Verify FIDO2(TM) SDK for Android")
            includes.from("fido2_module.md")

            // adds source links that lead to this repository, allowing readers
            // to easily find source code for inspected declarations
            sourceLink {
                localDirectory.set(file("src/main/java"))
                remoteUrl.set(URL("https://github.com/ibm-security-verify/verify-sdk-android/tree/main/sdk/fido2/src/main/java/"))
                remoteLineSuffix.set("#L")
            }
        }
    }
}