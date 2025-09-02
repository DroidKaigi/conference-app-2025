package io.github.droidkaigi.confsched.testing.robot.settings

import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.settings.SettingsDataStore
import io.github.droidkaigi.confsched.model.settings.KaigiFontFamily
import io.github.droidkaigi.confsched.model.settings.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreRobot {
    enum class SettingsStatus(
        val displayName: String,
    ) {
        UseChangoRegularFontFamily("Chango Regular"),
        UseRobotoRegularFontFamily("Roboto Regular"),
        UseRobotoMediumFontFamily("Roboto Medium"),
        UseSystemDefaultFont("System Default"),
    }

    suspend fun setupSettings(settingsStatus: SettingsStatus)
    fun getSettingsStream(): Flow<Settings>
}

@Inject
class DefaultSettingsDataStoreRobot(
    private val settingsDataStore: SettingsDataStore,
) : SettingsDataStoreRobot {
    override suspend fun setupSettings(
        settingsStatus: SettingsDataStoreRobot.SettingsStatus,
    ) {
        val useKaigiFontFamily = when (settingsStatus) {
            SettingsDataStoreRobot.SettingsStatus.UseChangoRegularFontFamily -> KaigiFontFamily.ChangoRegular
            SettingsDataStoreRobot.SettingsStatus.UseRobotoRegularFontFamily -> KaigiFontFamily.RobotoRegular
            SettingsDataStoreRobot.SettingsStatus.UseRobotoMediumFontFamily -> KaigiFontFamily.RobotoMedium
            SettingsDataStoreRobot.SettingsStatus.UseSystemDefaultFont -> KaigiFontFamily.SystemDefault
        }

        settingsDataStore.save(
            Settings(
                useKaigiFontFamily = useKaigiFontFamily,
            ),
        )
    }

    override fun getSettingsStream(): Flow<Settings> = settingsDataStore.getSettingsStream()
}
