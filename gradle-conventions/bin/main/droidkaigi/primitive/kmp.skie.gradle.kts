package droidkaigi.primitive

import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuspendInterop
import co.touchlab.skie.plugin.configuration.SkieExtension

plugins {
    id("co.touchlab.skie")
}

configure<SkieExtension> {
    features {
        group {
            coroutinesInterop.set(true)
            SuspendInterop.Enabled(true)
            FlowInterop.Enabled(true)
            SealedInterop.Enabled(true)
        }
    }
}
