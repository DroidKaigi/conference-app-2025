package io.github.droidkaigi.confsched.testing.robot.core

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.SensorEventBuilder
import org.robolectric.shadows.ShadowSensor
import org.robolectric.shadows.ShadowSensorManager

actual class PlatformDefaultSensorRobot : SensorRobot {
    private val sensorManager: SensorManager =
        getApplicationContext<Context>().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shadowSensorManager = shadowOf(sensorManager)

    private lateinit var mockAccelerometerSensor: Sensor
    private lateinit var mockMagneticFieldSensor: Sensor

    actual override fun setupMockSensors(sensorTypes: List<SensorType>) {
        sensorTypes.forEach { sensorType ->
            val sensor = ShadowSensor.newInstance(sensorType.id)
            shadowSensorManager.addSensor(sensor)
            when (sensorType) {
                SensorType.ACCELEROMETER -> mockAccelerometerSensor = sensor
                SensorType.MAGNETIC_FIELD -> mockMagneticFieldSensor = sensor
            }
        }
    }

    actual override fun cleanUpSensors() {
        CustomShadowSensorManager.setCustomRotationMatrix(floatArrayOf())
        CustomShadowSensorManager.setCustomOrientationAngles(floatArrayOf())
    }

    actual override fun tiltPitch(pitch: Float) {
        sendTiltEvent(mockAccelerometerSensor, pitch = pitch)
        sendTiltEvent(mockMagneticFieldSensor, pitch = pitch)
    }

    actual override fun tiltRoll(roll: Float) {
        sendTiltEvent(mockAccelerometerSensor, roll = roll)
        sendTiltEvent(mockMagneticFieldSensor, roll = roll)
    }

    actual override fun tiltAzimuth(azimuth: Float) {
        sendTiltEvent(mockAccelerometerSensor, azimuth = azimuth)
        sendTiltEvent(mockMagneticFieldSensor, azimuth = azimuth)
    }

    actual override fun tiltAllAxes(pitch: Float, roll: Float, azimuth: Float) {
        sendTiltEvent(mockAccelerometerSensor, pitch, roll, azimuth)
        sendTiltEvent(mockMagneticFieldSensor, pitch, roll, azimuth)
    }

    private fun sendTiltEvent(
        sensor: Sensor?,
        pitch: Float = 0f,
        roll: Float = 0f,
        azimuth: Float = 0f,
    ) {
        if (sensor != null) {
            val event = createTiltEvent(sensor, pitch, roll, azimuth)
            CustomShadowSensorManager.setCustomRotationMatrix(
                FloatArray(9).apply {
                    SensorManager.getRotationMatrix(
                        this,
                        null,
                        floatArrayOf(pitch, roll, azimuth),
                        floatArrayOf(0f, 0f, 0f),
                    )
                },
            )
            CustomShadowSensorManager.setCustomOrientationAngles(floatArrayOf(azimuth, pitch, roll))
            shadowSensorManager.sendSensorEventToListeners(event)
        }
    }

    private fun createTiltEvent(
        sensor: Sensor,
        pitch: Float,
        roll: Float,
        azimuth: Float,
    ): SensorEvent {
        return SensorEventBuilder.newBuilder()
            .setSensor(sensor)
            .setTimestamp(System.currentTimeMillis())
            .setValues(floatArrayOf(pitch, roll, azimuth))
            .build()
    }

    @Implements(SensorManager::class)
    class CustomShadowSensorManager : ShadowSensorManager() {

        @Suppress("UNUSED_PARAMETER")
        companion object {
            private var customRotationMatrix: FloatArray? = null
            private var customOrientationAngles: FloatArray? = null

            fun setCustomRotationMatrix(rotationMatrix: FloatArray) {
                customRotationMatrix = rotationMatrix
            }

            @Implementation
            @JvmStatic
            fun getRotationMatrix(
                r: FloatArray?,
                i: FloatArray?,
                gravity: FloatArray?,
                geomagnetic: FloatArray?,
            ): Boolean {
                customRotationMatrix?.let {
                    if (r != null && it.size == r.size) {
                        System.arraycopy(it, 0, r, 0, it.size)
                    }
                    return true
                }
                return false
            }

            fun setCustomOrientationAngles(orientationAngles: FloatArray) {
                customOrientationAngles = orientationAngles
            }

            @Implementation
            @JvmStatic
            fun getOrientation(r: FloatArray?, values: FloatArray?): FloatArray {
                customOrientationAngles?.let {
                    if (values != null && it.size == values.size) {
                        System.arraycopy(it, 0, values, 0, it.size)
                    }
                    return it
                }
                return r!!
            }
        }
    }
}
