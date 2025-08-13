package io.github.droidkaigi.confsched.data.core

import io.github.droidkaigi.confsched.model.core.MultiLangText
import kotlinx.serialization.Serializable

@Serializable
public data class LocaledResponse(
    val ja: String,
    val en: String,
)

internal fun LocaledResponse.toMultiLangText(): MultiLangText {
    return MultiLangText(jaTitle = ja, enTitle = en)
}
