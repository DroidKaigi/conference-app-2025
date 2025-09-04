import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.aboutlibraries")
    id("droidkaigi.primitive.spotless")
    id("droidkaigi.primitive.firebase")
    id("droidkaigi.primitive.firebase.crashlytics")
}

val keystorePropertiesFile = file("keystore.properties")
val keystoreExists = keystorePropertiesFile.exists()

android {
    namespace = "io.github.droidkaigi.confsched"
    compileSdk = 36

    flavorDimensions += "network"

    defaultConfig {
        applicationId = "io.github.droidkaigi.confsched2025"
        versionCode = 2
        minSdk = 24
        targetSdk = 36
    }

    signingConfigs {
        create("dev") {
            storeFile = project.file("dev.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        if (keystoreExists) {
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
            create("prod") {
                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
                storeFile = keystoreProperties["storeFile"]?.let { file(it) }
                storePassword = keystoreProperties["storePassword"] as String?
            }
        }
    }

    buildFeatures.buildConfig = true

    productFlavors {
        create("dev") {
            signingConfig = signingConfigs.getByName("dev")
            isDefault = true
            applicationIdSuffix = ".dev"
            dimension = "network"
            buildConfigField(
                type = "Boolean",
                name = "USE_PRODUCTION_API",
                value = "false",
            )
        }

        create("prod") {
            dimension = "network"
            signingConfig = if (keystoreExists) {
                signingConfigs.getByName("prod")
            } else {
                signingConfigs.getByName("dev")
            }
            buildConfigField(
                type = "Boolean",
                name = "USE_PRODUCTION_API",
                value = "true",
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            signingConfig = null
        }
    }

    packaging {
        resources {
            excludes += "META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(projects.appShared)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.droidkaigiui)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)

    implementation(projects.feature.sessions)
    implementation(projects.feature.about)
    implementation(projects.feature.sponsors)
    implementation(projects.feature.settings)
    implementation(projects.feature.staff)
    implementation(projects.feature.contributors)

    implementation(compose.runtime)
    implementation(compose.components.uiToolingPreview)
    implementation(compose.materialIconsExtended)
    implementation(libs.material3)
    debugImplementation(compose.uiTooling)

    implementation(libs.androidxActivityCompose)

    implementation(libs.navigation3Ui)
    implementation(libs.navigation3Runtime)
    implementation(libs.navigation3Adaptive)

    implementation(libs.kotlinxSerializationJson)
    implementation(libs.rin)

    implementation(libs.soilQueryCompose)
    // need this for compile success
    implementation(libs.androidxDatastorePreferencesCore)
}
