package droidkaigi.primitive

import util.kotlin

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}
