package io.github.droidkaigi.confsched.testing.robot.core

import org.robolectric.RuntimeEnvironment

actual fun setPlatformFontScale(fontScale: Float) {
    RuntimeEnvironment.setFontScale(fontScale)
}
