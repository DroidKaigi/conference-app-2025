package droidkaigi.primitive

import util.commonMainImplementation
import util.library
import util.libs

plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    commonMainImplementation(compose.ui)
    commonMainImplementation(compose.components.uiToolingPreview)
    commonMainImplementation(compose.materialIconsExtended)
    commonMainImplementation(libs.library("rin"))
    commonMainImplementation(libs.library("material3"))
}

dependencies {
    add("debugImplementation", compose.uiTooling)
}
