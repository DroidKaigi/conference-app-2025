/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress(
    "unused",
    "nothing_to_inline",
    "useless_cast",
    "unchecked_cast",
    "extension_shadowed_by_member",
    "redundant_projection",
    "RemoveRedundantBackticks",
    "ObjectPropertyName",
    "deprecation",
    "detekt:all"
)
@file:org.gradle.api.Generated

package gradle.kotlin.dsl.plugins._678468e46f34d22f7c035454ddd91d7c

import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec


/**
 * The `android` plugin implemented by [com.android.build.gradle.AppPlugin].
 */
internal
val `PluginDependenciesSpec`.`android`: PluginDependencySpec
    get() = this.id("android")


/**
 * The `android-library` plugin implemented by [com.android.build.gradle.LibraryPlugin].
 */
internal
val `PluginDependenciesSpec`.`android-library`: PluginDependencySpec
    get() = this.id("android-library")


/**
 * The `android-reporting` plugin implemented by [com.android.build.gradle.ReportingPlugin].
 */
internal
val `PluginDependenciesSpec`.`android-reporting`: PluginDependencySpec
    get() = this.id("android-reporting")


/**
 * The `co` plugin group.
 */
@org.gradle.api.Generated
internal
class `CoPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `co`.
 */
internal
val `PluginDependenciesSpec`.`co`: `CoPluginGroup`
    get() = `CoPluginGroup`(this)


/**
 * The `co.touchlab` plugin group.
 */
@org.gradle.api.Generated
internal
class `CoTouchlabPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `co.touchlab`.
 */
internal
val `CoPluginGroup`.`touchlab`: `CoTouchlabPluginGroup`
    get() = `CoTouchlabPluginGroup`(plugins)


/**
 * The `co.touchlab.skie` plugin implemented by [co.touchlab.skie.plugin.SkieGradlePlugin].
 */
internal
val `CoTouchlabPluginGroup`.`skie`: PluginDependencySpec
    get() = plugins.id("co.touchlab.skie")


/**
 * The `com` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com`.
 */
internal
val `PluginDependenciesSpec`.`com`: `ComPluginGroup`
    get() = `ComPluginGroup`(this)


/**
 * The `com.android` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComAndroidPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.android`.
 */
internal
val `ComPluginGroup`.`android`: `ComAndroidPluginGroup`
    get() = `ComAndroidPluginGroup`(plugins)


/**
 * The `com.android.ai-pack` plugin implemented by [com.android.build.gradle.AiPackPlugin].
 */
internal
val `ComAndroidPluginGroup`.`ai-pack`: PluginDependencySpec
    get() = plugins.id("com.android.ai-pack")


/**
 * The `com.android.application` plugin implemented by [com.android.build.gradle.AppPlugin].
 */
internal
val `ComAndroidPluginGroup`.`application`: PluginDependencySpec
    get() = plugins.id("com.android.application")


/**
 * The `com.android.asset-pack` plugin implemented by [com.android.build.gradle.AssetPackPlugin].
 */
internal
val `ComAndroidPluginGroup`.`asset-pack`: PluginDependencySpec
    get() = plugins.id("com.android.asset-pack")


/**
 * The `com.android.asset-pack-bundle` plugin implemented by [com.android.build.gradle.AssetPackBundlePlugin].
 */
internal
val `ComAndroidPluginGroup`.`asset-pack-bundle`: PluginDependencySpec
    get() = plugins.id("com.android.asset-pack-bundle")


/**
 * The `com.android.base` plugin implemented by [com.android.build.gradle.api.AndroidBasePlugin].
 */
internal
val `ComAndroidPluginGroup`.`base`: PluginDependencySpec
    get() = plugins.id("com.android.base")


/**
 * The `com.android.dynamic-feature` plugin implemented by [com.android.build.gradle.DynamicFeaturePlugin].
 */
internal
val `ComAndroidPluginGroup`.`dynamic-feature`: PluginDependencySpec
    get() = plugins.id("com.android.dynamic-feature")


/**
 * The `com.android.fused-library` plugin implemented by [com.android.build.gradle.api.FusedLibraryPlugin].
 */
internal
val `ComAndroidPluginGroup`.`fused-library`: PluginDependencySpec
    get() = plugins.id("com.android.fused-library")


/**
 * The `com.android.internal` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComAndroidInternalPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.android.internal`.
 */
internal
val `ComAndroidPluginGroup`.`internal`: `ComAndroidInternalPluginGroup`
    get() = `ComAndroidInternalPluginGroup`(plugins)


/**
 * The `com.android.internal.ai-pack` plugin implemented by [com.android.build.gradle.internal.plugins.AiPackPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`ai-pack`: PluginDependencySpec
    get() = plugins.id("com.android.internal.ai-pack")


/**
 * The `com.android.internal.application` plugin implemented by [com.android.build.gradle.internal.plugins.AppPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`application`: PluginDependencySpec
    get() = plugins.id("com.android.internal.application")


/**
 * The `com.android.internal.asset-pack` plugin implemented by [com.android.build.gradle.internal.plugins.AssetPackPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`asset-pack`: PluginDependencySpec
    get() = plugins.id("com.android.internal.asset-pack")


/**
 * The `com.android.internal.asset-pack-bundle` plugin implemented by [com.android.build.gradle.internal.plugins.AssetPackBundlePlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`asset-pack-bundle`: PluginDependencySpec
    get() = plugins.id("com.android.internal.asset-pack-bundle")


/**
 * The `com.android.internal.dynamic-feature` plugin implemented by [com.android.build.gradle.internal.plugins.DynamicFeaturePlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`dynamic-feature`: PluginDependencySpec
    get() = plugins.id("com.android.internal.dynamic-feature")


/**
 * The `com.android.internal.fused-library` plugin implemented by [com.android.build.gradle.internal.plugins.FusedLibraryPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`fused-library`: PluginDependencySpec
    get() = plugins.id("com.android.internal.fused-library")


/**
 * The `com.android.internal.kotlin` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComAndroidInternalKotlinPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.android.internal.kotlin`.
 */
