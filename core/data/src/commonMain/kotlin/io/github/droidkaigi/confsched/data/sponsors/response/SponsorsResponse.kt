package io.github.droidkaigi.confsched.data.sponsors.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SponsorsResponse(
    @SerialName("sponsor")
    val sponsors: List<SponsorResponse> = emptyList(),
)

@Serializable
internal data class SponsorResponse(
    val sponsorName: String,
    val sponsorLogo: String,
    val plan: String,
    val link: String,
)
