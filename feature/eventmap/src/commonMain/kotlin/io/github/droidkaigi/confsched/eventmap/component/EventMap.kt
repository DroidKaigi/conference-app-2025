package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.eventmap.EventMapUiState
import io.github.droidkaigi.confsched.eventmap.EventmapRes
import io.github.droidkaigi.confsched.eventmap.event_map
import io.github.droidkaigi.confsched.eventmap.event_map_description
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventMap(
    uiState: EventMapUiState,
    onSelectFloor: (FloorLevel) -> Unit,
    onClickReadMore: (url: String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item {
            Text(
                text = stringResource(EventmapRes.string.event_map_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(10.dp))
            TabbedEventMap(
                selectedFloor = uiState.selectedFloor,
                onSelectFloor = onSelectFloor,
            )
            Spacer(Modifier.height(16.dp))
        }
        itemsIndexed(uiState.events) { index, event ->
            EventMapItem(
                eventMapEvent = event,
                onClickReadMore = onClickReadMore,
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 24.dp,
                )
            )
            if (index != uiState.events.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}