internal
val `ComAndroidInternalPluginGroup`.`kotlin`: `ComAndroidInternalKotlinPluginGroup`
    get() = `ComAndroidInternalKotlinPluginGroup`(plugins)


/**
 * The `com.android.internal.kotlin.multiplatform` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComAndroidInternalKotlinMultiplatformPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.android.internal.kotlin.multiplatform`.
 */
internal
val `ComAndroidInternalKotlinPluginGroup`.`multiplatform`: `ComAndroidInternalKotlinMultiplatformPluginGroup`
    get() = `ComAndroidInternalKotlinMultiplatformPluginGroup`(plugins)


/**
 * The `com.android.internal.kotlin.multiplatform.library` plugin implemented by [com.android.build.gradle.internal.plugins.KotlinMultiplatformAndroidPlugin].
 */
internal
val `ComAndroidInternalKotlinMultiplatformPluginGroup`.`library`: PluginDependencySpec
    get() = plugins.id("com.android.internal.kotlin.multiplatform.library")


/**
 * The `com.android.internal.library` plugin implemented by [com.android.build.gradle.internal.plugins.LibraryPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`library`: PluginDependencySpec
    get() = plugins.id("com.android.internal.library")


/**
 * The `com.android.internal.lint` plugin implemented by [com.android.build.gradle.internal.plugins.LintPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`lint`: PluginDependencySpec
    get() = plugins.id("com.android.internal.lint")


/**
 * The `com.android.internal.privacy-sandbox-sdk` plugin implemented by [com.android.build.gradle.internal.plugins.PrivacySandboxSdkPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`privacy-sandbox-sdk`: PluginDependencySpec
    get() = plugins.id("com.android.internal.privacy-sandbox-sdk")


/**
 * The `com.android.internal.reporting` plugin implemented by [com.android.build.gradle.internal.plugins.ReportingPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`reporting`: PluginDependencySpec
    get() = plugins.id("com.android.internal.reporting")


/**
 * The `com.android.internal.test` plugin implemented by [com.android.build.gradle.internal.plugins.TestPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`test`: PluginDependencySpec
    get() = plugins.id("com.android.internal.test")


/**
 * The `com.android.internal.version-check` plugin implemented by [com.android.build.gradle.internal.plugins.VersionCheckPlugin].
 */
internal
val `ComAndroidInternalPluginGroup`.`version-check`: PluginDependencySpec
    get() = plugins.id("com.android.internal.version-check")


/**
 * The `com.android.kotlin` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComAndroidKotlinPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.android.kotlin`.
 */
internal
val `ComAndroidPluginGroup`.`kotlin`: `ComAndroidKotlinPluginGroup`
    get() = `ComAndroidKotlinPluginGroup`(plugins)


/**
 * The `com.android.kotlin.multiplatform` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComAndroidKotlinMultiplatformPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.android.kotlin.multiplatform`.
 */
internal
val `ComAndroidKotlinPluginGroup`.`multiplatform`: `ComAndroidKotlinMultiplatformPluginGroup`
    get() = `ComAndroidKotlinMultiplatformPluginGroup`(plugins)


/**
 * The `com.android.kotlin.multiplatform.library` plugin implemented by [com.android.build.gradle.api.KotlinMultiplatformAndroidPlugin].
 */
internal
val `ComAndroidKotlinMultiplatformPluginGroup`.`library`: PluginDependencySpec
    get() = plugins.id("com.android.kotlin.multiplatform.library")


/**
 * The `com.android.library` plugin implemented by [com.android.build.gradle.LibraryPlugin].
 */
internal
val `ComAndroidPluginGroup`.`library`: PluginDependencySpec
    get() = plugins.id("com.android.library")


/**
 * The `com.android.lint` plugin implemented by [com.android.build.gradle.LintPlugin].
 */
internal
val `ComAndroidPluginGroup`.`lint`: PluginDependencySpec
    get() = plugins.id("com.android.lint")


/**
 * The `com.android.privacy-sandbox-sdk` plugin implemented by [com.android.build.gradle.api.PrivacySandboxSdkPlugin].
 */
internal
val `ComAndroidPluginGroup`.`privacy-sandbox-sdk`: PluginDependencySpec
    get() = plugins.id("com.android.privacy-sandbox-sdk")


/**
 * The `com.android.reporting` plugin implemented by [com.android.build.gradle.ReportingPlugin].
 */
internal
val `ComAndroidPluginGroup`.`reporting`: PluginDependencySpec
    get() = plugins.id("com.android.reporting")


/**
 * The `com.android.test` plugin implemented by [com.android.build.gradle.TestPlugin].
 */
internal
val `ComAndroidPluginGroup`.`test`: PluginDependencySpec
    get() = plugins.id("com.android.test")


/**
 * The `com.codingfeline` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComCodingfelinePluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.codingfeline`.
 */
internal
val `ComPluginGroup`.`codingfeline`: `ComCodingfelinePluginGroup`
    get() = `ComCodingfelinePluginGroup`(plugins)


/**
 * The `com.codingfeline.buildkonfig` plugin implemented by [com.codingfeline.buildkonfig.gradle.BuildKonfigPlugin].
 */
internal
val `ComCodingfelinePluginGroup`.`buildkonfig`: PluginDependencySpec
    get() = plugins.id("com.codingfeline.buildkonfig")


/**
 * The `com.google` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComGooglePluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.google`.
 */
internal
val `ComPluginGroup`.`google`: `ComGooglePluginGroup`
    get() = `ComGooglePluginGroup`(plugins)


/**
 * The `com.google.devtools` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComGoogleDevtoolsPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.google.devtools`.
 */
