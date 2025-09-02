package io.github.droidkaigi.confsched.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: SettingsScreenContext)
fun SettingsScreenRoot(
    onBackClick: () -> Unit,
) {
    SoilDataBoundary(
        state = rememberSubscription(screenContext.settingsSubscriptionKey),
        fallback = SoilFallbackDefaults.appBar(
            title = stringResource(SettingsRes.string.settings_title),
        ),
    ) { settings ->
        val eventFlow = rememberEventFlow<SettingsScreenEvent>()

        val uiState = settingsScreenPresenter(
            eventFlow = eventFlow,
            settings = settings,
        )

        SettingsScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onSelectUseFontFamily = { eventFlow.tryEmit(SettingsScreenEvent.SelectUseFontFamily(it)) },
        )
    }
}
