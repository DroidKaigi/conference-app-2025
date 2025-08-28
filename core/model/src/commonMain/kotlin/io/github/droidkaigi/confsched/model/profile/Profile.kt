package io.github.droidkaigi.confsched.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val nickName: String = "",
    val occupation: String = "",
    val link: String = "",
    val imagePath: String = "",
    val theme: ProfileCardTheme = ProfileCardTheme.DarkPill,
) {
    companion object {
        val Fake = Profile(
            nickName = "Yorushika",
            occupation = "Rock band",
            link = "https://yorushika.com/",
            imagePath = "image.png"
        )
    }
}