internal
val `ComGooglePluginGroup`.`devtools`: `ComGoogleDevtoolsPluginGroup`
    get() = `ComGoogleDevtoolsPluginGroup`(plugins)


/**
 * The `com.google.devtools.ksp` plugin implemented by [com.google.devtools.ksp.gradle.KspGradleSubplugin].
 */
internal
val `ComGoogleDevtoolsPluginGroup`.`ksp`: PluginDependencySpec
    get() = plugins.id("com.google.devtools.ksp")


/**
 * The `com.mikepenz` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComMikepenzPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.mikepenz`.
 */
internal
val `ComPluginGroup`.`mikepenz`: `ComMikepenzPluginGroup`
    get() = `ComMikepenzPluginGroup`(plugins)


/**
 * The `com.mikepenz.aboutlibraries` plugin group.
 */
@org.gradle.api.Generated
internal
class `ComMikepenzAboutlibrariesPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `com.mikepenz.aboutlibraries`.
 */
internal
val `ComMikepenzPluginGroup`.`aboutlibraries`: `ComMikepenzAboutlibrariesPluginGroup`
    get() = `ComMikepenzAboutlibrariesPluginGroup`(plugins)


/**
 * The `com.mikepenz.aboutlibraries.plugin` plugin implemented by [com.mikepenz.aboutlibraries.plugin.AboutLibrariesPlugin].
 */
internal
val `ComMikepenzAboutlibrariesPluginGroup`.`plugin`: PluginDependencySpec
    get() = plugins.id("com.mikepenz.aboutlibraries.plugin")


/**
 * The `dev` plugin group.
 */
@org.gradle.api.Generated
internal
class `DevPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `dev`.
 */
internal
val `PluginDependenciesSpec`.`dev`: `DevPluginGroup`
    get() = `DevPluginGroup`(this)


/**
 * The `dev.zacsweers` plugin group.
 */
@org.gradle.api.Generated
internal
class `DevZacsweersPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `dev.zacsweers`.
 */
internal
val `DevPluginGroup`.`zacsweers`: `DevZacsweersPluginGroup`
    get() = `DevZacsweersPluginGroup`(plugins)


/**
 * The `dev.zacsweers.metro` plugin implemented by [dev.zacsweers.metro.gradle.MetroGradleSubplugin].
 */
internal
val `DevZacsweersPluginGroup`.`metro`: PluginDependencySpec
    get() = plugins.id("dev.zacsweers.metro")


/**
 * The `io` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io`.
 */
internal
val `PluginDependenciesSpec`.`io`: `IoPluginGroup`
    get() = `IoPluginGroup`(this)


/**
 * The `io.github` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoGithubPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io.github`.
 */
internal
val `IoPluginGroup`.`github`: `IoGithubPluginGroup`
    get() = `IoGithubPluginGroup`(plugins)


/**
 * The `io.github.detekt` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoGithubDetektPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io.github.detekt`.
 */
internal
val `IoGithubPluginGroup`.`detekt`: `IoGithubDetektPluginGroup`
    get() = `IoGithubDetektPluginGroup`(plugins)


/**
 * The `io.github.detekt.gradle` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoGithubDetektGradlePluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io.github.detekt.gradle`.
 */
internal
val `IoGithubDetektPluginGroup`.`gradle`: `IoGithubDetektGradlePluginGroup`
    get() = `IoGithubDetektGradlePluginGroup`(plugins)


/**
 * The `io.github.detekt.gradle.compiler-plugin` plugin implemented by [io.github.detekt.gradle.DetektKotlinCompilerPlugin].
 */
internal
val `IoGithubDetektGradlePluginGroup`.`compiler-plugin`: PluginDependencySpec
    get() = plugins.id("io.github.detekt.gradle.compiler-plugin")


/**
 * The `io.github.takahirom` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoGithubTakahiromPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io.github.takahirom`.
 */
internal
val `IoGithubPluginGroup`.`takahirom`: `IoGithubTakahiromPluginGroup`
    get() = `IoGithubTakahiromPluginGroup`(plugins)


/**
 * The `io.github.takahirom.roborazzi` plugin implemented by [io.github.takahirom.roborazzi.RoborazziPlugin].
 */
internal
val `IoGithubTakahiromPluginGroup`.`roborazzi`: PluginDependencySpec
    get() = plugins.id("io.github.takahirom.roborazzi")


/**
 * The `io.gitlab` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoGitlabPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io.gitlab`.
 */
internal
val `IoPluginGroup`.`gitlab`: `IoGitlabPluginGroup`
    get() = `IoGitlabPluginGroup`(plugins)


/**
 * The `io.gitlab.arturbosch` plugin group.
 */
