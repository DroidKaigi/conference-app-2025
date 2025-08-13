package io.github.droidkaigi.confsched.eventmap

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.util.plus
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.droidkaigiui.extension.excludeTop
import io.github.droidkaigi.confsched.eventmap.component.EventMap
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    uiState: EventMapUiState,
    onSelectFloor: (FloorLevel) -> Unit,
    onClickReadMore: (url: String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
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
            onSelectFloor = onSelectFloor,
            onClickReadMore = onClickReadMore,
            contentPadding = innerPadding +
                    WindowInsets.safeDrawingWithBottomNavBar.excludeTop().asPaddingValues() +
                    PaddingValues(
                        top = 10.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}
