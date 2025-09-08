package io.github.confsched.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.droidkaigi.confsched.profile.share_description
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
context(screenContext: ProfileScreenContext)
fun ProfileScreenRoot(
    onShareClick: (String, ImageBitmap) -> Unit,
    onNavigateBack: () -> Unit,
) {
    SoilDataBoundary(
        state = rememberSubscription(screenContext.profileSubscriptionKey),
        fallback = SoilFallbackDefaults.appBar(stringResource(ProfileRes.string.profile_card_title)),
    ) { profileWithImageBitmaps ->
        val eventFlow = rememberEventFlow<ProfileScreenEvent>()

        val uiState = profilePresenter(
            eventFlow = eventFlow,
            profileWithImages = profileWithImageBitmaps,
        )

        when (uiState) {
            is ProfileUiState.Card -> {
                val shareText = stringResource(ProfileRes.string.share_description)

                ProfileCardScreen(
                    uiState = uiState,
                    onEditClick = { eventFlow.tryEmit(ProfileScreenEvent.EnterEditMode) },
                    onShareClick = { shareableImageBitmap ->
                        onShareClick(shareText, shareableImageBitmap)
                    },
                )
            }

            is ProfileUiState.Edit -> {
                BackHandler(enabled = true) {
                    if (uiState.baseProfile != null) {
                        eventFlow.tryEmit(ProfileScreenEvent.ExitEditMode)
                    } else {
                        onNavigateBack()
                    }
                }

                ProfileEditScreen(
                    initialProfile = uiState.baseProfile,
                    onCreateClick = { eventFlow.tryEmit(ProfileScreenEvent.CreateProfile(it)) },
                )
            }
        }
    }
}
