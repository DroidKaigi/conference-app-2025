import Dependencies
import SwiftUI
import shared

@MainActor
struct KmpAppComposeViewControllerWrapper: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        shared.KaigiAppViewControllerKt.kaigiAppViewController(appGraph: KMPDependencyProvider.shared.appGraph)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
