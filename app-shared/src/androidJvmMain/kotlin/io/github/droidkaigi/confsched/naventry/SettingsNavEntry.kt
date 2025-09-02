package io.github.droidkaigi.confsched.naventry

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.navkey.SettingsNavKey
import io.github.droidkaigi.confsched.settings.SettingsScreenRoot
import io.github.droidkaigi.confsched.settings.rememberSettingsScreenContextRetained

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.settingsEntry(
    onBackClick: () -> Unit,
) {
    entry<SettingsNavKey> {
        with(rememberSettingsScreenContextRetained()) {
            SettingsScreenRoot(
                onBackClick = onBackClick,
            )
        }
    }
}
