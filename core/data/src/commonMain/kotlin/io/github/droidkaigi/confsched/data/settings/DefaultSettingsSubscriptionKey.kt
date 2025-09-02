package io.github.droidkaigi.confsched.data.settings

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.settings.SettingsSubscriptionKey
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultSettingsSubscriptionKey(
    private val settingsDataStore: SettingsDataStore,
) : SettingsSubscriptionKey by buildSubscriptionKey(
    id = SubscriptionId("settings"),
    subscribe = { settingsDataStore.getSettingsStream() },
)
