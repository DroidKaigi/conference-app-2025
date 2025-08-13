package io.github.droidkaigi.confsched.data.sessions.response

import io.github.droidkaigi.confsched.data.core.LocaledResponse
import kotlinx.serialization.Serializable

@Serializable
public data class SessionResponse(
    val id: String,
    val isServiceSession: Boolean,
    val title: LocaledResponse,
    val speakers: List<String>,
    val description: String? = null,
    val i18nDesc: LocaledResponse? = null,
    val startsAt: String,
    val endsAt: String,
    val language: String,
    val roomId: Int,
    val sessionCategoryItemId: Int,
    val sessionType: String,
    val message: LocaledResponse?,
    val isPlenumSession: Boolean,
    val targetAudience: String,
    val interpretationTarget: Boolean,
    val asset: SessionAssetResponse,
    val levels: List<String>,
)
