package io.github.confsched.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.github.confsched.profile.components.ProfileCardUser
import io.github.confsched.profile.components.shape
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIColor
import platform.UIKit.UIViewController
import platform.posix.memcpy
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/**
 * トップレベル関数としてProfileCardをSwiftから直接呼び出し可能にする
 * SwiftUIからKMP Composeコンポーネントを利用するためのエントリーポイント
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName("createKMPProfileCardViewController")
fun createKMPProfileCardViewController(
    isDarkTheme: Boolean,
    profileImageData: NSData,
    userName: String,
    occupation: String,
    profileCardTheme: String,
): UIViewController = ProfileCardIOSBridge.createProfileCardUserViewController(
    isDarkTheme = isDarkTheme,
    profileImageData = profileImageData,
    userName = userName,
    occupation = occupation,
    profileCardTheme = profileCardTheme,
)

/**
 * Material3 Shapeのみを適用した画像ViewControllerを作成
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName("createKMPProfileImageViewController")
fun createKMPProfileImageViewController(
    profileImageData: NSData,
    profileCardTheme: String,
): UIViewController {
    val controller = ComposeUIViewController {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ProfileCardIOSBridge.ProfileImageWithShape(
                profileImageBitmap = profileImageData.toImageBitmap(),
                profileCardTheme = profileCardTheme.toProfileCardTheme(),
            )
        }
    }
    // UIViewControllerとビューの背景を完全に透明に設定
    controller.view.backgroundColor = UIColor.clearColor

    return controller
}

/**
 * iOS向けのProfileCard Material3 Shape実装ブリッジ
 * SwiftUIからCompose Multiplatformのコンポーネントを利用可能にする
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName("KMPProfileCardIOSBridge")
object ProfileCardIOSBridge {

    /**
     * ProfileCardUserのiOS向けUIViewController生成
     */
    @OptIn(ExperimentalObjCName::class)
    @ObjCName("createProfileCardUserViewController")
    fun createProfileCardUserViewController(
        isDarkTheme: Boolean,
        profileImageData: NSData,
        userName: String,
        occupation: String,
        profileCardTheme: String,
    ): UIViewController {
        val controller = ComposeUIViewController {
            Box(
                modifier = Modifier.background(Color.Transparent).fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                val imageBitmap = try {
                    profileImageData.toImageBitmap()
                } catch (e: Exception) {
                    null
                }

                if (imageBitmap != null) {
                    ProfileImageWithShape(
                        profileImageBitmap = imageBitmap,
                        profileCardTheme = profileCardTheme.toProfileCardTheme(),
                    )
                } else {
                    ProfileImageWithShapeTest(
                        profileCardTheme = profileCardTheme.toProfileCardTheme(),
                    )
                }
            }
        }

        // UIViewControllerとビューの背景を完全に透明に設定
        controller.view.backgroundColor = UIColor.clearColor
        controller.view.opaque = false // 不透明フラグをfalseに
        controller.view.layer.opaque = false // レイヤーも透明に

        return controller
    }

    /**
     * Material3 Shapeを適用したProfileCardUserコンポーネント
     */
    @Composable
    fun ProfileCardUserWithShape(
        isDarkTheme: Boolean,
        profileImageBitmap: ImageBitmap,
        userName: String,
        occupation: String,
        profileCardTheme: ProfileCardTheme,
    ) {
        ProfileCardUser(
            isDarkTheme = isDarkTheme,
            profileImageBitmap = profileImageBitmap,
            userName = userName,
            occupation = occupation,
            profileShape = profileCardTheme.shape, // Material3 Shapeを使用
        )
    }

    /**
     * 画像のみをMaterial3 Shapeでクリップしたコンポーネント
     */
    @Composable
    fun ProfileImageWithShape(
        profileImageBitmap: ImageBitmap,
        profileCardTheme: ProfileCardTheme,
        modifier: Modifier = Modifier,
    ) {
        Image(
            bitmap = profileImageBitmap,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(150.dp)
                .clip(profileCardTheme.shape)
                .background(Color.Transparent),
        )
    }

    /**
     * テスト用：Material3形状のクロッピング動作確認用の色付きコンポーネント
     */
    @Composable
    fun ProfileImageWithShapeTest(
        profileCardTheme: ProfileCardTheme,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .size(150.dp)
                .clip(profileCardTheme.shape) // Material3 Shapeでクリップ
                .background(
                    when (profileCardTheme) {
                        ProfileCardTheme.LightPill, ProfileCardTheme.DarkPill ->
                            Color.Blue
                        ProfileCardTheme.LightDiamond, ProfileCardTheme.DarkDiamond ->
                            Color.Green
                        ProfileCardTheme.LightFlower, ProfileCardTheme.DarkFlower ->
                            Color.Red
                    },
                ),
        )
    }
}

/**
 * 透明なImageBitmapを作成するヘルパー関数
 */
private fun createTransparentImageBitmap(width: Int, height: Int): ImageBitmap {
    return androidx.compose.ui.graphics.ImageBitmap(
        width = width,
        height = height,
        config = ImageBitmapConfig.Argb8888,
        hasAlpha = true,
    )
}

/**
 * NSDataをImageBitmapに変換
 * アルファチャンネルを保持してSkikoのImage.toComposeImageBitmap()を使用
 */
@OptIn(ExperimentalForeignApi::class)
private fun NSData.toImageBitmap(): ImageBitmap {
    return try {
        // 直接NSDataからSkikoのImageを作成（PNG変換を経由しない）
        val bytes = ByteArray(this.length.toInt())
        bytes.usePinned { pinned ->
            memcpy(pinned.addressOf(0), this.bytes, this.length)
        }

        // SkikoのImageからImageBitmapを作成
        val skikoImage = org.jetbrains.skia.Image.makeFromEncoded(bytes)
        skikoImage.toComposeImageBitmap()
    } catch (e: Exception) {
        // 変換に失敗した場合は透明なプレースホルダーを返す
        createTransparentImageBitmap(150, 150)
    }
}

/**
 * iOS向けの共通インターフェース実装
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName("IOSProfileCardBridge")
class IOSProfileCardBridge : ProfileCardBridgeInterface {

    override fun createProfileCardUserView(
        isDarkTheme: Boolean,
        profileImageData: Any,
        userName: String,
        occupation: String,
        profileCardTheme: String,
    ): Any {
        require(profileImageData is NSData) { "profileImageData must be NSData on iOS" }
        return ProfileCardIOSBridge.createProfileCardUserViewController(
            isDarkTheme = isDarkTheme,
            profileImageData = profileImageData,
            userName = userName,
            occupation = occupation,
            profileCardTheme = profileCardTheme,
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

    companion object {
        @OptIn(ExperimentalObjCName::class)
        @ObjCName("shared")
        val shared = IOSProfileCardBridge()
    }
}

/**
 * iOS向けのProfileCardBridgeインスタンス作成
 */
actual fun createProfileCardBridge(): ProfileCardBridgeInterface = IOSProfileCardBridge()

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
