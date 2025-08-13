package io.github.confsched.profile.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.confsched.profile.ProfileScreenContext
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.takahirom.rin.rememberRetained
import soil.form.compose.FormState
import soil.form.compose.rememberForm
import soil.form.compose.rememberFormMetaState
import soil.query.compose.rememberMutation

context(screenContext: ProfileScreenContext)
@Composable
fun profileEditScreenPresenter(
    eventFlow: EventFlow<ProfileEditScreenEvent>,
    profile: Profile?,
): ProfileEditUiState = providePresenterDefaults {
    val profileMutation = rememberMutation(screenContext.profileMutationKey)
    val formMeta = rememberFormMetaState()
    val formState = remember {
        FormState(
            value = profile ?: Profile(
                name = "",
                occupation = "",
                link = "",
            ),
            meta = formMeta,
        )
    }
    val form = rememberForm(
        state = formState,
        onSubmit = { eventFlow.tryEmit(ProfileEditScreenEvent.Create(it)) }
    )
    var created by rememberRetained { mutableStateOf(false) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is ProfileEditScreenEvent.Create -> {
                profileMutation.mutate(event.profile)
                created = true
            }
        }
    }

    ProfileEditUiState(
        form = form,
        created = created,
    )
}
