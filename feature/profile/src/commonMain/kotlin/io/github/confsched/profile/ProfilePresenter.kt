package io.github.confsched.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.profile.ProfileWithImages
import io.github.takahirom.rin.rememberRetained
import soil.query.compose.rememberMutation

@Composable
context(screenContext: ProfileScreenContext)
fun profilePresenter(
    eventFlow: EventFlow<ProfileScreenEvent>,
    profileWithImages: ProfileWithImages,
): ProfileUiState = providePresenterDefaults {
    val isAllowedToShowCard = profileWithImages.profile != null &&
        profileWithImages.profileImageByteArray != null &&
        profileWithImages.qrImageByteArray != null

    val profileMutation = rememberMutation(screenContext.profileMutationKey)
    var isInEditMode by rememberRetained { mutableStateOf(!isAllowedToShowCard) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is ProfileScreenEvent.EnterEditMode -> {
                isInEditMode = true
            }

            is ProfileScreenEvent.CreateProfile -> {
                profileMutation.mutate(event.profile)
                isInEditMode = false
            }
        }
    }

    if (!isAllowedToShowCard || isInEditMode) {
        ProfileUiState.Edit(baseProfile = profileWithImages.profile)
    } else {
        ProfileUiState.Card(
            profile = requireNotNull(profileWithImages.profile),
            profileImageBitmap = requireNotNull(profileWithImages.profileImageByteArray).decodeToImageBitmap(),
            qrImageBitmap = requireNotNull(profileWithImages.qrImageByteArray).decodeToImageBitmap(),
        )
    }
}
