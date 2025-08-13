package droidkaigi.convention

import com.android.build.gradle.BaseExtension
import com.google.devtools.ksp.gradle.KspAATask
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import util.library
import util.libs

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.compose.resources")
    id("droidkaigi.primitive.kmp.roborazzi")
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.detekt")
    id("com.google.devtools.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:model"))
            implementation(project(":core:common"))
            implementation(project(":core:droidkaigiui"))
            implementation(project(":core:designsystem"))

            implementation(libs.library("kotlinxCollectionsImmutable"))

            implementation(libs.library("soilQueryCompose"))
            implementation(libs.library("soilReacty"))
        }

        commonTest.dependencies {
            implementation(project(":core:testing"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.library("kotlinTest"))
            implementation(libs.library("lifecycleViewmodelCompose"))
            implementation(libs.library("lifecycleRuntimeCompose"))
        }

        androidMain.dependencies {
            implementation(libs.library("lifecycleViewmodelNavigation3"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=soil.query.annotation.ExperimentalSoilQueryApi",
            "-opt-in=androidx.compose.ui.test.ExperimentalTestApi",
        )
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":tools:ksp-processor"))
    add("debugImplementation", compose.uiTooling)
}

configure<BaseExtension> {
    testOptions.unitTests.isIncludeAndroidResources = true
}

configure<KotlinMultiplatformExtension> {
    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

// Workaround for KSP declarations become Unresolved reference in IDE.
// https://github.com/google/ksp/issues/963#issuecomment-2970973355
tasks.withType<KspAATask>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.withType<KotlinCompile>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.withType<KotlinNativeCompile>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
