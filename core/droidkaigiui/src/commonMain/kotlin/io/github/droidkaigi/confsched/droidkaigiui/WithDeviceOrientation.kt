package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal class DeviceOrientationScopeImpl : DeviceOrientationScope {
    override var orientation: Orientation by mutableStateOf(Orientation.Zero)
        private set

    fun updateOrientation(orientation: Orientation) {
        this.orientation = orientation
    }
}

interface DeviceOrientationScope {
    val orientation: Orientation
}

@Composable
expect fun WithDeviceOrientation(
    content: @Composable (DeviceOrientationScope.() -> Unit),
)
