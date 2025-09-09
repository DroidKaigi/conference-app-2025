package io.github.confsched.profile

import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.model.profile.Profile

sealed interface ProfileUiState {
    data class Card(
        val profile: Profile,
        val profileImageBitmap: ImageBitmap,
        val qrImageBitmap: ImageBitmap,
    ) : ProfileUiState

    data class Edit(
        val baseProfile: Profile?,
        val canBackToCardScreen: Boolean,
    ) : ProfileUiState
}
