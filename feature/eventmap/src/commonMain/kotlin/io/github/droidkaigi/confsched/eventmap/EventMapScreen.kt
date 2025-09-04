package io.github.droidkaigi.confsched.eventmap

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.util.plus
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.droidkaigiui.extension.excludeTop
import io.github.droidkaigi.confsched.eventmap.component.EventMap
import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import io.github.droidkaigi.confsched.model.eventmap.fakes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val EventMapScreenTestTag = "EventMapScreenTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    uiState: EventMapUiState,
    onSelectFloor: (FloorLevel) -> Unit,
    onClickReadMore: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier.testTag(EventMapScreenTestTag),
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(EventmapRes.string.event_map),
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets(),
    ) { innerPadding ->
        EventMap(
            uiState = uiState,
            listState = listState,
            onSelectFloor = onSelectFloor,
            onClickReadMore = onClickReadMore,
            contentPadding = innerPadding +
                WindowInsets.safeDrawingWithBottomNavBar.excludeTop().asPaddingValues() +
                PaddingValues(
                    top = 10.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        )
    }
}

@Preview(heightDp = 1920)
@Composable
private fun EventMapScreenPreview() {
    KaigiPreviewContainer {
        EventMapScreen(
            uiState = EventMapUiState(
                events = EventMapEvent.Companion.fakes(),
                selectedFloor = FloorLevel.Ground,
            ),
            onSelectFloor = {},
            onClickReadMore = {},
            modifier = Modifier,
        )
    }
}
