package droidkaigi.primitive

import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import util.getDefaultPackageName

plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val configurationName = when {
    extensions.findByType(KotlinMultiplatformExtension::class.java) != null -> "commonMainImplementation"
    extensions.findByType(KotlinSingleTargetExtension::class.java) != null -> "implementation"
    else -> "implementation"
}

dependencies {
    add(configurationName, compose.components.resources)
}

compose {
    resources {
        val namespace = getDefaultPackageName(project.name)
        packageOfResClass = namespace
        nameOfResClass = namespace.split(".").last().capitalized() + "Res"
        generateResClass = always
    }
}
