package util

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun Project.kotlin(
    block: KotlinMultiplatformExtension.() -> Unit
) {
    extensions.getByType<KotlinMultiplatformExtension>().apply(block)
}

internal fun KotlinMultiplatformExtension.androidLibrary(
    block: KotlinMultiplatformAndroidLibraryExtension.() -> Unit
) {
    extensions.getByType<KotlinMultiplatformAndroidLibraryExtension>().apply(block)
}

internal fun DependencyHandlerScope.commonMainImplementation(
    dependencyNotation: Any
) {
    add("commonMainImplementation", dependencyNotation)
}

val NamedDomainObjectContainer<KotlinSourceSet>.androidJvmMain
    get() = maybeCreate("androidJvmMain").also {
        it.dependsOn(getByName("commonMain"))
        getByName("androidMain").dependsOn(it)
        getByName("jvmMain").dependsOn(it)
    }
