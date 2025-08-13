package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItemDetailTopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier.background(LocalRoomTheme.current.dimColor),
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = LocalRoomTheme.current.dimColor,
            scrolledContainerColor = LocalRoomTheme.current.dimColor,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
    )
}

@Preview
@Composable
private fun TimetableItemDetailTopAppBarPreview() {
    KaigiPreviewContainer {
        ProvideRoomTheme(TimetableItem.Session.fake().room.roomTheme) {
            TimetableItemDetailTopAppBar(
                onBackClick = {},
            )
        }
    }
}
