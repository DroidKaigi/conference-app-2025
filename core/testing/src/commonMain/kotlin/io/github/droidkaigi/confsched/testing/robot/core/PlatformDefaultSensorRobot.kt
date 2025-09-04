package io.github.droidkaigi.confsched.testing.robot.core

import dev.zacsweers.metro.Inject

interface SensorRobot {
    fun setupMockSensors(sensorTypes: List<SensorType>)
    fun cleanUpSensors()
    fun tiltPitch(pitch: Float = 10f)
    fun tiltRoll(roll: Float = 10f)
    fun tiltAzimuth(azimuth: Float = 10f)
    fun tiltAllAxes(pitch: Float = 10f, roll: Float = 10f, azimuth: Float = 10f)
}

/**
 * - This sensor robot only supports Euler angles.
 * - TODO If necessary, add processing to handle quaternions.
 */
@Inject
expect class PlatformDefaultSensorRobot : SensorRobot {
    override fun setupMockSensors(sensorTypes: List<SensorType>)
    override fun cleanUpSensors()
    override fun tiltPitch(pitch: Float)
    override fun tiltRoll(roll: Float)
    override fun tiltAzimuth(azimuth: Float)
    override fun tiltAllAxes(pitch: Float, roll: Float, azimuth: Float)
}
