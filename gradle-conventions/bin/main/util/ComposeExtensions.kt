@file:Suppress("UNUSED")

package util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal val Project.compose: ComposeExtension get() = extensions.getByType<ComposeExtension>()

internal val KotlinDependencyHandler.compose: ComposePlugin.Dependencies get() = this.project.extensions.getByType<ComposeExtension>().dependencies

internal fun Project.compose(block: ComposeExtension.() -> Unit) {
    (extensions.getByType<ComposeExtension>()).apply(block)
}

internal fun ComposeExtension.resources(block: ResourcesExtension.() -> Unit) {
    this.extensions.getByType<ResourcesExtension>().apply(block)
}
