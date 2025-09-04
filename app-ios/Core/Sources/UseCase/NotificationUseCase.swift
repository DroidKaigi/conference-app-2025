import Dependencies
import DependenciesMacros
import Model

@DependencyClient
public struct NotificationUseCase: Sendable {
    public var load: @Sendable () async -> NotificationSettings = { NotificationSettings() }

    public var save: @Sendable (NotificationSettings) async -> Void = { _ in }

    public var requestPermission: @Sendable () async -> Bool = { false }

    public var checkAuthorizationStatus: @Sendable () async -> NotificationAuthorizationStatus = { .notDetermined }

    public var scheduleNotification: @Sendable (TimetableItemWithFavorite, NotificationSettings) async -> Bool = {
        _, _ in false
    }

    public var cancelNotification: @Sendable (TimetableItemId) async -> Void = { _ in }

    public var rescheduleAllNotifications: @Sendable ([TimetableItemWithFavorite], NotificationSettings) async -> Void =
        { _, _ in }

    public var cancelAllNotifications: @Sendable () async -> Void = {}

    public var setNavigationHandler: @MainActor @Sendable (NotificationNavigationHandler?) -> Void = { _ in }
}

@MainActor
public protocol NotificationNavigationHandler: AnyObject {
    func navigateToSession(itemId: String) async
}

public enum NotificationUseCaseKey: TestDependencyKey {
    public static let testValue = NotificationUseCase()
}

extension DependencyValues {
    public var notificationUseCase: NotificationUseCase {
        get { self[NotificationUseCaseKey.self] }
        set { self[NotificationUseCaseKey.self] = newValue }
    }
}
