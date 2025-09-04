import Dependencies
import Foundation
import Model
import Observation
import UseCase

@Observable
public final class NotificationProvider {
    @ObservationIgnored
    @Dependency(\.notificationUseCase) private var notificationUseCase

    @ObservationIgnored
    private var settingsTask: Task<Void, Never>?

    public var settings = NotificationSettings()
    public var authorizationStatus: NotificationAuthorizationStatus = .notDetermined

    public init() {}

    @MainActor
    public func initialize() {
        guard settingsTask == nil else { return }

        settingsTask = Task {
            // Load initial settings
            self.settings = await notificationUseCase.load()
            self.authorizationStatus = await notificationUseCase.checkAuthorizationStatus()
        }
    }

    @MainActor
    public func handleFavoriteChange(_ item: TimetableItemWithFavorite) {
        Task {
            if item.isFavorited && settings.isEnabled {
                // Schedule notification for newly favorited item
                _ = await notificationUseCase.scheduleNotification(item, settings)
            } else {
                // Cancel notification for unfavorited item
                await notificationUseCase.cancelNotification(item.id)
            }
        }
    }

    @MainActor
    public func updateNotifications(for items: [TimetableItemWithFavorite]) {
        Task {
            await notificationUseCase.rescheduleAllNotifications(items, settings)
        }
    }

    @MainActor
    public func refreshSettings() {
        Task {
            self.settings = await notificationUseCase.load()
            self.authorizationStatus = await notificationUseCase.checkAuthorizationStatus()
        }
    }
}
