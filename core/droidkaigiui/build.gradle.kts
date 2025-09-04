plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.compose.resources")
    id("droidkaigi.primitive.spotless")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.designsystem)
            implementation(projects.core.model)
            implementation(libs.soilQueryCompose)
            implementation(libs.soilReacty)
            implementation(libs.material3Adaptive)

            api(libs.coil)
            api(libs.coilNetwork)
        }
    }
}
