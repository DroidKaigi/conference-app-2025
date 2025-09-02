import Foundation
import os.log

public final class NotificationLaunchHandler {
    @MainActor public static let shared = NotificationLaunchHandler()

    private var launchNotificationUserInfo: [AnyHashable: Any]?

    private init() {}

    public func storeLaunchNotification(_ userInfo: [AnyHashable: Any]) {
        launchNotificationUserInfo = userInfo
    }

    public func consumeLaunchNotification() -> [AnyHashable: Any]? {
        defer { launchNotificationUserInfo = nil }
        return launchNotificationUserInfo
    }

    public var hasLaunchNotification: Bool {
        return launchNotificationUserInfo != nil
    }
}
