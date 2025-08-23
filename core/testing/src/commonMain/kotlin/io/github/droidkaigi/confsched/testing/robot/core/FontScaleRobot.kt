package io.github.droidkaigi.confsched.testing.robot.core

import dev.zacsweers.metro.Inject

interface FontScaleRobot {
    fun setFontScale(fontScale: Float)
}

expect fun setPlatformFontScale(fontScale: Float)

@Inject
class DefaultFontScaleRobot : FontScaleRobot {
    override fun setFontScale(fontScale: Float) {
        setPlatformFontScale(fontScale)
    }
}
