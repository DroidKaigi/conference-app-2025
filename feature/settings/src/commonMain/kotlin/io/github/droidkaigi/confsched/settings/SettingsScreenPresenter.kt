package io.github.droidkaigi.confsched.settings

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.settings.Settings
import soil.query.compose.rememberMutation

@Composable
context(screenContext: SettingsScreenContext)
fun settingsScreenPresenter(
    eventFlow: EventFlow<SettingsScreenEvent>,
    settings: Settings,
): SettingsScreenUiState = providePresenterDefaults {
    val settingsMutation = rememberMutation(screenContext.settingsMutationKey)

    EventEffect(eventFlow) { event ->
        when (event) {
            is SettingsScreenEvent.SelectUseFontFamily -> {
                settingsMutation.mutate(settings.copy(useKaigiFontFamily = event.kaigiFontFamily))
            }
        }
    }

    SettingsScreenUiState(
        useKaigiFontFamily = settings.useKaigiFontFamily,
    )
}
