package io.github.droidkaigi.confsched.data.eventmap.response

import io.github.droidkaigi.confsched.data.core.LocaledResponse
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectResponse(
    val i18nDesc: LocaledResponse,
    val id: String,
    val message: LocaledResponse?,
    val moreDetailsUrl: String?,
    val noShow: Boolean,
    val roomId: Int,
    val title: LocaledResponse,
)
