package io.github.confsched.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.confsched.profile.components.CardPreviewImageBitmaps
import io.github.confsched.profile.components.FlippableProfileCard
import io.github.confsched.profile.components.ShareableProfileCard
import io.github.confsched.profile.components.isDark
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.edit
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.droidkaigi.confsched.profile.share
import io.github.droidkaigi.confsched.profile.shareable_card_background_day
import io.github.droidkaigi.confsched.profile.shareable_card_background_night
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
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
    val isShareReady = shareableProfileCardRenderResult != null
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isShareReady) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing,
        ),
    )

    // Not displayed, just for generating shareable card image
    ShareableProfileCard(
        theme = uiState.profile.theme,
        qrImageBitmap = uiState.qrImageBitmap,
        nickName = uiState.profile.nickName,
        occupation = uiState.profile.occupation,
        profileImageBitmap = uiState.profileImageBitmap,
        onRenderResultUpdate = { shareableProfileCardRenderResult = it },
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(
                if (uiState.profile.theme.isDark) {
                    ProfileRes.drawable.shareable_card_background_day
                } else {
                    ProfileRes.drawable.shareable_card_background_night
                },
            ),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            alpha = backgroundAlpha,
        )
        Scaffold(
            topBar = {
                AnimatedTextTopAppBar(
                    title = stringResource(ProfileRes.string.profile_card_title),
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    textColor = MaterialTheme.colorScheme.surface,
                    scrolledTextColor = MaterialTheme.colorScheme.onSurface,
                )
            },
            contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
            containerColor = Color.Transparent,
            modifier = modifier.fillMaxSize(),
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
                    modifier = Modifier.alpha(if (isShareReady) 1f else 0f),
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    enabled = isShareReady,
                    onClick = {
                        shareableProfileCardRenderResult?.let {
                            coroutineScope.launch { onShareClick(it) }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(18.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share profile card",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(ProfileRes.string.share),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth(),
                    border = null,
                    contentPadding = PaddingValues(18.dp),
                ) {
                    Text(
                        text = stringResource(ProfileRes.string.edit),
                        color = MaterialTheme.colorScheme.surface,
                    )
                }
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
