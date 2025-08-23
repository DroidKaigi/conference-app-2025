package io.github.droidkaigi.confsched.eventmap

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: EventMapScreenContext)
fun EventMapScreenRoot(
    onClickReadMore: (url: String) -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.eventMapQueryKey),
        fallback = SoilFallbackDefaults.appBar(
            title = stringResource(EventmapRes.string.event_map),
        ),
    ) { events ->
        val eventFlow = rememberEventFlow<EventMapScreenEvent>()
        val uiState = eventMapScreenPresenter(events = events, eventFlow = eventFlow)
        EventMapScreen(
            uiState = uiState,
            onSelectFloor = { eventFlow.tryEmit(EventMapScreenEvent.SelectFloor(it)) },
            onClickReadMore = onClickReadMore,
        )
    }
}