@org.gradle.api.Generated
internal
class `IoGitlabArturboschPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `io.gitlab.arturbosch`.
 */
internal
val `IoGitlabPluginGroup`.`arturbosch`: `IoGitlabArturboschPluginGroup`
    get() = `IoGitlabArturboschPluginGroup`(plugins)


/**
 * The `io.gitlab.arturbosch.detekt` plugin implemented by [io.gitlab.arturbosch.detekt.DetektPlugin].
 */
internal
val `IoGitlabArturboschPluginGroup`.`detekt`: PluginDependencySpec
    get() = plugins.id("io.gitlab.arturbosch.detekt")


/**
 * The `kotlin` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper].
 */
internal
val `PluginDependenciesSpec`.`kotlin`: PluginDependencySpec
    get() = this.id("kotlin")


/**
 * The `kotlin-android` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper].
 */
internal
val `PluginDependenciesSpec`.`kotlin-android`: PluginDependencySpec
    get() = this.id("kotlin-android")


/**
 * The `kotlin-android-extensions` plugin implemented by [org.jetbrains.kotlin.gradle.internal.AndroidExtensionsSubpluginIndicator].
 */
internal
val `PluginDependenciesSpec`.`kotlin-android-extensions`: PluginDependencySpec
    get() = this.id("kotlin-android-extensions")


/**
 * The `kotlin-composecompiler` plugin implemented by [org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin].
 */
internal
val `PluginDependenciesSpec`.`kotlin-composecompiler`: PluginDependencySpec
    get() = this.id("kotlin-composecompiler")


/**
 * The `kotlin-kapt` plugin implemented by [org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin].
 */
internal
val `PluginDependenciesSpec`.`kotlin-kapt`: PluginDependencySpec
    get() = this.id("kotlin-kapt")


/**
 * The `kotlin-multiplatform` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper].
 */
internal
val `PluginDependenciesSpec`.`kotlin-multiplatform`: PluginDependencySpec
    get() = this.id("kotlin-multiplatform")


/**
 * The `kotlin-native-cocoapods` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin].
 */
internal
val `PluginDependenciesSpec`.`kotlin-native-cocoapods`: PluginDependencySpec
    get() = this.id("kotlin-native-cocoapods")


/**
 * The `kotlin-parcelize` plugin implemented by [org.jetbrains.kotlin.gradle.internal.ParcelizeSubplugin].
 */
internal
val `PluginDependenciesSpec`.`kotlin-parcelize`: PluginDependencySpec
    get() = this.id("kotlin-parcelize")


/**
 * The `kotlin-scripting` plugin implemented by [org.jetbrains.kotlin.gradle.scripting.internal.ScriptingGradleSubplugin].
 */
internal
val `PluginDependenciesSpec`.`kotlin-scripting`: PluginDependencySpec
    get() = this.id("kotlin-scripting")


/**
 * The `kotlinx-serialization` plugin implemented by [org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin].
 */
internal
val `PluginDependenciesSpec`.`kotlinx-serialization`: PluginDependencySpec
    get() = this.id("kotlinx-serialization")


/**
 * The `org` plugin group.
 */
@org.gradle.api.Generated
internal
class `OrgPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `org`.
 */
internal
val `PluginDependenciesSpec`.`org`: `OrgPluginGroup`
    get() = `OrgPluginGroup`(this)


/**
 * The `org.gradle` plugin group.
 */
@org.gradle.api.Generated
internal
class `OrgGradlePluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `org.gradle`.
 */
internal
val `OrgPluginGroup`.`gradle`: `OrgGradlePluginGroup`
    get() = `OrgGradlePluginGroup`(plugins)


/**
 * The `org.gradle.antlr` plugin implemented by [org.gradle.api.plugins.antlr.AntlrPlugin].
 */
internal
val `OrgGradlePluginGroup`.`antlr`: PluginDependencySpec
    get() = plugins.id("org.gradle.antlr")


/**
 * The `org.gradle.application` plugin implemented by [org.gradle.api.plugins.ApplicationPlugin].
 */
internal
val `OrgGradlePluginGroup`.`application`: PluginDependencySpec
    get() = plugins.id("org.gradle.application")


/**
 * The `org.gradle.assembler` plugin implemented by [org.gradle.language.assembler.plugins.AssemblerPlugin].
 */
internal
val `OrgGradlePluginGroup`.`assembler`: PluginDependencySpec
    get() = plugins.id("org.gradle.assembler")


/**
 * The `org.gradle.assembler-lang` plugin implemented by [org.gradle.language.assembler.plugins.AssemblerLangPlugin].
 */
internal
val `OrgGradlePluginGroup`.`assembler-lang`: PluginDependencySpec
    get() = plugins.id("org.gradle.assembler-lang")


/**
 * The `org.gradle.base` plugin implemented by [org.gradle.api.plugins.BasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`base`: PluginDependencySpec
    get() = plugins.id("org.gradle.base")


/**
 * The `org.gradle.binary-base` plugin implemented by [org.gradle.platform.base.plugins.BinaryBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`binary-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.binary-base")


/**
 * The `org.gradle.build-dashboard` plugin implemented by [org.gradle.api.reporting.plugins.BuildDashboardPlugin].
 */
internal
val `OrgGradlePluginGroup`.`build-dashboard`: PluginDependencySpec
    get() = plugins.id("org.gradle.build-dashboard")


/**
 * The `org.gradle.build-init` plugin implemented by [org.gradle.buildinit.plugins.BuildInitPlugin].
 */
internal
val `OrgGradlePluginGroup`.`build-init`: PluginDependencySpec
    get() = plugins.id("org.gradle.build-init")


/**
 * The `org.gradle.c` plugin implemented by [org.gradle.language.c.plugins.CPlugin].
 */
internal
val `OrgGradlePluginGroup`.`c`: PluginDependencySpec
    get() = plugins.id("org.gradle.c")


/**
 * The `org.gradle.c-lang` plugin implemented by [org.gradle.language.c.plugins.CLangPlugin].
 */
internal
val `OrgGradlePluginGroup`.`c-lang`: PluginDependencySpec
    get() = plugins.id("org.gradle.c-lang")


/**
 * The `org.gradle.checkstyle` plugin implemented by [org.gradle.api.plugins.quality.CheckstylePlugin].
 */
internal
val `OrgGradlePluginGroup`.`checkstyle`: PluginDependencySpec
    get() = plugins.id("org.gradle.checkstyle")


/**
 * The `org.gradle.clang-compiler` plugin implemented by [org.gradle.nativeplatform.toolchain.plugins.ClangCompilerPlugin].
 */
internal
val `OrgGradlePluginGroup`.`clang-compiler`: PluginDependencySpec
    get() = plugins.id("org.gradle.clang-compiler")


/**
 * The `org.gradle.codenarc` plugin implemented by [org.gradle.api.plugins.quality.CodeNarcPlugin].
 */
internal
val `OrgGradlePluginGroup`.`codenarc`: PluginDependencySpec
    get() = plugins.id("org.gradle.codenarc")


