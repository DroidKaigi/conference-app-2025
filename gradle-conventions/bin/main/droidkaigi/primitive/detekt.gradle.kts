package droidkaigi.primitive

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import util.library
import util.libs

plugins {
    id("io.gitlab.arturbosch.detekt")
}

configure<DetektExtension> {
    // parallel processing
    parallel = true
    // detekt configuration file
    config.setFrom("${project.rootDir}/config/detekt/detekt.yml")
    // baseline configuration file
    baseline = file("${project.rootDir}/config/detekt/baseline.xml")
    // apply your own configuration file on top of the default settings
    buildUponDefaultConfig = true
    // do not let them fail when there is a rule violation
    ignoreFailures = false
    // attempt to automatically correct
    autoCorrect = false
}

val reportMerge = if (!rootProject.tasks.names.contains("reportMerge")) {
    rootProject.tasks.register<ReportMergeTask>("reportMerge") {
        output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.xml"))
    }
} else {
    @Suppress("UNCHECKED_CAST")
    rootProject.tasks.named("reportMerge") as TaskProvider<ReportMergeTask>
}

tasks.withType<Detekt> {
    finalizedBy(reportMerge)

    source = project.files("./").asFileTree

    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")


    reportMerge.configure {
        input.from(xmlReportFile) // or .sarifReportFile
    }
}

dependencies {
    add("detektPlugins", libs.library("detektFormatting"))
    add("detektPlugins", libs.library("twitterComposeRule"))
}
