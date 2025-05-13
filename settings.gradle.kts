// Root settings.gradle.kts
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
//    versionCatalogs {
//        create("libs") {
//            from("gradle/libs.versions.toml")
//        }
//    }
}


rootProject.name = "IBM Verify SDK"

include(":sdk:adaptive")
include(":sdk:authentication")
include(":sdk:core")
include(":sdk:dc")
include(":sdk:fido2")
include(":sdk:mfa")

include(":sdk:dpop_demo")
include(":sdk:fido2_demo")
include(":sdk:mfa_demo")
include(":sdk:dc_demo")
include(":sdk:test_utils")

project(":sdk:dc_demo").projectDir = File("sdk/examples/dc_demo")
project(":sdk:dpop_demo").projectDir = File("sdk/examples/dpop_demo")
project(":sdk:fido2_demo").projectDir = File("sdk/examples/fido2_demo")
project(":sdk:mfa_demo").projectDir = File("sdk/examples/mfa_demo")