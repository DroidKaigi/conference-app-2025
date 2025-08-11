package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.model.sessions.TimetableUiType
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.grid_view
import io.github.droidkaigi.confsched.sessions.ic_view_grid
import io.github.droidkaigi.confsched.sessions.ic_view_timeline
import io.github.droidkaigi.confsched.sessions.search
import io.github.droidkaigi.confsched.sessions.timeline_view
import io.github.droidkaigi.confsched.sessions.timetable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableTopAppBar(
    timetableUiType: TimetableUiType,
    onSearchClick: () -> Unit,
    onUiTypeChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedTextTopAppBar(
        title = stringResource(SessionsRes.string.timetable),
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(SessionsRes.string.search),
                )
            }
            IconButton(onClick = onUiTypeChangeClick) {
                val iconRes = when (timetableUiType) {
                    TimetableUiType.List -> SessionsRes.drawable.ic_view_timeline
                    TimetableUiType.Grid -> SessionsRes.drawable.ic_view_grid
                }
                val descriptionRes = when (timetableUiType) {
                    TimetableUiType.List -> SessionsRes.string.timeline_view
                    TimetableUiType.Grid -> SessionsRes.string.grid_view
                }
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = stringResource(descriptionRes),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun TimetableTopAppBarPreview_List() {
    KaigiPreviewContainer {
        TimetableTopAppBar(
            timetableUiType = TimetableUiType.List,
            onSearchClick = {},
            onUiTypeChangeClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableTopAppBarPreview_Grid() {
    KaigiPreviewContainer {
        TimetableTopAppBar(
            timetableUiType = TimetableUiType.Grid,
            onSearchClick = {},
            onUiTypeChangeClick = {},
        )
    }
}
