package io.github.droidkaigi.confsched.model.settings

import kotlinx.serialization.Serializable

@Serializable
enum class KaigiFontFamily(
    val displayName: String,
    val fileName: String? = null,
) {
    SystemDefault(
        displayName = "Default",
    ),
    ChangoRegular(
        displayName = "Chango Regular",
        fileName = "Chango_Regular",
    ),
    RobotoRegular(
        displayName = "Roboto Regular",
        fileName = "Roboto_Regular",
    ),
    RobotoMedium(
        displayName = "Roboto Medium",
        fileName = "Roboto_Medium",
    ),
}
