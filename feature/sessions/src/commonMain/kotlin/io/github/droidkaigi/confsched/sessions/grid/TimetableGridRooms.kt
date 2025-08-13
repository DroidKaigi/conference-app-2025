package io.github.droidkaigi.confsched.sessions.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.RoomType
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.sessions.TimetableScrollState
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RoomItem(
    room: Room,
    modifier: Modifier = Modifier,
) {
    ProvideRoomTheme(room.roomTheme) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = room.name.currentLangTitle,
                style = MaterialTheme.typography.titleMedium,
                color = LocalRoomTheme.current.primaryColor,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimetableGridRooms(
    roomCount: () -> Int,
    scrollState: TimetableScrollState,
    modifier: Modifier = Modifier,
    content: TimetableGridRoomsScope.() -> Unit,
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val roomsScreen = rememberRoomsLayout(
        roomCount = roomCount(),
        density = density,
        scrollState = scrollState,
    )

    val timetableGridRoomsScope = TimetableGridRoomsScopeImpl().apply(content)
    val itemProvider = itemProvider({ timetableGridRoomsScope.itemList.size }) { index ->
        timetableGridRoomsScope.itemList[index]()
    }

    val lineColor = TimetableGridDefaults.lineColor
    val lineStrokeWidthPx = with(density) { TimetableGridDefaults.lineStrokeWidth.toPx() }

    LazyLayout(
        itemProvider = { itemProvider },
        modifier = modifier
            .height(height = RoomsDefaults.headerHeight)
            .clipToBounds()
            .drawBehind {
                drawLine(
                    lineColor,
                    Offset(0f, roomsScreen.height.toFloat()),
                    Offset(roomsScreen.width.toFloat(), roomsScreen.height.toFloat()),
                    lineStrokeWidthPx,
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        if (roomsScreen.enableHorizontalScroll(dragAmount.x)) {
                            if (change.positionChange() != Offset.Zero) change.consume()
                        }
                        coroutineScope.launch {
                            roomsScreen.scroll(
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
                            scrollState.flingXIfPossible()
                        }
                    },
                )
            },
    ) { constraints ->
        data class ItemData(val placeable: Placeable, val roomItemLayout: RoomItemLayout)
        roomsScreen.updateBounds(width = constraints.maxWidth, height = constraints.maxHeight)

        val items = roomsScreen.visibleItemLayouts.map { (index, roomItemLayout) ->
            ItemData(
                placeable = measure(
                    index = index,
                    constraints = Constraints.fixed(
                        width = roomItemLayout.width,
                        height = roomItemLayout.height,
                    ),
                )[0],
                roomItemLayout = roomItemLayout,
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            items.forEach { (placeable, roomItemLayout) ->
                placeable.place(
                    x = roomItemLayout.left + scrollState.scrollX.toInt(),
                    y = roomItemLayout.top,
                )
            }
        }
    }
}

@Composable
private fun rememberRoomsLayout(
    roomCount: Int,
    density: Density,
    scrollState: TimetableScrollState,
): RoomsLayout {
    var width = 0
    var height = 0
    val roomItemLayouts = List(roomCount) { index ->
        val itemLayout = RoomItemLayout(
            index = index,
            density = density,
        )

        // update width and height based on the item layout
        height = maxOf(height, itemLayout.height)
        width = maxOf(width, itemLayout.right)

        itemLayout
    }

    return remember {
        RoomsLayout(
            roomItemLayouts = roomItemLayouts,
            scrollState = scrollState,
        )
    }
}

private data class RoomsLayout(
    private val roomItemLayouts: List<RoomItemLayout>,
    private val scrollState: TimetableScrollState,
) {
    var width = 0
        private set
    var height = 0
        private set

    val visibleItemLayouts by derivedStateOf {
        val scrollX = scrollState.scrollX.toInt()
        roomItemLayouts
            .withIndex()
            .filter { (_, layout) -> layout.isVisible(width, scrollX) }
    }

    fun enableHorizontalScroll(dragX: Float): Boolean {
        val nextPossibleX = calculatePossibleScrollX(dragX)
        return (scrollState.maxX < nextPossibleX && nextPossibleX < 0f)
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
        val nextPossibleX = calculatePossibleScrollX(dragAmount.x)
        scrollState.scroll(
            scrollX = nextPossibleX,
            scrollY = scrollState.scrollY,
            timeMillis = timeMillis,
            position = position,
        )
    }

    private fun calculatePossibleScrollX(scrollX: Float): Float {
        val currentValue = scrollState.scrollX
        val nextValue = currentValue + scrollX
        val maxScroll = scrollState.maxX
        return maxOf(minOf(nextValue, 0f), maxScroll)
    }
}

private class RoomItemLayout(
    index: Int,
    density: Density,
) {
    val width = with(density) { TimetableGridDefaults.columnWidth.roundToPx() }
    val height = with(density) { RoomsDefaults.headerHeight.roundToPx() }
    val left = index * width
    val top = 0
    val right = left + width

    fun isVisible(
        screenWidth: Int,
        scrollX: Int,
    ): Boolean {
        val screenStart = -scrollX
        val screenEnd = -scrollX + screenWidth
        val xInside = left in screenStart..screenEnd || right in screenStart..screenEnd
        return xInside
    }
}

interface TimetableGridRoomsScope {
    fun <T> items(
        items: List<T>,
        content: @Composable (T) -> Unit,
    )
}

private class TimetableGridRoomsScopeImpl : TimetableGridRoomsScope {
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

object RoomsDefaults {
    val headerHeight = 48.dp
}

@Preview
@Composable
private fun TimetableGridRoomsPreview() {
    val rooms = listOf(
        Room(
            id = 1,
            name = MultiLangText(
                enTitle = "Room A",
                jaTitle = "ルームA",
            ),
            type = RoomType.RoomJ,
            sort = 1,
        ),
        Room(
            id = 2,
            name = MultiLangText(
                enTitle = "Room B",
                jaTitle = "ルームB",
            ),
            type = RoomType.RoomF,
            sort = 2,
        ),
        Room(
            id = 3,
            name = MultiLangText(
                enTitle = "Room C",
                jaTitle = "ルームC",
            ),
            type = RoomType.RoomH,
            sort = 3,
        )
    )
    KaigiPreviewContainer {
        TimetableGridRooms(
            roomCount = { rooms.size },
            scrollState = TimetableScrollState(),
            modifier = Modifier.height(200.dp),
        ) {
            items(rooms) { room ->
                RoomItem(
                    room = room,
                    modifier = Modifier.height(RoomsDefaults.headerHeight),
                )
            }
        }
    }
}
