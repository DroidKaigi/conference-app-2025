package io.github.droidkaigi.confsched.eventmap

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import soil.query.compose.rememberQuery

context(screenContext: EventMapScreenContext)
@Composable
fun EventMapScreenRoot(
    onClickReadMore: (url: String) -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.eventMapQueryKey),
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
