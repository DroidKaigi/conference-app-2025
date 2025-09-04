package io.github.droidkaigi.confsched.sessions.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.sessions.TimetableScaleState
import io.github.droidkaigi.confsched.sessions.TimetableScrollState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun HourItem(
    hour: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = hour,
        modifier = modifier,
        textAlign = TextAlign.Right,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimetableGridHours(
    hoursCount: () -> Int,
    scrollState: TimetableScrollState,
    scaleState: TimetableScaleState,
    timeLine: TimeLine?,
    selectedDay: DroidKaigi2025Day,
    modifier: Modifier = Modifier,
    content: TimetableGridHoursScope.() -> Unit,
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val verticalScale = scaleState.verticalScale

    val hoursScreen by remember(verticalScale, timeLine) {
        derivedStateOf {
            val hoursLayout = createHoursLayout(hoursCount(), density, verticalScale)
            HoursScreen(
                scrollState = scrollState,
                hoursLayout = hoursLayout,
                timeLine = timeLine,
                selectedDay = selectedDay,
            )
        }
    }

    val timetableGridHoursScope = TimetableGridHoursScopeImpl().apply(content)
    val itemProvider = itemProvider(
        itemCount = { timetableGridHoursScope.itemList.size },
    ) { index ->
        timetableGridHoursScope.itemList[index]()
    }

    val lineColor = MaterialTheme.colorScheme.surfaceVariant
    val linePxSize = with(density) { HoursDefaults.lineStrokeWidth.toPx() }
    val lineOffsetY = with(density) { HoursDefaults.lineTopOffset.roundToPx() }
    val lineOffsetX = with(density) { HoursDefaults.width.roundToPx() }
    val currentTimeIndicatorColor = MaterialTheme.colorScheme.primary
    val currentTimeIndicatorRadius = with(density) { TimetableGridDefaults.currentTimeDotRadius.toPx() }

    LazyLayout(
        itemProvider = { itemProvider },
        modifier = modifier
            .width(HoursDefaults.width)
            .clipToBounds()
            .drawBehind {
                drawLine(
                    color = lineColor,
                    start = Offset(lineOffsetX.toFloat(), lineOffsetY.toFloat()),
                    end = Offset(lineOffsetX.toFloat(), lineOffsetY.toFloat() + hoursScreen.height.toFloat()),
                    strokeWidth = linePxSize,
                )
            }
            .drawWithContent {
                drawContent()
                hoursScreen.timeLineOffsetY?.let { timeLineOffsetY ->
                    drawCircle(
                        color = currentTimeIndicatorColor,
                        radius = currentTimeIndicatorRadius,
                        center = Offset(lineOffsetX.toFloat(), lineOffsetY.toFloat() + timeLineOffsetY),
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        if (change.positionChange() != Offset.Zero) change.consume()
                        coroutineScope.launch {
                            hoursScreen.scroll(
                                dragAmount,
                                change.uptimeMillis,
                                change.position,
                            )
                        }
                    },
                    onDragCancel = {
                        scrollState.resetTracking()
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            scrollState.flingYIfPossible()
                        }
                    },
                )
            },
    ) { constraints ->
        data class ItemData(val placeable: Placeable, val hourItemLayout: HourItemLayout)
        hoursScreen.updateBounds(width = constraints.maxWidth, height = constraints.maxHeight)

        val items = hoursScreen.visibleItemLayouts.map { (index, hourLayout) ->
            ItemData(
                placeable = measure(
                    index,
                    Constraints.fixed(
                        width = hourLayout.width,
                        height = hourLayout.height,
                    ),
                )[0],
                hourItemLayout = hourLayout,
            )
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            items.forEach { (placeable, hoursLayout) ->
                placeable.place(
                    hoursLayout.left,
                    hoursLayout.top + hoursScreen.scrollState.scrollY.toInt(),
                )
            }
        }
    }
}

interface TimetableGridHoursScope {
    fun <T> items(
        items: List<T>,
        content: @Composable (T) -> Unit,
    )
}

private class TimetableGridHoursScopeImpl : TimetableGridHoursScope {
    private val _itemList = mutableListOf<@Composable () -> Unit>()
    val itemList: List<@Composable () -> Unit>
        get() = _itemList

    override fun <T> items(
        items: List<T>,
        content: @Composable (T) -> Unit,
    ) {
        _itemList.addAll(items.map { { content(it) } })
    }
}

