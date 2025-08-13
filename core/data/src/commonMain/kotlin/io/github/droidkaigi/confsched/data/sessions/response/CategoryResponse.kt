package io.github.droidkaigi.confsched.data.sessions.response

import io.github.droidkaigi.confsched.data.core.LocaledResponse
import kotlinx.serialization.Serializable

@Serializable
public data class CategoryResponse(
    val id: Int,
    val sort: Int,
    val title: LocaledResponse,
    val items: List<CategoryItemResponse> = emptyList(),
)
