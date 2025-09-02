package io.github.droidkaigi.confsched.testing.robot.core

// Since we have no plans to run tests using this robot on iOS, all processing has been set to NOOP.
actual class PlatformDefaultSensorRobot : SensorRobot {
    actual override fun setupMockSensors(sensorTypes: List<SensorType>) {
        // NOOP
    }

    actual override fun cleanUpSensors() {
        // NOOP
    }

    actual override fun tiltPitch(pitch: Float) {
        // NOOP
    }

    actual override fun tiltRoll(roll: Float) {
        // NOOP
    }

    actual override fun tiltAzimuth(azimuth: Float) {
        // NOOP
    }

    actual override fun tiltAllAxes(
        pitch: Float,
        roll: Float,
        azimuth: Float,
    ) {
        // NOOP
    }
}
