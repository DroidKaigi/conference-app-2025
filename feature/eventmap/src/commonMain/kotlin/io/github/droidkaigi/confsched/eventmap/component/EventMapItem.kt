package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.RoomToolTip
import io.github.droidkaigi.confsched.droidkaigiui.extension.icon
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.eventmap.EventmapRes
import io.github.droidkaigi.confsched.eventmap.read_more
import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import io.github.droidkaigi.confsched.model.eventmap.fakes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EventMapItem(
    eventMapEvent: EventMapEvent,
    onClickReadMore: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProvideRoomTheme(eventMapEvent.room.roomTheme) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RoomToolTip(
                    icon = eventMapEvent.room.icon,
                    text = eventMapEvent.room.name.currentLangTitle,
                    color = LocalRoomTheme.current.primaryColor,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = eventMapEvent.name.currentLangTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primaryFixed,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = eventMapEvent.description.currentLangTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7F),
            )
            eventMapEvent.message?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it.currentLangTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            eventMapEvent.moreDetailsUrl?.let {
                Spacer(Modifier.height(height = 8.dp))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onClickReadMore(it) },
                ) {
                    Text(
                        text = stringResource(EventmapRes.string.read_more),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primaryFixed,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun EventMapItemPreview() {
    KaigiPreviewContainer {
        Surface {
            EventMapItem(
                eventMapEvent = EventMapEvent.fakes().first(),
                onClickReadMore = {},
            )
        }
    }
}
