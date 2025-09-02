//
//  DroidKaigi2025App.swift
//  DroidKaigi2025
//
//  Created by Ryoya Ito on 2025/06/28.
//

import BackgroundTasks
import Root
import SwiftUI
import Theme

@main
struct DroidKaigi2025App: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    private let inlineTitleFontSize: CGFloat = 20
    private let titleFontSize: CGFloat = 28

    init() {
        ThemeFonts.registerAll()
        registerBackgroundTasks()

        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()

        let inlineTitleBase =
            UIFont(name: changoFontName, size: inlineTitleFontSize) ?? .systemFont(ofSize: inlineTitleFontSize)
        let titleBase = UIFont(name: changoFontName, size: titleFontSize) ?? .systemFont(ofSize: titleFontSize)

        let inlineTitleFont = UIFontMetrics(forTextStyle: .headline).scaledFont(for: inlineTitleBase)
        let titleFont = UIFontMetrics(forTextStyle: .headline).scaledFont(for: titleBase)

        appearance.titleTextAttributes = [
            .font: inlineTitleFont
        ]
        appearance.largeTitleTextAttributes = [
            .font: titleFont
        ]

        let bar = UINavigationBar.appearance()
        bar.standardAppearance = appearance
        bar.compactAppearance = appearance
        bar.scrollEdgeAppearance = appearance
    }

    private func registerBackgroundTasks() {
        BackgroundTaskHandler.registerBackgroundTasks()
    }

    var body: some Scene {
        WindowGroup {
            RootScreen()
        }
    }
}
