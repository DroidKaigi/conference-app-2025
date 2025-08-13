package io.github.confsched.profile.edit

import io.github.droidkaigi.confsched.model.profile.Profile
import soil.form.compose.Form

data class ProfileEditUiState(
    val form: Form<Profile>,
    val created: Boolean,
)
