package io.github.droidkaigi.confsched.settings

import io.github.droidkaigi.confsched.model.settings.KaigiFontFamily

sealed interface SettingsScreenEvent {
    data class SelectUseFontFamily(val kaigiFontFamily: KaigiFontFamily) : SettingsScreenEvent
}
