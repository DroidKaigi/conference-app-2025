package io.github.confsched.profile

import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.model.profile.Profile

sealed interface ProfileScreenUiState {
    data class Card(
        val profile: Profile,
        val profileImageBitmap: ImageBitmap,
        val qrImageBitmap: ImageBitmap,
    ) : ProfileScreenUiState

    data class Edit(
        val baseProfile: Profile?,
        val canBackToCardScreen: Boolean,
    ) : ProfileScreenUiState
}
