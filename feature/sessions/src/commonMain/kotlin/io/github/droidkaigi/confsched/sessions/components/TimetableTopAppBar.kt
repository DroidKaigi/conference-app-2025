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
import io.github.droidkaigi.confsched.common.compose.rememberXrEnvironment
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
    val xrEnvironment = rememberXrEnvironment()
    AnimatedTextTopAppBar(
        title = stringResource(SessionsRes.string.timetable),
        actions = {
            if (xrEnvironment.enabledSpacialControl) {
                XrSpaceToggleIconButton(
                    onClick = xrEnvironment::toggleSpaceMode,
                    isXrFullSpace = xrEnvironment.isFullSpace,
                )
            }
            IconButton(
                onClick = onSearchClick,
                shapes = IconButtonDefaults.shapes(),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(SessionsRes.string.search),
                )
            }
            IconButton(
                onClick = onUiTypeChangeClick,
                shapes = IconButtonDefaults.shapes(),
            ) {
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
            containerColor = if (xrEnvironment.isFullSpace) Color.Unspecified else Color.Transparent,
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun XrSpaceToggleIconButton(
    onClick: () -> Unit,
    isXrFullSpace: Boolean,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        shapes = IconButtonDefaults.shapes(),
        modifier = modifier,
    ) {
        if (isXrFullSpace) {
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
