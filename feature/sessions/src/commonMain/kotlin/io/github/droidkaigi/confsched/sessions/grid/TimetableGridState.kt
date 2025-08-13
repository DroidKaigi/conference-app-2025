package io.github.droidkaigi.confsched.sessions.grid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.sessions.TimetableScrollState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

private val timeZone = TimeZone.of("Asia/Tokyo")

@Composable
fun rememberTimetableGridState(
    timetable: Timetable,
    timeLine: TimeLine?,
    selectedDay: DroidKaigi2025Day,
    scrollState: TimetableScrollState,
    density: Density = LocalDensity.current,
) = remember(timetable, timeLine, selectedDay, scrollState, density) {
    TimetableGridState(
        timetableLayout = timetable.toLayout(density),
        timeLine = timeLine,
        selectedDay = selectedDay,
        scrollState = scrollState,
        density = density,
    )
}

class TimetableGridState(
    val timetableLayout: TimetableLayout,
    val timeLine: TimeLine?,
    val selectedDay: DroidKaigi2025Day,
    val scrollState: TimetableScrollState,
    private val density: Density,
) {
    var width = 0
        private set
    var height = 0
        private set

    val visibleItemLayouts by derivedStateOf {
        timetableLayout.visibleItemLayouts(
            width,
            height,
            scrollState.scrollX.toInt(),
            scrollState.scrollY.toInt(),
        )
    }
    val topOffset = with(density) { 0.dp.roundToPx() }
    val timeHorizontalLines by derivedStateOf {
        (0..10).map {
            scrollState.scrollY + timetableLayout.minutePx * 60 * it + topOffset
        }
    }
    val roomVerticalLines by derivedStateOf {
        val width = with(density) { TimetableGridDefaults.columnWidth.toPx() }
        val rooms = timetableLayout.rooms
        (0..rooms.lastIndex).map {
            scrollState.scrollX + width * it
        }
    }

    val timeLineOffsetY by derivedStateOf {
        timeLine
            ?.durationFromScheduleStart(selectedDay)
            ?.let {
                scrollState.scrollY + it.inWholeMinutes * timetableLayout.minutePx + topOffset
            }
    }

    override fun toString(): String {
        return "Screen(" +
                "width=$width, " +
                "height=$height, " +
                "scroll=$scrollState, " +
                "visibleItemLayouts=$visibleItemLayouts" +
                ")"
    }

    suspend fun scroll(
        dragAmount: Offset,
        timeMillis: Long,
        position: Offset,
        nestedScrollDispatcher: NestedScrollDispatcher,
    ) {
        // If the position does not change, VelocityTracker malfunctions. Therefore return here.
        if (dragAmount == Offset.Zero) return

        val parentConsumed = nestedScrollDispatcher.dispatchPreScroll(
            available = dragAmount,
            source = NestedScrollSource.UserInput,
        )
        val nextPossibleX = calculatePossibleScrollX(dragAmount.x - parentConsumed.x)
        val nextPossibleY = calculatePossibleScrollY(dragAmount.y - parentConsumed.y)
        val weConsumed = Offset(
            nextPossibleX - scrollState.scrollX,
            nextPossibleY - scrollState.scrollY,
        )
        scrollState.scroll(
            scrollX = nextPossibleX,
            scrollY = nextPossibleY,
            timeMillis = timeMillis,
            position = position,
        )
        nestedScrollDispatcher.dispatchPostScroll(
            consumed = parentConsumed + weConsumed,
            available = dragAmount - weConsumed - parentConsumed,
            source = NestedScrollSource.UserInput,
        )
    }

    fun enableHorizontalScroll(dragX: Float): Boolean {
        val nextPossibleX = calculatePossibleScrollX(dragX)
        return (scrollState.maxX < nextPossibleX && nextPossibleX < 0f)
    }

    fun enableVerticalScroll(dragY: Float): Boolean {
        val nextPossibleY = calculatePossibleScrollY(dragY)
        return (scrollState.maxY < nextPossibleY && nextPossibleY < 0f)
    }

    fun updateBounds(width: Int, height: Int, bottomPadding: Dp) {
        val bottomPaddingPx = with(density) {
            bottomPadding.toPx()
        }
        this.width = width
        this.height = height
        scrollState.updateBounds(
            maxX = if (width < timetableLayout.timetableWidth) {
                -(timetableLayout.timetableWidth - width).toFloat()
            } else {
                0f
            },
            maxY = if (height < timetableLayout.timetableHeight - bottomPaddingPx) {
                // Allow additional scrolling by bottomPadding (navigation bar height).
                -(timetableLayout.timetableHeight - height).toFloat() - bottomPaddingPx
            } else {
                0f
            },
        )
    }

    private fun calculatePossibleScrollX(scrollX: Float): Float {
        val currentValue = scrollState.scrollX
        val nextValue = currentValue + scrollX
        val maxScroll = scrollState.maxX
        return maxOf(minOf(nextValue, 0f), maxScroll)
    }

    private fun calculatePossibleScrollY(scrollY: Float): Float {
        val currentValue = scrollState.scrollY
        val nextValue = currentValue + scrollY
        val maxScroll = scrollState.maxY
        return maxOf(minOf(nextValue, 0f), maxScroll)
    }
}

