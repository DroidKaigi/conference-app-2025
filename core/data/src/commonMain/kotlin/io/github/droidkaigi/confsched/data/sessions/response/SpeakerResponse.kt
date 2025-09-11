package io.github.droidkaigi.confsched.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
public data class SpeakerResponse(
    val profilePicture: String? = null,
    val sessions: List<Int> = emptyList(),
    val tagLine: String? = null,
    val bio: String? = null,
    val fullName: String,
    val id: String,
)
