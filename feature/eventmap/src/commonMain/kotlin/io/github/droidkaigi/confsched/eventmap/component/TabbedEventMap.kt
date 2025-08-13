package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedFilterChip
import io.github.droidkaigi.confsched.eventmap.EventmapRes
import io.github.droidkaigi.confsched.eventmap.event_map_1f
import io.github.droidkaigi.confsched.eventmap.event_map_b1f
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

private const val ChangeTabDeltaThreshold = 20f

@Composable
fun TabbedEventMap(
    selectedFloor: FloorLevel,
    onSelectFloor: (FloorLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState { delta ->
                when (selectedFloor) {
                    FloorLevel.Basement if delta > ChangeTabDeltaThreshold -> onSelectFloor(FloorLevel.Ground)
                    FloorLevel.Ground if delta < -ChangeTabDeltaThreshold -> onSelectFloor(FloorLevel.Basement)
                    else -> {
                        // Do nothing
                    }
                }
            },
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FloorLevel.entries.reversed().forEach { floorLevel ->
                AnimatedFilterChip(
                    isSelected = selectedFloor == floorLevel,
                    text = floorLevel.floorName,
                    onClick = { onSelectFloor(floorLevel) },
                )
            }
        }
        Crossfade(targetState = selectedFloor) { floor ->
            val mapRes = when (floor) {
                FloorLevel.Basement -> EventmapRes.drawable.event_map_b1f
                FloorLevel.Ground -> EventmapRes.drawable.event_map_1f
            }
            Image(
                painter = painterResource(mapRes),
                contentDescription = "Map of ${floor.floorName}",
            )
        }
    }
}

private class TabbedEventMapPreviewParameterProvider : PreviewParameterProvider<FloorLevel> {
    override val values: Sequence<FloorLevel> = sequenceOf(FloorLevel.Basement, FloorLevel.Ground)
}

@Preview
@Composable
private fun TabbedEventMapPreview(
    @PreviewParameter(TabbedEventMapPreviewParameterProvider::class) selectedFloor: FloorLevel,
) {
    TabbedEventMap(
        selectedFloor = selectedFloor,
        onSelectFloor = {},
    )
}
