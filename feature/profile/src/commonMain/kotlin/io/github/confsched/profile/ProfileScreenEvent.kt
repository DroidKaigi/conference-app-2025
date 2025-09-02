package io.github.confsched.profile

import io.github.droidkaigi.confsched.model.profile.Profile

sealed interface ProfileScreenEvent {
    data object EnterEditMode : ProfileScreenEvent
    data class CreateProfile(val profile: Profile) : ProfileScreenEvent
}