/**
 * The `org.gradle.component-base` plugin implemented by [org.gradle.platform.base.plugins.ComponentBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`component-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.component-base")


/**
 * The `org.gradle.component-model-base` plugin implemented by [org.gradle.language.base.plugins.ComponentModelBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`component-model-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.component-model-base")


/**
 * The `org.gradle.component-reporting-tasks` plugin implemented by [org.gradle.api.plugins.internal.ComponentReportingTasksPlugin].
 */
internal
val `OrgGradlePluginGroup`.`component-reporting-tasks`: PluginDependencySpec
    get() = plugins.id("org.gradle.component-reporting-tasks")


/**
 * The `org.gradle.cpp` plugin implemented by [org.gradle.language.cpp.plugins.CppPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cpp`: PluginDependencySpec
    get() = plugins.id("org.gradle.cpp")


/**
 * The `org.gradle.cpp-application` plugin implemented by [org.gradle.language.cpp.plugins.CppApplicationPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cpp-application`: PluginDependencySpec
    get() = plugins.id("org.gradle.cpp-application")


/**
 * The `org.gradle.cpp-lang` plugin implemented by [org.gradle.language.cpp.plugins.CppLangPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cpp-lang`: PluginDependencySpec
    get() = plugins.id("org.gradle.cpp-lang")


/**
 * The `org.gradle.cpp-library` plugin implemented by [org.gradle.language.cpp.plugins.CppLibraryPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cpp-library`: PluginDependencySpec
    get() = plugins.id("org.gradle.cpp-library")


/**
 * The `org.gradle.cpp-unit-test` plugin implemented by [org.gradle.nativeplatform.test.cpp.plugins.CppUnitTestPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cpp-unit-test`: PluginDependencySpec
    get() = plugins.id("org.gradle.cpp-unit-test")


/**
 * The `org.gradle.cunit` plugin implemented by [org.gradle.nativeplatform.test.cunit.plugins.CUnitConventionPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cunit`: PluginDependencySpec
    get() = plugins.id("org.gradle.cunit")


/**
 * The `org.gradle.cunit-test-suite` plugin implemented by [org.gradle.nativeplatform.test.cunit.plugins.CUnitPlugin].
 */
internal
val `OrgGradlePluginGroup`.`cunit-test-suite`: PluginDependencySpec
    get() = plugins.id("org.gradle.cunit-test-suite")


/**
 * The `org.gradle.distribution` plugin implemented by [org.gradle.api.distribution.plugins.DistributionPlugin].
 */
internal
val `OrgGradlePluginGroup`.`distribution`: PluginDependencySpec
    get() = plugins.id("org.gradle.distribution")


/**
 * The `org.gradle.distribution-base` plugin implemented by [org.gradle.api.distribution.plugins.DistributionBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`distribution-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.distribution-base")


/**
 * The `org.gradle.ear` plugin implemented by [org.gradle.plugins.ear.EarPlugin].
 */
internal
val `OrgGradlePluginGroup`.`ear`: PluginDependencySpec
    get() = plugins.id("org.gradle.ear")


/**
 * The `org.gradle.eclipse` plugin implemented by [org.gradle.plugins.ide.eclipse.EclipsePlugin].
 */
internal
val `OrgGradlePluginGroup`.`eclipse`: PluginDependencySpec
    get() = plugins.id("org.gradle.eclipse")


/**
 * The `org.gradle.eclipse-wtp` plugin implemented by [org.gradle.plugins.ide.eclipse.EclipseWtpPlugin].
 */
internal
val `OrgGradlePluginGroup`.`eclipse-wtp`: PluginDependencySpec
    get() = plugins.id("org.gradle.eclipse-wtp")


/**
 * The `org.gradle.gcc-compiler` plugin implemented by [org.gradle.nativeplatform.toolchain.plugins.GccCompilerPlugin].
 */
internal
val `OrgGradlePluginGroup`.`gcc-compiler`: PluginDependencySpec
    get() = plugins.id("org.gradle.gcc-compiler")


/**
 * The `org.gradle.google-test` plugin implemented by [org.gradle.nativeplatform.test.googletest.plugins.GoogleTestConventionPlugin].
 */
internal
val `OrgGradlePluginGroup`.`google-test`: PluginDependencySpec
    get() = plugins.id("org.gradle.google-test")


/**
 * The `org.gradle.google-test-test-suite` plugin implemented by [org.gradle.nativeplatform.test.googletest.plugins.GoogleTestPlugin].
 */
internal
val `OrgGradlePluginGroup`.`google-test-test-suite`: PluginDependencySpec
    get() = plugins.id("org.gradle.google-test-test-suite")


/**
 * The `org.gradle.groovy` plugin implemented by [org.gradle.api.plugins.GroovyPlugin].
 */
internal
val `OrgGradlePluginGroup`.`groovy`: PluginDependencySpec
    get() = plugins.id("org.gradle.groovy")


/**
 * The `org.gradle.groovy-base` plugin implemented by [org.gradle.api.plugins.GroovyBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`groovy-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.groovy-base")


/**
 * The `org.gradle.groovy-gradle-plugin` plugin implemented by [org.gradle.plugin.devel.internal.precompiled.PrecompiledGroovyPluginsPlugin].
 */
internal
val `OrgGradlePluginGroup`.`groovy-gradle-plugin`: PluginDependencySpec
    get() = plugins.id("org.gradle.groovy-gradle-plugin")


/**
 * The `org.gradle.help-tasks` plugin implemented by [org.gradle.api.plugins.HelpTasksPlugin].
 */
internal
val `OrgGradlePluginGroup`.`help-tasks`: PluginDependencySpec
    get() = plugins.id("org.gradle.help-tasks")


/**
 * The `org.gradle.idea` plugin implemented by [org.gradle.plugins.ide.idea.IdeaPlugin].
 */
internal
val `OrgGradlePluginGroup`.`idea`: PluginDependencySpec
    get() = plugins.id("org.gradle.idea")


