package io.github.droidkaigi.confsched.repository

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.settings.SettingsDataStore
import io.github.droidkaigi.confsched.model.settings.Settings
import io.github.droidkaigi.confsched.model.settings.SettingsSubscriptionKey
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.rememberSubscription

@Inject
class SettingsRepository(
    private val settingsSubscriptionKey: SettingsSubscriptionKey,
    private val settingsDataStore: SettingsDataStore,
) {
    @OptIn(ExperimentalSoilQueryApi::class, FlowPreview::class)
    fun settingsFlow(): Flow<Settings> = moleculeFlow(RecompositionMode.Immediate) {
        soilDataBoundary(
            state = rememberSubscription(key = settingsSubscriptionKey),
        )
    }
        .filterNotNull()
        .distinctUntilChanged()
        // Errors thrown inside flow can't be caught on iOS side, so we catch it here.
        .catch { emit(Settings.Default) }

    @Throws(Throwable::class)
    suspend fun save(settings: Settings) {
        settingsDataStore.save(settings)
    }
}
