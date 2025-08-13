package droidkaigi.primitive

import com.android.build.gradle.BaseExtension
import util.getDefaultPackageName

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    /**
     *  replace with "com.android.kotlin.multiplatform.library"
     *  after https://youtrack.jetbrains.com/issue/CMP-8202/Preview-not-work-in-commonMain-with-multi-module is resolved
     */
    id("com.android.library")
}

kotlin {
    jvmToolchain(17)
    jvm()

    androidTarget()

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xwhen-guards",
            "-Xexpect-actual-classes",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }
}

configure<BaseExtension> {
    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }
    compileSdkVersion(36)
    namespace = getDefaultPackageName(project.name)
}
