package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.droidkaigi.confsched.common.compose.rememberSpacialEnvironment
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.model.sessions.TimetableUiType
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.grid_view
import io.github.droidkaigi.confsched.sessions.ic_request_full_space
import io.github.droidkaigi.confsched.sessions.ic_request_home_space
import io.github.droidkaigi.confsched.sessions.ic_view_grid
import io.github.droidkaigi.confsched.sessions.ic_view_timeline
import io.github.droidkaigi.confsched.sessions.request_full_space
import io.github.droidkaigi.confsched.sessions.request_home_space
import io.github.droidkaigi.confsched.sessions.search
import io.github.droidkaigi.confsched.sessions.timeline_view
import io.github.droidkaigi.confsched.sessions.timetable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimetableTopAppBar(
    timetableUiType: TimetableUiType,
    onSearchClick: () -> Unit,
    onUiTypeChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFullSpace = rememberSpacialEnvironment().isFullSpace
    AnimatedTextTopAppBar(
        title = stringResource(SessionsRes.string.timetable),
        actions = {
            val spacialEnvironment = rememberSpacialEnvironment()
            if (spacialEnvironment.enabledSpacialControl) {
                IconButton(
                    onClick = {
                        spacialEnvironment.toggleSpaceMode()
                    },
                    shapes = IconButtonDefaults.shapes(),
                ) {
                    if (spacialEnvironment.isFullSpace) {
                        Icon(
                            painter = painterResource(SessionsRes.drawable.ic_request_home_space),
                            contentDescription = stringResource(SessionsRes.string.request_full_space),
                        )
                    } else {
                        Icon(
                            painter = painterResource(SessionsRes.drawable.ic_request_full_space),
                            contentDescription = stringResource(SessionsRes.string.request_home_space),
                        )
                    }
                }
            }
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(SessionsRes.string.search),
                )
            }
            IconButton(onClick = onUiTypeChangeClick) {
                val iconRes = when (timetableUiType) {
                    TimetableUiType.List -> SessionsRes.drawable.ic_view_grid
                    TimetableUiType.Grid -> SessionsRes.drawable.ic_view_timeline
                }
                val descriptionRes = when (timetableUiType) {
                    TimetableUiType.List -> SessionsRes.string.grid_view
                    TimetableUiType.Grid -> SessionsRes.string.timeline_view
                }
                Icon(
                    imageVector = vectorResource(iconRes),
                    contentDescription = stringResource(descriptionRes),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isFullSpace) Color.Unspecified else Color.Transparent,
        ),
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
