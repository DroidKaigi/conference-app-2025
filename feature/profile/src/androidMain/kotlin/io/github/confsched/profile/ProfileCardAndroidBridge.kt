package io.github.confsched.profile

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import io.github.confsched.profile.components.ProfileCardUser
import io.github.confsched.profile.components.shape
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme

/**
 * Android用のプロフィールカードデータホルダー
 */
data class AndroidProfileCardData(
    val isDarkTheme: Boolean,
    val profileImageBitmap: ImageBitmap?,
    val userName: String,
    val occupation: String,
    val profileCardTheme: ProfileCardTheme,
)

/**
 * Android用のプロフィール画像データホルダー
 */
data class AndroidProfileImageData(
    val profileImageBitmap: ImageBitmap?,
    val profileCardTheme: ProfileCardTheme,
)

/**
 * Android向けのProfileCard実装ブリッジ
 * ComposableをAndroidビューとして提供
 */
class AndroidProfileCardBridge : ProfileCardBridgeInterface {

    override fun createProfileCardUserView(
        isDarkTheme: Boolean,
        profileImageData: Any,
        userName: String,
        occupation: String,
        profileCardTheme: String,
    ): Any {
        require(profileImageData is ByteArray) { "profileImageData must be ByteArray on Android" }

        val imageBitmap = try {
            profileImageData.toImageBitmap()
        } catch (e: Exception) {
            null
        }

        // Return a data holder that can be used by Composable functions
        return AndroidProfileCardData(
            isDarkTheme = isDarkTheme,
            profileImageBitmap = imageBitmap,
            userName = userName,
            occupation = occupation,
            profileCardTheme = profileCardTheme.toProfileCardTheme(),
        )
    }

    override fun getSupportedThemes(): List<String> = listOf(
        "LightPill",
        "DarkPill",
        "LightDiamond",
        "DarkDiamond",
        "LightFlower",
        "DarkFlower",
    )
}

/**
 * Android向けのProfileCardBridgeインスタンス作成
 */
actual fun createProfileCardBridge(): ProfileCardBridgeInterface = AndroidProfileCardBridge()

/**
 * ByteArrayをImageBitmapに変換
 */
private fun ByteArray.toImageBitmap(): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return bitmap.asImageBitmap()
}

/**
 * 文字列をProfileCardThemeに変換
 */
private fun String.toProfileCardTheme(): ProfileCardTheme {
    return when (this) {
        "LightPill" -> ProfileCardTheme.LightPill
        "DarkPill" -> ProfileCardTheme.DarkPill
        "LightDiamond" -> ProfileCardTheme.LightDiamond
        "DarkDiamond" -> ProfileCardTheme.DarkDiamond
        "LightFlower" -> ProfileCardTheme.LightFlower
        "DarkFlower" -> ProfileCardTheme.DarkFlower
        else -> ProfileCardTheme.LightPill // デフォルト
    }
}
