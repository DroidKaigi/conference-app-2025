package io.github.droidkaigi.confsched.sessions.grid

import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

data class TimeLine(
    private val currentTime: Instant,
    private val currentDay: DroidKaigi2025Day,
) {
    fun durationFromScheduleStart(targetDay: DroidKaigi2025Day): Duration? {
        if (currentDay != targetDay) return null
        val currentTimeSecondOfDay = currentTime.toLocalDateTime(TimeZone.Companion.currentSystemDefault()).time.toSecondOfDay()
        val scheduleStartTimeSecondOfDay = LocalTime(hour = 10, minute = 0).toSecondOfDay()
        return ((currentTimeSecondOfDay - scheduleStartTimeSecondOfDay) / 60).minutes
    }

    companion object {
        fun now(clock: Clock): TimeLine? {
            val currentTime = clock.now()
            val currentDay = DroidKaigi2025Day.Companion.ofOrNull(currentTime)
            return currentDay?.let {
                TimeLine(
                    currentTime = currentTime,
                    currentDay = currentDay,
                )
            }
        }
    }
}
