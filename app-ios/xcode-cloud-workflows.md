# Xcode Cloud ワークフロー設定ガイド

このドキュメントでは、DroidKaigi 2025 iOS アプリの Xcode Cloud ワークフロー設定方法を説明します。

## 📋 前提条件

1. Apple Developer Program への登録
2. Xcode Cloud へのアクセス権限
3. App Store Connect での アプリの作成

## 🚀 セットアップ手順

### 1. Xcode でプロジェクトを開く

```bash
cd app-ios
open DroidKaigi2025.xcodeproj
```

### 2. Xcode Cloud の初期設定

1. Xcode メニューから `Product` > `Xcode Cloud` > `Create Workflow` を選択
2. アプリを選択して `Next` をクリック
3. リポジトリへのアクセスを設定（GitHub との連携）

### 3. ワークフローの作成

以下の3つのワークフローを作成することを推奨します：

## 📱 ワークフロー設定

### Workflow 1: Pull Request ビルド

**基本設定：**
- **Name**: PR Build & Test
- **Start Conditions**: 
  - Pull Request Changes
  - Source Branch: `*` (すべてのブランチ)
  - Target Branch: `main`, `ios-distribution`

**Environment：**
- **Xcode Version**: Latest Release
- **macOS Version**: Latest

**Actions：**
1. Archive (iOS)
   - Scheme: `DroidKaigi2025`
   - Platform: iOS
   - Configuration: Debug

2. Test
   - Scheme: `DroidKaigi2025`
   - Platform: iOS Simulator
   - Destination: iPhone 16 Pro

**Post-Actions：**
- Notify via Slack/Email on failure

### Workflow 2: メインブランチビルド

**基本設定：**
- **Name**: Main Branch Build
- **Start Conditions**: 
  - Branch Changes
  - Branch: `main`, `ios-distribution`

**Environment：**
- **Xcode Version**: Latest Release
- **macOS Version**: Latest

**Actions：**
1. Archive (iOS)
   - Scheme: `DroidKaigi2025`
   - Platform: iOS
   - Configuration: Release
   - Distribution Preparation: TestFlight (Internal Testing)

**Post-Actions：**
- Deploy to TestFlight (Internal)
- Notify team via Slack

### Workflow 3: リリースビルド

**基本設定：**
- **Name**: Release Build
- **Start Conditions**: 
  - Tag Changes
  - Tags starting with: `ios-v*`

**Environment：**
- **Xcode Version**: Latest Release (Stable)
- **macOS Version**: Latest

**Actions：**
1. Archive (iOS)
   - Scheme: `DroidKaigi2025`
   - Platform: iOS
   - Configuration: Release
   - Distribution Preparation: TestFlight and App Store

**Post-Actions：**
- Deploy to TestFlight (External Testing)
- Submit for App Store Review (Manual)
- Create GitHub Release

## 🔧 カスタムビルドスクリプト

Xcode Cloud は自動的に `ci_scripts` ディレクトリ内のスクリプトを実行します：

### 実行順序

1. **ci_post_clone.sh** - リポジトリクローン後
   - Java のインストール
   - Gradle の設定
   - 依存関係のダウンロード

2. **ci_pre_xcodebuild.sh** - ビルド前
   - XCFramework のビルド
   - SPM 依存関係の解決
   - 環境変数の設定

3. **ci_post_xcodebuild.sh** - ビルド後
   - ビルドレポートの生成
   - アーカイブの処理
   - 通知の送信

## 🔑 環境変数とシークレット

### Xcode Cloud で設定する環境変数

1. **APP_ENVIRONMENT**
   - `development` (デバッグビルド)
   - `production` (リリースビルド)

2. **SLACK_WEBHOOK_URL** (オプション)
   - Slack 通知用の Webhook URL

### シークレットの管理

App Store Connect > Xcode Cloud > Settings > Environment Variables で設定：

- API キーなどの機密情報は Secret として設定
- ビルド番号は自動インクリメント設定を使用

## 📦 配布設定

### TestFlight 内部テスト

1. ワークフローの Post-Actions で設定
2. 自動配布グループ: `Internal Testers`
3. ビルド完了後自動配信

### TestFlight 外部テスト

1. タグベースのワークフローで設定
2. 配布グループ: `Beta Testers`
3. リリースノートの自動生成

### App Store リリース

1. 手動承認プロセス
2. メタデータの事前準備
3. 段階的リリースの設定

## 🐛 トラブルシューティング

### よくある問題と解決方法

#### 1. XCFramework ビルドエラー

```bash
# ローカルでテスト
./app-ios/ci_scripts/build_xcframework.sh Debug
```

#### 2. Java/Gradle の問題

- `ci_post_clone.sh` で Java 17 がインストールされているか確認
- Gradle Wrapper の実行権限を確認

#### 3. SPM 依存関係エラー

- Package.resolved ファイルをコミット
- Xcode Cloud のキャッシュをクリア

## 📊 ビルド状況の確認

### Xcode Cloud ダッシュボード

- https://appstoreconnect.apple.com でログイン
- Xcode Cloud セクションでビルド履歴を確認

### Xcode 内での確認

- Report Navigator (⌘9) でビルドレポートを確認
- Cloud Reports タブでビルド詳細を確認

## 🔄 CI/CD ベストプラクティス

1. **ブランチ保護ルール**
   - main ブランチへの直接プッシュを禁止
   - PR のビルド成功を必須に

2. **ビルド番号管理**
   - Xcode Cloud の自動インクリメントを使用
   - $(CI_BUILD_NUMBER) を活用

3. **テスト戦略**
   - Unit テストは全ワークフローで実行
   - UI テストはリリースワークフローのみ

4. **通知設定**
   - 失敗時のみ通知
   - リリースビルドは常に通知

## 📝 メンテナンス

### スクリプトの更新

```bash
# スクリプトを編集後、実行権限を付与
chmod +x app-ios/ci_scripts/*.sh

# ローカルでテスト
./app-ios/ci_scripts/ci_pre_xcodebuild.sh
```

### ワークフローの更新

1. Xcode Cloud で既存ワークフローを編集
2. または Xcode から直接更新

## 🆘 サポート

問題が発生した場合：
1. Xcode Cloud のビルドログを確認
2. ローカルで同じスクリプトを実行してデバッグ
3. Apple Developer Forums で質問