data class TimetableLayout(
    val rooms: List<Room>,
    val dayStartTime: Instant?,
    val dayToStartTime: Map<DroidKaigi2025Day, Instant>,
    val timetableLayouts: List<TimetableItemLayout>,
    val minutePx: Float,
    val timetableHeight: Int,
    val timetableWidth: Int,
) {
    fun visibleItemLayouts(
        screenWidth: Int,
        screenHeight: Int,
        scrollX: Int,
        scrollY: Int,
    ) = timetableLayouts.withIndex().filter { (_, layout) ->
        layout.isVisible(screenWidth, screenHeight, scrollX, scrollY)
    }
}

private fun Timetable.toLayout(
    density: Density,
): TimetableLayout {
    val dayStartTime = timetableItems.minOfOrNull { it.startsAt }
    val dayToStartTime = run {
        val dayToStartTime = mutableMapOf<DroidKaigi2025Day, Instant>()
        timetableItems.forEach { timetableItem ->
            timetableItem.day?.let {
                dayToStartTime[it] = minOf(
                    dayToStartTime[it] ?: Instant.DISTANT_FUTURE,
                    timetableItem.startsAt,
                )
            }
        }
        dayToStartTime.mapValues { (_, startTime) ->
            val dayStartLocalTime = startTime.toLocalDateTime(timeZone)
            LocalDateTime(
                date = dayStartLocalTime.date,
                time = LocalTime(dayStartLocalTime.hour, 0),
            ).toInstant(timeZone)
        }
    }
    val minutePx = with(density) { TimetableGridDefaults.minuteHeight.toPx() }
    var timetableHeight = 0
    var timetableWidth = 0
    val timetableLayouts = timetableItems.map {
        val timetableItemLayout = it.toLayout(
            rooms = rooms,
            dayStartTime = dayStartTime!!,
            density = density,
            minutePx = minutePx,
            dayToStartTime = dayToStartTime,
        )
        timetableHeight =
            maxOf(timetableHeight, timetableItemLayout.bottom)
        timetableWidth =
            maxOf(timetableWidth, timetableItemLayout.right)
        timetableItemLayout
    }
    return TimetableLayout(
        rooms = rooms,
        dayStartTime = dayStartTime,
        dayToStartTime = dayToStartTime,
        timetableLayouts = timetableLayouts,
        minutePx = minutePx,
        timetableHeight = timetableHeight,
        timetableWidth = timetableWidth,
    )
}

data class TimetableItemLayout(
    val dayStart: Instant,
    val height: Int,
    val width: Int,
    val left: Int,
    val top: Int,
) {
    val right = left + width
    val bottom = top + height
    fun isVisible(
        screenWidth: Int,
        screenHeight: Int,
        scrollX: Int,
        scrollY: Int,
    ): Boolean {
        val screenLeft = -scrollX
        val screenRight = -scrollX + screenWidth
        val screenTop = -scrollY
        val screenBottom = -scrollY + screenHeight
        val xInside =
            left in screenLeft..screenRight || right in screenLeft..screenRight
        val yInside =
            top in screenTop..screenBottom || bottom in screenTop..screenBottom ||
                    (top <= screenTop && screenBottom <= bottom)
        return xInside && yInside
    }
}

private fun TimetableItem.toLayout(
    rooms: List<Room>,
    dayStartTime: Instant,
    density: Density,
    minutePx: Float,
    dayToStartTime: Map<DroidKaigi2025Day, Instant>,
): TimetableItemLayout {
    val dayStart = run {
        val startTime = dayToStartTime[day] ?: dayStartTime
        val localDate = startTime.toLocalDateTime(timeZone).date
        val dayStartLocalTime = LocalDateTime(
            date = localDate,
            time = LocalTime(10, 0),
        )
        dayStartLocalTime.toInstant(timeZone)
    }
    val displayEndsAt = endsAt.minus(1, DateTimeUnit.MINUTE)
    val height =
        ((displayEndsAt - startsAt).inWholeMinutes * minutePx).roundToInt()
    val width = with(density) { TimetableGridDefaults.columnWidth.roundToPx() }
    val left = rooms.indexOf(room) * width
    val top = ((startsAt - dayStart).inWholeMinutes * minutePx).toInt()
    return TimetableItemLayout(
        dayStart = dayStart,
        height = height,
        width = width,
        left = left,
        top = top,
    )
}