private class HoursScreen(
    val scrollState: TimetableScrollState,
    hoursLayout: HoursLayout,
    timeLine: TimeLine?,
    selectedDay: DroidKaigi2025Day,
) {
    var width = 0
        private set
    var height = 0
        private set

    val visibleItemLayouts by derivedStateOf {
        hoursLayout.visibleItemLayouts(
            height,
            scrollState.scrollY.toInt(),
        )
    }

    val timeLineOffsetY by derivedStateOf {
        val durationFromStart = timeLine?.durationFromScheduleStart(selectedDay)
        durationFromStart?.let {
            scrollState.scrollY + it.inWholeMinutes * hoursLayout.minutePx
        }
    }

    fun updateBounds(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    suspend fun scroll(
        dragAmount: Offset,
        timeMillis: Long,
        position: Offset,
    ) {
        val nextPossibleY = calculatePossibleScrollY(dragAmount.y)
        scrollState.scroll(
            scrollX = scrollState.scrollX,
            scrollY = nextPossibleY,
            timeMillis = timeMillis,
            position = position,
        )
    }

    private fun calculatePossibleScrollY(scrollY: Float): Float {
        val currentValue = scrollState.scrollY
        val nextValue = currentValue + scrollY
        val maxScroll = scrollState.maxY
        return maxOf(minOf(nextValue, 0f), maxScroll)
    }
}

private data class HoursLayout(
    val height: Int,
    val width: Int,
    val minutePx: Float,
    val hourItemLayouts: List<HourItemLayout>,
) {
    fun visibleItemLayouts(
        screenHeight: Int,
        scrollY: Int,
    ): List<IndexedValue<HourItemLayout>> {
        return hourItemLayouts.withIndex().filter { (_, layout) ->
            layout.isVisible(screenHeight, scrollY)
        }
    }
}

private fun createHoursLayout(
    hoursCount: Int,
    density: Density,
    verticalScale: Float,
): HoursLayout {
    val minutePx = with(density) { TimetableGridDefaults.minuteHeight.times(verticalScale).toPx() }
    var hoursHeight = 0
    var hoursWidth = 0
    val hourItemLayouts = List(hoursCount) { index ->
        HourItemLayout(
            density = density,
            minutePx = minutePx,
            index = index,
        ).apply {
            hoursHeight = maxOf(hoursHeight, this.height)
            hoursWidth = maxOf(hoursWidth, this.width)
        }
    }

    return HoursLayout(
        height = hoursHeight,
        width = hoursWidth,
        minutePx = minutePx,
        hourItemLayouts = hourItemLayouts,
    )
}

private class HourItemLayout(
    density: Density,
    minutePx: Float,
    index: Int,
) {
    val topOffset = with(density) { HoursDefaults.lineTopOffset.roundToPx() }
    val itemOffset = with(density) { HoursDefaults.itemTopOffset.roundToPx() }
    val height = (minutePx * 60).roundToInt()
    val width = with(density) { HoursDefaults.width.roundToPx() }
    val left = 0
    val top = index * height + topOffset - itemOffset
    val bottom = top + height

    fun isVisible(
        screenHeight: Int,
        scrollY: Int,
    ): Boolean {
        val screenTop = -scrollY
        val screenBottom = -scrollY + screenHeight
        val yInside =
            top in screenTop..screenBottom || bottom in screenTop..screenBottom
        return yInside
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun itemProvider(
    itemCount: () -> Int,
    itemContent: @Composable (Int) -> Unit,
): LazyLayoutItemProvider {
    return object : LazyLayoutItemProvider {
        override val itemCount: Int get() = itemCount()

        @Composable
        override fun Item(index: Int, key: Any) {
            itemContent(index)
        }
    }
}

object HoursDefaults {
    val lineStrokeWidth = 1.dp
    val lineTopOffset = 48.dp
    val width = 68.dp
    val itemTopOffset = 11.dp
}

val hoursList by lazy {
    val conferenceStartHour = 10
    val conferenceEndHour = 19
    val tz = TimeZone.of("UTC+9")
    val now = Clock.System.now().toLocalDateTime(tz)
    (conferenceStartHour..conferenceEndHour).map {
        val dateTime = LocalDateTime(
            date = now.date,
            time = LocalTime(hour = it, minute = 0),
        )
            .toInstant(tz)
        val localDate = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDate.hour}".padStart(2, '0') + ":" + "${localDate.minute}".padStart(2, '0')
    }
}
