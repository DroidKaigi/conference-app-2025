package io.github.confsched.profile.edit

import androidx.compose.runtime.Composable
import io.github.confsched.profile.ProfileScreenContext
import io.github.droidkaigi.confsched.common.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.model.profile.Profile

context(screenContext: ProfileScreenContext)
@Composable
fun ProfileEditScreenRoot(
    profile: Profile?,
    onProfileCreate: () -> Unit,
) {
    val eventFlow = rememberEventFlow<ProfileEditScreenEvent>()

    val uiState = profileEditScreenPresenter(
        eventFlow = eventFlow,
        profile = profile,
    )

    SafeLaunchedEffect(uiState.created) {
        if (uiState.created) {
            onProfileCreate()
        }
    }

    ProfileEditScreen(form = uiState.form)
}
