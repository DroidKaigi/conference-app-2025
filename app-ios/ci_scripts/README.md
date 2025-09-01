# Xcode Cloud CI Scripts

このディレクトリには、Xcode Cloud でのビルドと配布を自動化するためのスクリプトが含まれています。

## 📁 スクリプト一覧

### ci_post_clone.sh
リポジトリのクローン直後に実行されるスクリプト。
- Java 17 のインストール
- Gradle の設定
- SwiftLint/SwiftFormat のインストール
- 必要なディレクトリの作成

### ci_pre_xcodebuild.sh
Xcode ビルド開始前に実行されるスクリプト。
- KMP 共有モジュールの XCFramework ビルド
- SPM 依存関係の解決
- 環境変数の設定

### ci_post_xcodebuild.sh
Xcode ビルド完了後に実行されるスクリプト。
- ビルド結果の検証
- アーカイブの処理
- ビルドレポートの生成
- Slack 通知（設定されている場合）

### build_xcframework.sh
XCFramework をビルドするためのスタンドアロンスクリプト。
ローカル開発やデバッグにも使用可能。

```bash
# Debug ビルド
./build_xcframework.sh Debug arm64SimulatorDebug

# Release ビルド
./build_xcframework.sh Release arm64
```

## 🚀 使用方法

### Xcode Cloud での自動実行

Xcode Cloud は `ci_scripts` ディレクトリ内の以下のスクリプトを自動的に検出して実行します：
- `ci_post_clone.sh`
- `ci_pre_xcodebuild.sh`
- `ci_post_xcodebuild.sh`

特別な設定は不要です。

### ローカルでのテスト

```bash
# スクリプトに実行権限を付与
chmod +x ci_scripts/*.sh

# 個別のスクリプトをテスト
./ci_scripts/build_xcframework.sh Debug

# 環境変数を設定してテスト
export CI_WORKSPACE=$(pwd)
export CI_XCODEBUILD_ACTION=build-for-testing
./ci_scripts/ci_pre_xcodebuild.sh
```

## 🔧 環境変数

Xcode Cloud が提供する環境変数：

| 変数名 | 説明 |
|--------|------|
| CI_WORKSPACE | ワークスペースのパス |
| CI_XCODEBUILD_ACTION | ビルドアクション (build, archive, test など) |
| CI_PRODUCT | プロダクト名 |
| CI_ARCHIVE_PATH | アーカイブの出力パス |
| CI_DERIVED_DATA_PATH | DerivedData のパス |
| CI_BUILD_NUMBER | ビルド番号 |
| CI_BRANCH | 現在のブランチ |
| CI_COMMIT | コミットハッシュ |

## 🐛 トラブルシューティング

### XCFramework ビルドが失敗する

1. Java がインストールされているか確認：
```bash
java -version
```

2. Gradle Wrapper の権限を確認：
```bash
chmod +x gradlew
```

3. 手動でビルドを実行：
```bash
./gradlew :app-shared:assembleSharedDebugXCFramework
```

### SPM 依存関係の解決に失敗する

1. Package.resolved ファイルが存在するか確認
2. Xcode のバージョンを確認
3. キャッシュをクリア：
```bash
rm -rf ~/Library/Developer/Xcode/DerivedData
```

### スクリプトが実行されない

1. ファイル名が正しいか確認（`ci_` プレフィックスが必要）
2. 実行権限があるか確認：
```bash
ls -la ci_scripts/
```

## 📝 カスタマイズ

### 新しいスクリプトの追加

1. `ci_scripts` ディレクトリに新しいスクリプトを作成
2. ファイル名は `ci_` で始める必要があります
3. 実行権限を付与：
```bash
chmod +x ci_scripts/ci_your_script.sh
```

### 既存スクリプトの修正

1. スクリプトを編集
2. ローカルでテスト
3. コミットしてプッシュ

## 🔗 参考リンク

- [Xcode Cloud Documentation](https://developer.apple.com/documentation/xcode/xcode-cloud)
- [Custom Build Scripts](https://developer.apple.com/documentation/xcode/writing-custom-build-scripts)
- [Environment Variable Reference](https://developer.apple.com/documentation/xcode/environment-variable-reference)