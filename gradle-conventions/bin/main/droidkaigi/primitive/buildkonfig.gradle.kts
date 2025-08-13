package droidkaigi.primitive

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("com.codingfeline.buildkonfig")
}

buildkonfig {
    packageName = "io.github.droidkaigi"

    defaultConfigs {
        buildConfigField(STRING, "versionName", "1.0.0")
    }
}
