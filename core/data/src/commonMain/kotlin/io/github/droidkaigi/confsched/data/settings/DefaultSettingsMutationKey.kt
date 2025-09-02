package io.github.droidkaigi.confsched.data.settings

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.settings.SettingsMutationKey
import soil.query.MutationId
import soil.query.buildMutationKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultSettingsMutationKey(
    private val settingsDataStore: SettingsDataStore,
) : SettingsMutationKey by buildMutationKey(
    id = MutationId("settings_mutation_key"),
    mutate = { settingsDataStore.save(settings = it) },
)