/**
 * The `org.gradle.ivy-publish` plugin implemented by [org.gradle.api.publish.ivy.plugins.IvyPublishPlugin].
 */
internal
val `OrgGradlePluginGroup`.`ivy-publish`: PluginDependencySpec
    get() = plugins.id("org.gradle.ivy-publish")


/**
 * The `org.gradle.jacoco` plugin implemented by [org.gradle.testing.jacoco.plugins.JacocoPlugin].
 */
internal
val `OrgGradlePluginGroup`.`jacoco`: PluginDependencySpec
    get() = plugins.id("org.gradle.jacoco")


/**
 * The `org.gradle.jacoco-report-aggregation` plugin implemented by [org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin].
 */
internal
val `OrgGradlePluginGroup`.`jacoco-report-aggregation`: PluginDependencySpec
    get() = plugins.id("org.gradle.jacoco-report-aggregation")


/**
 * The `org.gradle.java` plugin implemented by [org.gradle.api.plugins.JavaPlugin].
 */
internal
val `OrgGradlePluginGroup`.`java`: PluginDependencySpec
    get() = plugins.id("org.gradle.java")


/**
 * The `org.gradle.java-base` plugin implemented by [org.gradle.api.plugins.JavaBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`java-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.java-base")


/**
 * The `org.gradle.java-gradle-plugin` plugin implemented by [org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin].
 */
internal
val `OrgGradlePluginGroup`.`java-gradle-plugin`: PluginDependencySpec
    get() = plugins.id("org.gradle.java-gradle-plugin")


/**
 * The `org.gradle.java-library` plugin implemented by [org.gradle.api.plugins.JavaLibraryPlugin].
 */
internal
val `OrgGradlePluginGroup`.`java-library`: PluginDependencySpec
    get() = plugins.id("org.gradle.java-library")


/**
 * The `org.gradle.java-library-distribution` plugin implemented by [org.gradle.api.plugins.JavaLibraryDistributionPlugin].
 */
internal
val `OrgGradlePluginGroup`.`java-library-distribution`: PluginDependencySpec
    get() = plugins.id("org.gradle.java-library-distribution")


/**
 * The `org.gradle.java-platform` plugin implemented by [org.gradle.api.plugins.JavaPlatformPlugin].
 */
internal
val `OrgGradlePluginGroup`.`java-platform`: PluginDependencySpec
    get() = plugins.id("org.gradle.java-platform")


/**
 * The `org.gradle.java-test-fixtures` plugin implemented by [org.gradle.api.plugins.JavaTestFixturesPlugin].
 */
internal
val `OrgGradlePluginGroup`.`java-test-fixtures`: PluginDependencySpec
    get() = plugins.id("org.gradle.java-test-fixtures")


/**
 * The `org.gradle.jvm-ecosystem` plugin implemented by [org.gradle.api.plugins.JvmEcosystemPlugin].
 */
internal
val `OrgGradlePluginGroup`.`jvm-ecosystem`: PluginDependencySpec
    get() = plugins.id("org.gradle.jvm-ecosystem")


/**
 * The `org.gradle.jvm-test-suite` plugin implemented by [org.gradle.api.plugins.JvmTestSuitePlugin].
 */
internal
val `OrgGradlePluginGroup`.`jvm-test-suite`: PluginDependencySpec
    get() = plugins.id("org.gradle.jvm-test-suite")


/**
 * The `org.gradle.jvm-toolchain-management` plugin implemented by [org.gradle.api.plugins.JvmToolchainManagementPlugin].
 */
internal
val `OrgGradlePluginGroup`.`jvm-toolchain-management`: PluginDependencySpec
    get() = plugins.id("org.gradle.jvm-toolchain-management")


/**
 * The `org.gradle.jvm-toolchains` plugin implemented by [org.gradle.api.plugins.JvmToolchainsPlugin].
 */
internal
val `OrgGradlePluginGroup`.`jvm-toolchains`: PluginDependencySpec
    get() = plugins.id("org.gradle.jvm-toolchains")


/**
 * The `org.gradle.language-base` plugin implemented by [org.gradle.language.base.plugins.LanguageBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`language-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.language-base")


/**
 * The `org.gradle.lifecycle-base` plugin implemented by [org.gradle.language.base.plugins.LifecycleBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`lifecycle-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.lifecycle-base")


/**
 * The `org.gradle.maven-publish` plugin implemented by [org.gradle.api.publish.maven.plugins.MavenPublishPlugin].
 */
internal
val `OrgGradlePluginGroup`.`maven-publish`: PluginDependencySpec
    get() = plugins.id("org.gradle.maven-publish")


/**
 * The `org.gradle.microsoft-visual-cpp-compiler` plugin implemented by [org.gradle.nativeplatform.toolchain.plugins.MicrosoftVisualCppCompilerPlugin].
 */
internal
val `OrgGradlePluginGroup`.`microsoft-visual-cpp-compiler`: PluginDependencySpec
    get() = plugins.id("org.gradle.microsoft-visual-cpp-compiler")


/**
 * The `org.gradle.model-reporting-tasks` plugin implemented by [org.gradle.api.plugins.internal.ModelReportingTasksPlugin].
 */
internal
val `OrgGradlePluginGroup`.`model-reporting-tasks`: PluginDependencySpec
    get() = plugins.id("org.gradle.model-reporting-tasks")


/**
 * The `org.gradle.native-component` plugin implemented by [org.gradle.nativeplatform.plugins.NativeComponentPlugin].
 */
internal
val `OrgGradlePluginGroup`.`native-component`: PluginDependencySpec
    get() = plugins.id("org.gradle.native-component")


/**
 * The `org.gradle.native-component-model` plugin implemented by [org.gradle.nativeplatform.plugins.NativeComponentModelPlugin].
 */
internal
val `OrgGradlePluginGroup`.`native-component-model`: PluginDependencySpec
    get() = plugins.id("org.gradle.native-component-model")


