package io.github.confsched.profile.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.confsched.profile.ProfileUiState
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.profile.Profile
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val ChangeFlipCardDeltaThreshold = 20f
private const val IntroAnimationMaxRotation = 30f

private enum class FlipState { Initial, Animating, Default }

@Composable
fun FlippableProfileCard(
    uiState: ProfileUiState.Card,
    modifier: Modifier = Modifier,
) {
    var flipState by remember { mutableStateOf(FlipState.Initial) }
    var isFlipped by remember { mutableStateOf(false) }

    val flipRotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )

    val introAnimationRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (flipState == FlipState.Initial) {
            flipState = FlipState.Animating
            introAnimationRotation.animateTo(
                IntroAnimationMaxRotation,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            )
            delay(100)
            introAnimationRotation.animateTo(
                0f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            )
            flipState = FlipState.Default
        }
    }

    ProfileCardDropShadow(
        modifier = modifier,
    ) {
        ProfileCard(
            uiState = uiState,
            rotation = if (flipState == FlipState.Default) flipRotation else introAnimationRotation.value,
            isFlipped = isFlipped,
            onFlippedChange = { isFlipped = it },
            interactionsEnabled = flipState == FlipState.Default,
        )
    }
}

@Composable
private fun ProfileCard(
    uiState: ProfileUiState.Card,
    rotation: Float,
    isFlipped: Boolean,
    onFlippedChange: (Boolean) -> Unit,
    interactionsEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable(
                enabled = interactionsEnabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onFlippedChange(!isFlipped)
            }
            .draggable(
                enabled = interactionsEnabled,
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    if (isFlipped && delta > ChangeFlipCardDeltaThreshold) {
                        onFlippedChange(false)
                    }
                    if (!isFlipped && delta < -ChangeFlipCardDeltaThreshold) {
                        onFlippedChange(true)
                    }
                },
            )
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
    ) {
        if (rotation < 90f) {
            ProfileCardFront(
                theme = uiState.profile.theme,
                profileImageBitmap = uiState.profileImageBitmap,
                nickName = uiState.profile.nickName,
                occupation = uiState.profile.occupation,
            )
        } else {
            ProfileCardBack(
                theme = uiState.profile.theme,
                qrImageBitmap = uiState.qrImageBitmap,
                modifier = Modifier.graphicsLayer {
                    rotationY = 180f
                },
            )
        }
    }
}

@Preview
@Composable
private fun FlippableProfileCardPreview() {
    KaigiPreviewContainer {
        FlippableProfileCard(
            uiState = ProfileUiState.Card(
                profile = Profile(
                    nickName = "DroidKaigi",
                    occupation = "Software Engineer",
                ),
                profileImageBitmap = CardPreviewImageBitmaps.profileImage,
                qrImageBitmap = CardPreviewImageBitmaps.qrImage,
            ),
        )
    }
}
