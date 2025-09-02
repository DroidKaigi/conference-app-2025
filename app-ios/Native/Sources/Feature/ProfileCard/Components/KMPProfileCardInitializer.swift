import Foundation
import UIKit
import shared

/**
 * KMPとSwiftUIのブリッジを初期化
 * アプリ起動時に呼び出す
 */
@MainActor
public class KMPProfileCardInitializer {
    
    public static func initialize() {
        // KMPProfileCardUserFactoryの実装を設定
        KMPProfileCardUserFactory.shared.implementation = KMPProfileCardUserFactoryImpl()
        
        // KMPViewEmbedderの実装を設定
        KMPViewEmbedder.shared.implementation = KMPViewEmbedderImpl()
    }
}

// KMPProfileCardUserFactoryの実装
@MainActor
private class KMPProfileCardUserFactoryImpl: NSObject, KMPProfileCardUserFactoryProtocol {
    
    func createProfileCardUser(
        isDarkTheme: Bool,
        profileImageData: Data,
        userName: String,
        occupation: String,
        profileCardTheme: Any
    ) -> Any {
        // KMP側のcreateKMPProfileCardViewController関数を使用
        guard let themeString = profileCardTheme as? String else {
            print("Error: profileCardTheme should be String, got \(type(of: profileCardTheme))")
            // エラー時は空のViewControllerを返す
            let errorViewController = UIViewController()
            errorViewController.view.backgroundColor = UIColor.clear
            return errorViewController
        }
        
        return ProfileCardIOSBridgeKt.createKMPProfileCardViewController(
            isDarkTheme: isDarkTheme,
            profileImageData: (profileImageData as NSData) as Data,
            userName: userName,
            occupation: occupation,
            profileCardTheme: themeString
        )
    }
}

// KMPViewEmbedderの実装
@MainActor
private class KMPViewEmbedderImpl: NSObject, KMPViewEmbedderProtocol {
    
    func embedInViewController(viewController: UIViewController, kmpView: Any) {
        guard let kmpViewController = kmpView as? UIViewController else {
            print("Warning: kmpView is not a UIViewController")
            return
        }
        
        // KMPのViewControllerを子ViewControllerとして追加
        viewController.addChild(kmpViewController)
        
        // 背景を透明に設定
        kmpViewController.view.backgroundColor = UIColor.clear
        
        kmpViewController.view.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.addSubview(kmpViewController.view)
        
        // 制約を設定
        NSLayoutConstraint.activate([
            kmpViewController.view.leadingAnchor.constraint(equalTo: viewController.view.leadingAnchor),
            kmpViewController.view.trailingAnchor.constraint(equalTo: viewController.view.trailingAnchor),
            kmpViewController.view.topAnchor.constraint(equalTo: viewController.view.topAnchor),
            kmpViewController.view.bottomAnchor.constraint(equalTo: viewController.view.bottomAnchor)
        ])
        
        kmpViewController.didMove(toParent: viewController)
    }
}

