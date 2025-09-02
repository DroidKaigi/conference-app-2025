package io.github.droidkaigi.confsched.droidkaigiui.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.KaigiWindowSizeClassConstants
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableTimeSlot
import io.github.droidkaigi.confsched.droidkaigiui.extension.enableMouseDragScroll
import io.github.droidkaigi.confsched.droidkaigiui.extension.plus
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.fake
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.ui.tooling.preview.Preview

const val TimetableListTestTag = "TimetableList"

@Composable
fun TimetableList(
    timetableItemMap: PersistentMap<out TimeSlotItem, List<TimetableItem>>,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onBookmarkClick: (TimetableItemId) -> Unit,
    isBookmarked: (TimetableItemId) -> Boolean,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    highlightWord: String = "",
    isDateTagVisible: Boolean = false,
) {
    BoxWithConstraints(
        modifier = Modifier.testTag(TimetableListTestTag),
    ) {
        val isWideScreen = maxWidth >= KaigiWindowSizeClassConstants.WindowWidthSizeClassMediumMinWidth
        val columnCount = if (isWideScreen) 2 else 1

        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding + WindowInsets.navigationBars.asPaddingValues(),
            modifier = modifier.enableMouseDragScroll(lazyListState),
        ) {
            itemsIndexed(
                items = timetableItemMap.toList(),
                key = { _, (timeSlot, _) -> timeSlot.key },
            ) { index, (timeSlot, timetableItems) ->
                var timetableTimeSlotHeight by remember { mutableIntStateOf(0) }
                val timetableTimeSlotOffsetY by remember {
                    derivedStateOf {
                        val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.find { it.index == index }
                        // If the item is not visible, keep the TimetableTimeSlot in its original position.
                        if (itemInfo == null) return@derivedStateOf 0

                        val itemTopOffset = itemInfo.offset
                        // A positive offset means the top of the item is within the visible viewport.
                        if (itemTopOffset > 0) return@derivedStateOf 0

                        // Apply a vertical offset to TimetableTimeSlot to create a "sticky" effect while scrolling,
                        // but clamp it to ensure it doesn't overflow beyond the bottom edge of its item.
                        (-itemTopOffset).coerceAtMost(itemInfo.size - timetableTimeSlotHeight)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TimetableTimeSlot(
                        startTimeText = timeSlot.startTimeString,
                        endTimeText = timeSlot.endTimeString,
                        modifier = Modifier
                            .onSizeChanged { timetableTimeSlotHeight = it.height }
                            .graphicsLayer { translationY = timetableTimeSlotOffsetY.toFloat() },
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        timetableItems.windowed(columnCount, columnCount, true).forEach { windowedItems ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.height(IntrinsicSize.Max),
                            ) {
                                windowedItems.forEach { item ->
                                    TimetableItemCard(
                                        timetableItem = item,
                                        isBookmarked = isBookmarked(item.id),
                                        isDateTagVisible = isDateTagVisible,
                                        highlightWord = highlightWord,
                                        onBookmarkClick = { onBookmarkClick(item.id) },
                                        onTimetableItemClick = { onTimetableItemClick(item.id) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                    )
                                }
                                if (windowedItems.size < columnCount) {
                                    repeat(columnCount - windowedItems.size) {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

interface TimeSlotItem {
    val startTimeString: String
    val endTimeString: String
    val key: String
}

@Preview
@Composable
private fun TimetableListPreview() {
    data class PreviewTimeSlot(
        override val startTimeString: String,
        override val endTimeString: String,
        override val key: String,
    ) : TimeSlotItem

    KaigiPreviewContainer {
        TimetableList(
            timetableItemMap = persistentMapOf(
                PreviewTimeSlot(
                    startTimeString = "11:20",
                    endTimeString = "12:00",
                    key = "11:20-12:00",
                ) to List(2) { TimetableItem.Session.fake() },
                PreviewTimeSlot(
                    startTimeString = "12:00",
                    endTimeString = "13:00",
                    key = "12:00-13:00",
                ) to List(2) { TimetableItem.Session.fake() },
            ),
            onTimetableItemClick = {},
            onBookmarkClick = {},
            isBookmarked = { false },
        )
    }
}
