package io.github.droidkaigi.confsched.eventmap

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundaryDefaults
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

context(screenContext: EventMapScreenContext)
@Composable
fun EventMapScreenRoot(
    onClickReadMore: (url: String) -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.eventMapQueryKey),
        fallback = SoilDataBoundaryDefaults.appBarFallback(
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