/**
 * The `org.gradle.objective-c` plugin implemented by [org.gradle.language.objectivec.plugins.ObjectiveCPlugin].
 */
internal
val `OrgGradlePluginGroup`.`objective-c`: PluginDependencySpec
    get() = plugins.id("org.gradle.objective-c")


/**
 * The `org.gradle.objective-c-lang` plugin implemented by [org.gradle.language.objectivec.plugins.ObjectiveCLangPlugin].
 */
internal
val `OrgGradlePluginGroup`.`objective-c-lang`: PluginDependencySpec
    get() = plugins.id("org.gradle.objective-c-lang")


/**
 * The `org.gradle.objective-cpp` plugin implemented by [org.gradle.language.objectivecpp.plugins.ObjectiveCppPlugin].
 */
internal
val `OrgGradlePluginGroup`.`objective-cpp`: PluginDependencySpec
    get() = plugins.id("org.gradle.objective-cpp")


/**
 * The `org.gradle.objective-cpp-lang` plugin implemented by [org.gradle.language.objectivecpp.plugins.ObjectiveCppLangPlugin].
 */
internal
val `OrgGradlePluginGroup`.`objective-cpp-lang`: PluginDependencySpec
    get() = plugins.id("org.gradle.objective-cpp-lang")


/**
 * The `org.gradle.pmd` plugin implemented by [org.gradle.api.plugins.quality.PmdPlugin].
 */
internal
val `OrgGradlePluginGroup`.`pmd`: PluginDependencySpec
    get() = plugins.id("org.gradle.pmd")


/**
 * The `org.gradle.project-report` plugin implemented by [org.gradle.api.plugins.ProjectReportsPlugin].
 */
internal
val `OrgGradlePluginGroup`.`project-report`: PluginDependencySpec
    get() = plugins.id("org.gradle.project-report")


/**
 * The `org.gradle.project-reports` plugin implemented by [org.gradle.api.plugins.ProjectReportsPlugin].
 */
internal
val `OrgGradlePluginGroup`.`project-reports`: PluginDependencySpec
    get() = plugins.id("org.gradle.project-reports")


/**
 * The `org.gradle.publishing` plugin implemented by [org.gradle.api.publish.plugins.PublishingPlugin].
 */
internal
val `OrgGradlePluginGroup`.`publishing`: PluginDependencySpec
    get() = plugins.id("org.gradle.publishing")


/**
 * The `org.gradle.reporting-base` plugin implemented by [org.gradle.api.plugins.ReportingBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`reporting-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.reporting-base")


/**
 * The `org.gradle.scala` plugin implemented by [org.gradle.api.plugins.scala.ScalaPlugin].
 */
internal
val `OrgGradlePluginGroup`.`scala`: PluginDependencySpec
    get() = plugins.id("org.gradle.scala")


/**
 * The `org.gradle.scala-base` plugin implemented by [org.gradle.api.plugins.scala.ScalaBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`scala-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.scala-base")


/**
 * The `org.gradle.signing` plugin implemented by [org.gradle.plugins.signing.SigningPlugin].
 */
internal
val `OrgGradlePluginGroup`.`signing`: PluginDependencySpec
    get() = plugins.id("org.gradle.signing")


/**
 * The `org.gradle.software-reporting-tasks` plugin implemented by [org.gradle.api.plugins.SoftwareReportingTasksPlugin].
 */
internal
val `OrgGradlePluginGroup`.`software-reporting-tasks`: PluginDependencySpec
    get() = plugins.id("org.gradle.software-reporting-tasks")


/**
 * The `org.gradle.standard-tool-chains` plugin implemented by [org.gradle.nativeplatform.toolchain.internal.plugins.StandardToolChainsPlugin].
 */
internal
val `OrgGradlePluginGroup`.`standard-tool-chains`: PluginDependencySpec
    get() = plugins.id("org.gradle.standard-tool-chains")


/**
 * The `org.gradle.swift-application` plugin implemented by [org.gradle.language.swift.plugins.SwiftApplicationPlugin].
 */
internal
val `OrgGradlePluginGroup`.`swift-application`: PluginDependencySpec
    get() = plugins.id("org.gradle.swift-application")


/**
 * The `org.gradle.swift-library` plugin implemented by [org.gradle.language.swift.plugins.SwiftLibraryPlugin].
 */
internal
val `OrgGradlePluginGroup`.`swift-library`: PluginDependencySpec
    get() = plugins.id("org.gradle.swift-library")


/**
 * The `org.gradle.swiftpm-export` plugin implemented by [org.gradle.swiftpm.plugins.SwiftPackageManagerExportPlugin].
 */
internal
val `OrgGradlePluginGroup`.`swiftpm-export`: PluginDependencySpec
    get() = plugins.id("org.gradle.swiftpm-export")


/**
 * The `org.gradle.test-report-aggregation` plugin implemented by [org.gradle.api.plugins.TestReportAggregationPlugin].
 */
internal
val `OrgGradlePluginGroup`.`test-report-aggregation`: PluginDependencySpec
    get() = plugins.id("org.gradle.test-report-aggregation")


/**
 * The `org.gradle.test-suite-base` plugin implemented by [org.gradle.testing.base.plugins.TestSuiteBasePlugin].
 */
internal
val `OrgGradlePluginGroup`.`test-suite-base`: PluginDependencySpec
    get() = plugins.id("org.gradle.test-suite-base")


/**
 * The `org.gradle.version-catalog` plugin implemented by [org.gradle.api.plugins.catalog.VersionCatalogPlugin].
 */
internal
val `OrgGradlePluginGroup`.`version-catalog`: PluginDependencySpec
    get() = plugins.id("org.gradle.version-catalog")


/**
 * The `org.gradle.visual-studio` plugin implemented by [org.gradle.ide.visualstudio.plugins.VisualStudioPlugin].
 */
