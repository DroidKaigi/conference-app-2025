package io.github.droidkaigi.confsched.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration

@Composable
actual fun rememberSpacialEnvironment(): SpacialEnvironment {
    val spatialCapabilities = LocalSpatialCapabilities.current
    val config = LocalSpatialConfiguration.current
    val session = LocalSession.current
    return remember(spatialCapabilities, session) {
        object : SpacialEnvironment {
            override val enabledSpacialControl: Boolean
                get() = session != null

            override val isFullSpace: Boolean
                get() = spatialCapabilities.isSpatialUiEnabled

            override fun toggleSpaceMode() {
                if (isFullSpace) {
                    config.requestHomeSpaceMode()
                } else {
                    config.requestFullSpaceMode()
                }
            }
        }
    }
}
