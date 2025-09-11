package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable

@Composable
internal actual fun getOrientationSensorManager(
    onOrientationChanged: (Orientation) -> Unit,
): OrientationSensorManager = object : OrientationSensorManager {
    override val onOrientationChanged: (Orientation) -> Unit
        get() = { Orientation.Zero }

    override fun start() {
        // NOOP
    }

    override fun stop() {
        // NOOP
    }
}
