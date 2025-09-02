plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.spotless")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    explicitApiWarning()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(libs.soilQueryCore)
            implementation(libs.ktorfitLib)
            implementation(libs.androidxDatastorePreferencesCore)
            implementation(libs.kotlinxSerializationJson)
            implementation(libs.kotlinxCollectionsImmutable)
            implementation(libs.qrcodeKotlin)
            implementation(libs.filekitCoil)

            implementation(libs.ktorClientContentNegotiation)
            implementation(libs.ktorKotlinxSerializationJson)
        }

        androidMain.dependencies {
            implementation(libs.ktorClientOkhttp)
        }

        jvmMain.dependencies {
            implementation(libs.ktorClientOkhttp)
        }
    }
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=soil.query.annotation.ExperimentalSoilQueryApi",
        )
    }
}
