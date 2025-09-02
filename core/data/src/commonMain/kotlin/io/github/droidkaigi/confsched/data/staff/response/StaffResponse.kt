package io.github.droidkaigi.confsched.data.staff.response

import kotlinx.serialization.Serializable

@Serializable
public data class StaffResponse(
    val staff: List<StaffItemResponse> = emptyList(),
)

@Serializable
public data class StaffItemResponse(
    val id: Long,
    val username: String,
    val profileUrl: String,
    val iconUrl: String,
)
