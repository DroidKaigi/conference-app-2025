package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable

@Composable
actual fun WithDeviceOrientation(
    content: @Composable (DeviceOrientationScope.() -> Unit),
) {
    // NOOP
    content(
        object : DeviceOrientationScope {
            override val orientation: Orientation
                get() = Orientation.Zero
        },
    )
}