internal
val `OrgGradlePluginGroup`.`visual-studio`: PluginDependencySpec
    get() = plugins.id("org.gradle.visual-studio")


/**
 * The `org.gradle.war` plugin implemented by [org.gradle.api.plugins.WarPlugin].
 */
internal
val `OrgGradlePluginGroup`.`war`: PluginDependencySpec
    get() = plugins.id("org.gradle.war")


/**
 * The `org.gradle.windows-resource-script` plugin implemented by [org.gradle.language.rc.plugins.WindowsResourceScriptPlugin].
 */
internal
val `OrgGradlePluginGroup`.`windows-resource-script`: PluginDependencySpec
    get() = plugins.id("org.gradle.windows-resource-script")


/**
 * The `org.gradle.windows-resources` plugin implemented by [org.gradle.language.rc.plugins.WindowsResourcesPlugin].
 */
internal
val `OrgGradlePluginGroup`.`windows-resources`: PluginDependencySpec
    get() = plugins.id("org.gradle.windows-resources")


/**
 * The `org.gradle.wrapper` plugin implemented by [org.gradle.buildinit.plugins.WrapperPlugin].
 */
internal
val `OrgGradlePluginGroup`.`wrapper`: PluginDependencySpec
    get() = plugins.id("org.gradle.wrapper")


/**
 * The `org.gradle.xcode` plugin implemented by [org.gradle.ide.xcode.plugins.XcodePlugin].
 */
internal
val `OrgGradlePluginGroup`.`xcode`: PluginDependencySpec
    get() = plugins.id("org.gradle.xcode")


/**
 * The `org.gradle.xctest` plugin implemented by [org.gradle.nativeplatform.test.xctest.plugins.XCTestConventionPlugin].
 */
internal
val `OrgGradlePluginGroup`.`xctest`: PluginDependencySpec
    get() = plugins.id("org.gradle.xctest")


/**
 * The `org.jetbrains` plugin group.
 */
@org.gradle.api.Generated
internal
class `OrgJetbrainsPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `org.jetbrains`.
 */
internal
val `OrgPluginGroup`.`jetbrains`: `OrgJetbrainsPluginGroup`
    get() = `OrgJetbrainsPluginGroup`(plugins)


/**
 * The `org.jetbrains.compose` plugin implemented by [org.jetbrains.compose.ComposePlugin].
 */
internal
val `OrgJetbrainsPluginGroup`.`compose`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.compose")


/**
 * The `org.jetbrains.kotlin` plugin group.
 */
@org.gradle.api.Generated
internal
class `OrgJetbrainsKotlinPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `org.jetbrains.kotlin`.
 */
internal
val `OrgJetbrainsPluginGroup`.`kotlin`: `OrgJetbrainsKotlinPluginGroup`
    get() = `OrgJetbrainsKotlinPluginGroup`(plugins)


/**
 * The `org.jetbrains.kotlin.android` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper].
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`android`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.android")


/**
 * The `org.jetbrains.kotlin.fus-statistics-gradle-plugin` plugin implemented by [org.jetbrains.kotlin.gradle.fus.FusStatisticsPlugin].
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`fus-statistics-gradle-plugin`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.fus-statistics-gradle-plugin")


/**
 * The `org.jetbrains.kotlin.js` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinJsPluginWrapper].
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`js`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.js")


/**
 * The `org.jetbrains.kotlin.jvm` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper].
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`jvm`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.jvm")


/**
 * The `org.jetbrains.kotlin.kapt` plugin implemented by [org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin].
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`kapt`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.kapt")


/**
 * The `org.jetbrains.kotlin.multiplatform` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper].
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`multiplatform`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.multiplatform")


/**
 * The `org.jetbrains.kotlin.native` plugin group.
 */
@org.gradle.api.Generated
internal
class `OrgJetbrainsKotlinNativePluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `org.jetbrains.kotlin.native`.
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`native`: `OrgJetbrainsKotlinNativePluginGroup`
    get() = `OrgJetbrainsKotlinNativePluginGroup`(plugins)


/**
 * The `org.jetbrains.kotlin.native.cocoapods` plugin implemented by [org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin].
 */
internal
val `OrgJetbrainsKotlinNativePluginGroup`.`cocoapods`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.native.cocoapods")


/**
 * The `org.jetbrains.kotlin.plugin` plugin group.
 */
@org.gradle.api.Generated
internal
class `OrgJetbrainsKotlinPluginPluginGroup`(internal val plugins: PluginDependenciesSpec)


/**
 * Plugin ids starting with `org.jetbrains.kotlin.plugin`.
 */
internal
val `OrgJetbrainsKotlinPluginGroup`.`plugin`: `OrgJetbrainsKotlinPluginPluginGroup`
    get() = `OrgJetbrainsKotlinPluginPluginGroup`(plugins)


/**
 * The `org.jetbrains.kotlin.plugin.compose` plugin implemented by [org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin].
 */
internal
val `OrgJetbrainsKotlinPluginPluginGroup`.`compose`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.plugin.compose")


/**
 * The `org.jetbrains.kotlin.plugin.parcelize` plugin implemented by [org.jetbrains.kotlin.gradle.internal.ParcelizeSubplugin].
 */
internal
val `OrgJetbrainsKotlinPluginPluginGroup`.`parcelize`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.plugin.parcelize")


/**
 * The `org.jetbrains.kotlin.plugin.scripting` plugin implemented by [org.jetbrains.kotlin.gradle.scripting.internal.ScriptingGradleSubplugin].
 */
internal
val `OrgJetbrainsKotlinPluginPluginGroup`.`scripting`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.plugin.scripting")


/**
 * The `org.jetbrains.kotlin.plugin.serialization` plugin implemented by [org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin].
 */
internal
val `OrgJetbrainsKotlinPluginPluginGroup`.`serialization`: PluginDependencySpec
    get() = plugins.id("org.jetbrains.kotlin.plugin.serialization")
