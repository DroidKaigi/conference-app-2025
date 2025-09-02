package io.github.droidkaigi.confsched.settings

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesGraphExtension
import io.github.droidkaigi.confsched.common.scope.SettingsScope
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.model.settings.SettingsMutationKey
import io.github.droidkaigi.confsched.model.settings.SettingsSubscriptionKey

@ContributesGraphExtension(SettingsScope::class)
interface SettingsScreenContext : ScreenContext {
    val settingsSubscriptionKey: SettingsSubscriptionKey
    val settingsMutationKey: SettingsMutationKey

    @ContributesGraphExtension.Factory(AppScope::class)
    fun interface Factory {
        fun createSettingsScreenContext(): SettingsScreenContext
    }
}
