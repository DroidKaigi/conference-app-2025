package io.github.confsched.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.github.confsched.profile.components.CardPreviewImageBitmaps
import io.github.confsched.profile.components.FlippableProfileCard
import io.github.confsched.profile.components.ShareableProfileCard
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.edit
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.droidkaigi.confsched.profile.share
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCardScreen(
    uiState: ProfileUiState.Card,
    onEditClick: () -> Unit,
    onShareClick: (ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    var shareableProfileCardRenderResult: ImageBitmap? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    // Not displayed, just for generating shareable card image
    ShareableProfileCard(
        theme = uiState.profile.theme,
        qrImageBitmap = uiState.qrImageBitmap,
        nickName = uiState.profile.nickName,
        occupation = uiState.profile.occupation,
        profileImageBitmap = uiState.profileImageBitmap,
        onRenderResultUpdate = { shareableProfileCardRenderResult = it },
    )

    Scaffold(
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(ProfileRes.string.profile_card_title),
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            FlippableProfileCard(
                uiState = uiState,
                modifier = Modifier.alpha(if (shareableProfileCardRenderResult != null) 1f else 0f),
            )
            Spacer(Modifier.height(32.dp))
            Button(
                enabled = shareableProfileCardRenderResult != null,
                onClick = {
                    shareableProfileCardRenderResult?.let {
                        coroutineScope.launch {
                            onShareClick(it)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share profile card",
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(stringResource(ProfileRes.string.share))
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth(),
                border = null,
                contentPadding = PaddingValues(18.dp),
            ) {
                Text(stringResource(ProfileRes.string.edit))
            }
        }
    }
}

@Preview
@Composable
private fun ProfileCardScreenPreview() {
    KaigiPreviewContainer {
        ProfileCardScreen(
            uiState = ProfileUiState.Card(
                profile = Profile(
                    nickName = "DroidKaigi",
                    occupation = "Software Engineer",
                ),
                qrImageBitmap = CardPreviewImageBitmaps.qrImage,
                profileImageBitmap = CardPreviewImageBitmaps.profileImage,
            ),
            onShareClick = {},
            onEditClick = {},
        )
    }
}
