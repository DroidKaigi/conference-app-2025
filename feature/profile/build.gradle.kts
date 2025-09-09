plugins {
    id("droidkaigi.convention.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.qrcodeKotlin)
            implementation(libs.soilForm)
            implementation(libs.filekitDialogsCompose)
            implementation(libs.uiBackHandler)
        }
    }
}
