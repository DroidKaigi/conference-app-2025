import Dependencies
import Model
import SwiftUI
@preconcurrency import UserNotifications
import os.log

public enum ScenePhaseHandler {
    static func handle(_ scenePhase: ScenePhase) {
        switch scenePhase {
        case .active:
            handleAppDidBecomeActive()

        case .inactive:
            break

        case .background:
            handleAppDidEnterBackground()

        @unknown default:
            break
        }
    }

    private static func handleAppDidBecomeActive() {
        Task {
            // Clear badge count when app becomes active
            await clearBadgeCount()

            // Check if notification permission status changed while app was inactive
            await refreshNotificationStatusIfNeeded()
        }
    }

    private static func handleAppDidEnterBackground() {
        Task {
            // Schedule background app refresh if needed
            await BackgroundTaskHandler.scheduleBackgroundRefreshIfNeeded()
        }
    }

    private static func clearBadgeCount() async {
        do {
            try await UNUserNotificationCenter.current().setBadgeCount(0)
        } catch {
            print("Failed to set badge count: \(error.localizedDescription)")
        }
    }

    private static func refreshNotificationStatusIfNeeded() async {
        // Check current authorization status
        let notificationCenter = UNUserNotificationCenter.current()
        let settings = await notificationCenter.notificationSettings()

        // Get current app settings
        @Dependency(\.notificationUseCase) var notificationUseCase
        let currentSettings = await notificationUseCase.load()

        // Post notifications for UI updates based on permission changes
        await handleNotificationPermissionChanges(
            systemStatus: settings.authorizationStatus,
            appSettings: currentSettings
        )
    }

    private static func handleNotificationPermissionChanges(
        systemStatus: UNAuthorizationStatus,
        appSettings: NotificationSettings
    ) async {
        await MainActor.run {
            // If notifications are enabled in app but denied by system
            if appSettings.isEnabled && systemStatus == .denied {
                NotificationCenter.default.post(
                    name: Notification.Name("NotificationPermissionStatusChanged"),
                    object: nil,
                    userInfo: ["status": "denied", "appEnabled": appSettings.isEnabled]
                )
            }

            // If authorization status changed to authorized and we have enabled settings
            if systemStatus == .authorized && appSettings.isEnabled {
                NotificationCenter.default.post(
                    name: Notification.Name("RefreshNotificationSchedules"),
                    object: nil
                )
            }
        }
    }
}
