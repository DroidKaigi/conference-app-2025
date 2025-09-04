@file:OptIn(ExperimentalTime::class)

package io.github.droidkaigi.confsched.sessions.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.scrollBy
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.verticalScrollAxisRange
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.ScrolledToCurrentTimeState
import io.github.droidkaigi.confsched.sessions.TimetableState
import io.github.droidkaigi.confsched.sessions.components.TimetableGridItem
import io.github.droidkaigi.confsched.sessions.rememberTimetableState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@Composable
fun TimetableGrid(
    timetable: Timetable,
    timeLine: TimeLine?,
    selectedDay: DroidKaigi2025Day,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    scrolledToCurrentTimeState: ScrolledToCurrentTimeState = remember { ScrolledToCurrentTimeState() },
) {
    val timetableState = rememberTimetableState()

    Row {
        TimetableGridHours(
            hoursCount = { hoursList.size },
            scrollState = timetableState.scrollState,
            scaleState = timetableState.scaleState,
            timeLine = timeLine,
            selectedDay = selectedDay,
        ) {
            items(hoursList) { hour ->
                HourItem(
                    hour = hour,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
        Column {
            TimetableGridRooms(
                roomCount = { timetable.rooms.size },
                scrollState = timetableState.scrollState,
            ) {
                items(timetable.rooms) { room ->
                    RoomItem(
                        room = room,
                        modifier = Modifier.height(RoomsDefaults.headerHeight),
                    )
                }
            }
            TimetableGrid(
                timetable = timetable,
                timeLine = timeLine,
                selectedDay = selectedDay,
                contentPadding = contentPadding,
                scrolledToCurrentTimeState = scrolledToCurrentTimeState,
                timetableState = timetableState,
                modifier = modifier,
            ) {
                items(timetable.timetableItems) { timetableItem ->
                    TimetableGridItem(
                        timetableItem = timetableItem,
                        onTimetableItemClick = { onTimetableItemClick(it.id) },
                        scaleState = timetableState.scaleState,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimetableGrid(
    timetable: Timetable,
    timeLine: TimeLine?,
    selectedDay: DroidKaigi2025Day,
    contentPadding: PaddingValues,
    scrolledToCurrentTimeState: ScrolledToCurrentTimeState,
    timetableState: TimetableState,
    modifier: Modifier = Modifier,
    content: TimetableGridScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val clock = LocalClock.current
    val verticalScale = timetableState.scaleState.verticalScale
    val timetableGridState = rememberTimetableGridState(
        timetable = timetable,
        timeLine = timeLine,
        selectedDay = selectedDay,
        scrollState = timetableState.scrollState,
        density = density,
        verticalScale = verticalScale,
    )

    val nestedScrollConnection = remember { object : NestedScrollConnection {} }
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    val lineColor = TimetableGridDefaults.lineColor
    val lineStrokeWidthPx = with(density) { TimetableGridDefaults.lineStrokeWidth.toPx() }
    val currentTimeLineColor = TimetableGridDefaults.currentTimeLineColor
    val currentTimeDotRadius = with(density) { TimetableGridDefaults.currentTimeDotRadius.toPx() }

    val timetableGridScope = TimetableGridScopeImpl().apply(content)
    val itemProvider = itemProvider({ timetableGridScope.itemList.size }) { index ->
        timetableGridScope.itemList[index]()
        val itemHeightPx = timetableGridState.timetableLayout.timetableLayouts[index].height
        timetableGridScope.itemHeightPx = itemHeightPx
    }

    LaunchedEffect(Unit) {
        if (scrolledToCurrentTimeState.inTimetableGrid.not()) {
            val progressingSession = timetable.timetableItems
                .insertDummyEndOfTheDayItem() // Insert dummy at a position after last session to allow scrolling
                .windowed(2, 1, true)
                .find { clock.now() in it.first().startsAt..it.last().startsAt }
                ?.firstOrNull()

            progressingSession?.let { session ->
                val timeZone = TimeZone.currentSystemDefault()
                val period = with(session.startsAt) {
                    toLocalDateTime(timeZone)
                        .date.atTime(10, 0)
                        .toInstant(timeZone)
                        .periodUntil(this, timeZone)
                }
                val minuteHeightPx =
                    with(density) { TimetableGridDefaults.minuteHeight.times(verticalScale).toPx() }
                val scrollOffsetY =
                    -with(period) { hours * minuteHeightPx * 60 + minutes * minuteHeightPx }
                timetableGridState.scroll(
                    Offset(0f, scrollOffsetY),
                    0,
                    Offset.Zero,
                    nestedScrollDispatcher,
                )
                scrolledToCurrentTimeState.scrolledInTimetableGrid()
            }
        }
    }

    LazyLayout(
        itemProvider = { itemProvider },
        modifier = modifier
            .focusGroup()
            .clipToBounds()
            .nestedScroll(nestedScrollConnection, nestedScrollDispatcher)
            .drawBehind {
                // Draw background grid lines
                timetableGridState.timeHorizontalLines.forEach {
                    drawLine(
                        color = lineColor,
                        start = Offset(0F, it),
                        end = Offset(timetableGridState.width.toFloat(), it),
                        strokeWidth = lineStrokeWidthPx,
                    )
                }
                timetableGridState.roomVerticalLines.forEach {
                    drawLine(
                        color = lineColor,
                        start = Offset(it, 0f),
                        end = Offset(it, timetableGridState.height.toFloat()),
                        strokeWidth = lineStrokeWidthPx,
                    )
                }
            }
            .drawWithContent {
                drawContent()
                // Draw current time line
                timetableGridState.timeLineOffsetY?.let {
                    drawLine(
                        color = currentTimeLineColor,
                        start = Offset(0f, it),
                        end = Offset(size.width, it),
                        strokeWidth = lineStrokeWidthPx,
                    )
                    drawCircle(
                        color = currentTimeLineColor,
                        radius = currentTimeDotRadius,
                        center = Offset(0f, it),
                    )
                }
            }
            .transformable(
                state = rememberTransformableState { zoomChange, panChange, _ ->
                    timetableState.scaleState.updateVerticalScale(
                        timetableState.scaleState.verticalScale * zoomChange,
                    )

                    coroutineScope.launch {
                        timetableGridState.scroll(
                            dragAmount = panChange,
                            timeMillis = 0,
                            position = Offset.Zero,
                            nestedScrollDispatcher = nestedScrollDispatcher,
                        )
                    }
                },
            )
            .onGloballyPositioned { coordinates ->
                timetableGridState.scrollState.componentPositionInRoot = coordinates.positionInRoot()
            }
            .pointerInput(timetableGridState) {
                detectDragGestures(
                    onDragStart = {
                        timetableGridState.scrollState.resetTracking()
                    },
                    onDrag = { change, dragAmount ->
                        if (timetableGridState.enableHorizontalScroll(dragAmount.x)) {
                            if (change.positionChange() != Offset.Zero) change.consume()
                        }
                        coroutineScope.launch {
                            timetableGridState.scroll(
                                dragAmount = dragAmount,
                                timeMillis = change.uptimeMillis,
                                position = change.position,
                                nestedScrollDispatcher = nestedScrollDispatcher,
                            )
                        }
                    },
                    onDragCancel = {
                        timetableGridState.scrollState.resetTracking()
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            timetableGridState.scrollState.flingIfPossible(nestedScrollDispatcher)
                        }
                    },
                )
            }
            .semantics {
                horizontalScrollAxisRange = ScrollAxisRange(
                    value = { -timetableGridState.scrollState.scrollX },
                    maxValue = { -timetableGridState.scrollState.maxX },
                )
                verticalScrollAxisRange = ScrollAxisRange(
                    value = { -timetableGridState.scrollState.scrollY },
                    maxValue = { -timetableGridState.scrollState.maxY },
                )
                scrollBy(
                    action = { x: Float, y: Float ->
                        coroutineScope.launch {
                            timetableGridState.scrollState.scroll(
                                scrollX = x,
                                scrollY = y,
                                timeMillis = 0,
                                position = Offset.Zero,
                            )
                        }
                        true
                    },
                )
            },
    ) { constraint ->
        data class ItemData(val placeable: Placeable, val timetableItem: TimetableItemLayout)
        if (timetableGridState.width != constraint.maxWidth ||
            timetableGridState.height != constraint.maxHeight
        ) {
            timetableGridState.updateBounds(
                width = constraint.maxWidth,
                height = constraint.maxHeight,
                bottomPadding = contentPadding.calculateBottomPadding(),
            )
            val originalContentHeight = timetableGridState.timetableLayout.timetableHeight *
                timetableState.scaleState.verticalScale
            val layoutHeight = constraint.maxHeight
            timetableState.scaleState.updateVerticalScaleLowerBound(
                layoutHeight.toFloat() / originalContentHeight,
            )
        }

        val items = timetableGridState.visibleItemLayouts.map { (index, timetableLayout) ->
            ItemData(
                placeable = measure(
                    index,
                    Constraints.fixed(
                        width = timetableLayout.width,
                        height = timetableLayout.height,
                    ),
                )[0],
                timetableItem = timetableLayout,
            )
        }
        layout(constraint.maxWidth, constraint.maxHeight) {
            items.forEach { (placeable, timetableLayout) ->
                placeable.place(
                    x = timetableLayout.left + timetableGridState.scrollState.scrollX.toInt(),
                    y = timetableLayout.top + timetableGridState.scrollState.scrollY.toInt(),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun itemProvider(
    itemCount: () -> Int,
    content: @Composable (Int) -> Unit,
): LazyLayoutItemProvider {
    return object : LazyLayoutItemProvider {
        override val itemCount: Int get() = itemCount()

        @Composable
        override fun Item(index: Int, key: Any) {
            content(index)
        }
    }
}

private interface TimetableGridScope {
    fun <T> items(
        items: List<T>,
        key: ((item: T) -> Any)? = null,
        content: @Composable (T) -> Unit,
    )

    val itemHeightPx: Int
}

private class TimetableGridScopeImpl : TimetableGridScope {
    val itemList = mutableListOf<@Composable () -> Unit>()
    override var itemHeightPx: Int = 0

    override fun <T> items(
        items: List<T>,
        key: ((item: T) -> Any)?,
        content: @Composable (T) -> Unit,
    ) {
        itemList.addAll(items.map { item -> { content(item) } })
    }
}

object TimetableGridDefaults {
    val columnWidth = 192.dp
    val lineStrokeWidth = 1.dp
    val minuteHeight = 4.dp
    val currentTimeDotRadius = 6.dp
    val lineColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val currentTimeLineColor: Color
        @Composable get() = MaterialTheme.colorScheme.primary
}

suspend fun PointerInputScope.detectDragGestures(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        var drag: PointerInputChange?
        val overSlop = Offset.Zero
        do {
            drag = awaitTouchSlopOrCancellation(down.id, onDrag)
        } while (drag != null && !drag.isConsumed)
        if (drag != null) {
            onDragStart.invoke(drag.position)
            onDrag(drag, overSlop)
            if (
                !drag(drag.id) {
                    onDrag(it, it.positionChange())
                    it.consume()
                }
            ) {
                onDragCancel()
            } else {
                onDragEnd()
            }
        }
    }
}

private fun PersistentList<TimetableItem>.insertDummyEndOfTheDayItem(): PersistentList<TimetableItem> {
    val endOfTheDayInstant = first()
        .startsAt
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
        .plus(1, DateTimeUnit.DAY)
        .atStartOfDayIn(TimeZone.currentSystemDefault())
    return plus(
        TimetableItem.Session.fake().copy(
            startsAt = endOfTheDayInstant,
            endsAt = endOfTheDayInstant,
        ),
    ).toPersistentList()
}

@Preview
@Composable
private fun TimetableGridPreview() {
    val timetable = remember { Timetable.fake() }
    CompositionLocalProvider(LocalClock provides FakeClock) {
        KaigiPreviewContainer(Modifier.fillMaxSize()) {
            TimetableGrid(
                timetable = timetable,
                timeLine = TimeLine.now(LocalClock.current),
                selectedDay = DroidKaigi2025Day.ConferenceDay1,
                onTimetableItemClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TimetableGridWithTimelinePreview() {
    val timetable = remember { Timetable.fake() }
    val currentTime = DroidKaigi2025Day.ConferenceDay1.start + 11.hours
    CompositionLocalProvider(LocalClock provides FakeClock(currentTime)) {
        KaigiPreviewContainer(Modifier.fillMaxSize()) {
            TimetableGrid(
                timetable = timetable,
                timeLine = TimeLine.now(LocalClock.current),
                selectedDay = DroidKaigi2025Day.ConferenceDay1,
                onTimetableItemClick = {},
            )
        }
    }
}
