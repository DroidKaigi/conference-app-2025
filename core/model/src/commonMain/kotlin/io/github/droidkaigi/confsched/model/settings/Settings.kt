package io.github.droidkaigi.confsched.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val useKaigiFontFamily: KaigiFontFamily,
) {
    companion object {
        val Default = Settings(
            useKaigiFontFamily = KaigiFontFamily.SystemDefault,
        )
    }
}
