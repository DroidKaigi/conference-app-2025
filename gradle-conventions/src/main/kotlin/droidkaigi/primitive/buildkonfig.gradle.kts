package droidkaigi.primitive

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import util.libs
import util.version

plugins {
    id("com.codingfeline.buildkonfig")
}

buildkonfig {
    packageName = "io.github.droidkaigi"

    defaultConfigs {
        buildConfigField(STRING, "versionName", libs.version("droidkaigiApp"))
    }
}
