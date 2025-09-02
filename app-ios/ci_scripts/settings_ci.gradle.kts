// CI-specific settings for Xcode Cloud
// This file should be copied over settings.gradle.kts in CI environment

rootProject.name = "conference-app-2025"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-conventions")
    repositories {
        // Add explicit plugin portal first for better plugin resolution
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        // Fallback to JitPack if needed
        maven { url = uri("https://jitpack.io") }
    }
    
    // Force specific plugin versions to avoid resolution issues
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.gradle.kotlin.kotlin-dsl" -> {
                    // Use the version that comes with Gradle
                    useModule("org.gradle.kotlin:gradle-kotlin-dsl-plugins:${requested.version}")
                }
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        // navigation3 adaptive
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
        }
    }
}

include(
    ":app-android",
    ":app-desktop",
    ":app-shared",
)
include(
    ":core:common",
    ":core:model",
    ":core:droidkaigiui",
    ":core:data",
    ":core:designsystem",
    ":core:testing",
)
include(
    ":feature:about",
    ":feature:contributors",
    ":feature:eventmap",
    ":feature:favorites",
    ":feature:main",
    ":feature:profilecard",
    ":feature:settings",
    ":feature:sessions",
    ":feature:sponsors",
    ":feature:staff",
)