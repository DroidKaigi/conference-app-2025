package io.github.confsched.profile

/**
 * ProfileCard操作の共通インターフェース
 * プラットフォーム固有の実装を抽象化
 */
interface ProfileCardBridgeInterface {

    /**
     * プロフィールカードのViewController/Viewを生成
     * 戻り値の型は各プラットフォームで異なる（Any型で返す）
     */
    fun createProfileCardUserView(
        isDarkTheme: Boolean,
        profileImageData: Any, // NSData (iOS) or ByteArray (Android)
        userName: String,
        occupation: String,
        profileCardTheme: String,
    ): Any // UIViewController (iOS) or Composable (Android)

    /**
     * サポートされているテーマ名の一覧を取得
     */
    fun getSupportedThemes(): List<String>
}

/**
 * プラットフォーム固有のProfileCardBridgeインスタンス取得
 */
expect fun createProfileCardBridge(): ProfileCardBridgeInterface
