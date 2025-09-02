import SwiftUI
import UIKit
// import shared // KMPの共有モジュール

// Compose UIをSwiftUIに埋め込むためのラッパービュー
struct ComposeView<Content>: UIViewControllerRepresentable {
    let content: () -> Content
    
    func makeUIViewController(context: Context) -> UIViewController {
        // KMPのCompose UIをホストするViewController
        let viewController = ComposeHostingController(content: content)
        
        // 背景を透明に設定
        viewController.view.backgroundColor = UIColor.clear
        
        return viewController
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // 必要に応じて更新処理を実装
    }
}

// KMPのProfileCardUserをラップするビュー
struct ProfileCardUserView {
    let isDarkTheme: Bool
    let profileImageData: Data
    let userName: String
    let occupation: String
    let profileCardTheme: Any
    
    // KMPのCompose UIコンポーネントを呼び出す
    @MainActor func createComposeView() -> Any {
        // KMPのProfileCardUserコンポーネントを作成
        // ここではKMPとのインターフェースを定義
        // 実際の実装はKMP側のiOSMainソースセットで行う
        return KMPProfileCardUserFactory.shared.createProfileCardUser(
            isDarkTheme: isDarkTheme,
            profileImageData: profileImageData,
            userName: userName,
            occupation: occupation,
            profileCardTheme: profileCardTheme
        )
    }
}

// KMPとのブリッジ用のViewController
class ComposeHostingController<Content>: UIViewController {
    let content: () -> Content
    
    init(content: @escaping () -> Content) {
        self.content = content
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 背景を透明に設定
        view.backgroundColor = UIColor.clear
        
        // KMPのCompose UIをホスト
        Task { @MainActor in
            if let composeView = content() as? ProfileCardUserView {
                let kmpView = await composeView.createComposeView()
                // KMPのViewをUIViewControllerに追加
                embedKMPView(kmpView)
            }
        }
    }
    
    private func embedKMPView(_ kmpView: Any) {
        // KMPのCompose UIをUIViewに変換して追加
        // この実装はKMP側のプラットフォーム固有コードで行う
        KMPViewEmbedder.shared.embedInViewController(
            viewController: self,
            kmpView: kmpView
        )
    }
}

// KMPとのインターフェース定義（実装はKMP側）
@objc protocol KMPProfileCardUserFactoryProtocol: AnyObject {
    @MainActor func createProfileCardUser(
        isDarkTheme: Bool,
        profileImageData: Data,
        userName: String,
        occupation: String,
        profileCardTheme: Any
    ) -> Any
}

@objc protocol KMPViewEmbedderProtocol: AnyObject {
    @MainActor func embedInViewController(viewController: UIViewController, kmpView: Any)
}

// シングルトンインスタンス（KMP側で実装を注入）createKMPProfileCardViewController
@MainActor
class KMPProfileCardUserFactory {
    static let shared = KMPProfileCardUserFactory()
    var implementation: KMPProfileCardUserFactoryProtocol?
    
    func createProfileCardUser(
        isDarkTheme: Bool,
        profileImageData: Data,
        userName: String,
        occupation: String,
        profileCardTheme: Any
    ) -> Any {
        guard let impl = implementation else {
            // 実装が設定されていない場合は、ダミーのViewを返す
            print("Warning: KMPProfileCardUserFactory implementation not set")
            return UIView()
        }
        return impl.createProfileCardUser(
            isDarkTheme: isDarkTheme,
            profileImageData: profileImageData,
            userName: userName,
            occupation: occupation,
            profileCardTheme: profileCardTheme
        )
    }
}

@MainActor
class KMPViewEmbedder {
    static let shared = KMPViewEmbedder()
    var implementation: KMPViewEmbedderProtocol?
    
    func embedInViewController(viewController: UIViewController, kmpView: Any) {
        guard let impl = implementation else {
            // 実装が設定されていない場合は、ダミーのViewを追加
            print("Warning: KMPViewEmbedder implementation not set")
            let dummyView = UIView()
            dummyView.backgroundColor = .systemGray6
            dummyView.translatesAutoresizingMaskIntoConstraints = false
            viewController.view.addSubview(dummyView)
            NSLayoutConstraint.activate([
                dummyView.centerXAnchor.constraint(equalTo: viewController.view.centerXAnchor),
                dummyView.centerYAnchor.constraint(equalTo: viewController.view.centerYAnchor),
                dummyView.widthAnchor.constraint(equalToConstant: 150),
                dummyView.heightAnchor.constraint(equalToConstant: 200)
            ])
            return
        }
        impl.embedInViewController(viewController: viewController, kmpView: kmpView)
    }
}
