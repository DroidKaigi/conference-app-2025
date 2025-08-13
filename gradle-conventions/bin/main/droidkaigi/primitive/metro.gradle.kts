package droidkaigi.primitive

import dev.zacsweers.metro.gradle.MetroPluginExtension

plugins {
    id("dev.zacsweers.metro")
}

configure<MetroPluginExtension> {
// disabled for now because unknown runtime crash occurs when injecting top-level functions
//    enableTopLevelFunctionInjection.set(true)
}
