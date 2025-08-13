package droidkaigi.primitive

import org.gradle.internal.extensions.stdlib.capitalized
import util.commonMainImplementation
import util.getDefaultPackageName

plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    commonMainImplementation(compose.components.resources)
}

compose {
    resources {
        val namespace = getDefaultPackageName(project.name)
        packageOfResClass = namespace
        nameOfResClass = namespace.split(".").last().capitalized() + "Res"
        generateResClass = always
    }
}
