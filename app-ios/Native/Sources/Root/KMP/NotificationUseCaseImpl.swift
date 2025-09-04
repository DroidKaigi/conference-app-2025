import Foundation
import Model
import UseCase
import UserNotifications
import os.log

public final class NotificationUseCaseImpl: NSObject, @unchecked Sendable {
    private let notificationCenter = UNUserNotificationCenter.current()
    private let userDefaults = UserDefaults.standard

    private static let maxNotificationLimit = 64

    private weak var navigationHandler: NotificationNavigationHandler?

    private enum StorageKeys {
        static let enabled = "notification_enabled"
        static let reminderMinutes = "notification_reminder_minutes"
        static let customSound = "notification_custom_sound"
        static let lastScheduledVersion = "notification_last_scheduled_version"
    }

    override init() {
        super.init()
        setupNotificationCategories()
    }

    private func setupNotificationCategories() {
        let viewAction = UNNotificationAction(
            identifier: "VIEW_SESSION",
            title: NSLocalizedString("View Session", comment: "Notification action to view session"),
            options: [.foreground]
        )

        let dismissAction = UNNotificationAction(
            identifier: "DISMISS",
            title: NSLocalizedString("Dismiss", comment: "Notification action to dismiss"),
            options: []
        )

        let sessionCategory = UNNotificationCategory(
            identifier: "SESSION_REMINDER",
            actions: [viewAction, dismissAction],
            intentIdentifiers: [],
            options: [.customDismissAction]
        )

        notificationCenter.setNotificationCategories([sessionCategory])
    }

    @MainActor
    public func setNavigationHandler(_ handler: NotificationNavigationHandler?) {
        self.navigationHandler = handler
    }

    deinit {
        // Clear delegate if it's still us to avoid dangling reference
        if notificationCenter.delegate === self {
            notificationCenter.delegate = nil
        }
    }

    public func load() async -> NotificationSettings {
        let isEnabled = userDefaults.bool(forKey: StorageKeys.enabled)
        let reminderMinutes = userDefaults.object(forKey: StorageKeys.reminderMinutes) as? Int ?? 10
        let reminderTime: NotificationReminderTime = reminderMinutes == 5 ? .fiveMinutes : .tenMinutes
        let useCustomSound = userDefaults.bool(forKey: StorageKeys.customSound)

        let settings = NotificationSettings(
            isEnabled: isEnabled,
            reminderTime: reminderTime,
            useCustomSound: useCustomSound
        )

        return settings
    }

    public func save(_ settings: NotificationSettings) async {
        userDefaults.set(settings.isEnabled, forKey: StorageKeys.enabled)
        userDefaults.set(settings.reminderTime.rawValue, forKey: StorageKeys.reminderMinutes)
        userDefaults.set(settings.useCustomSound, forKey: StorageKeys.customSound)

        // Increment version to trigger re-scheduling
        let currentVersion = userDefaults.integer(forKey: StorageKeys.lastScheduledVersion)
        userDefaults.set(currentVersion + 1, forKey: StorageKeys.lastScheduledVersion)
    }

    public func requestPermission() async -> Bool {
        do {
            let granted = try await notificationCenter.requestAuthorization(
                options: [.alert, .sound, .badge]
            )
            return granted
        } catch {
            print("Error requesting notification permission: \(error.localizedDescription)")
            return false
        }
    }

    public func checkAuthorizationStatus() async -> NotificationAuthorizationStatus {
        let settings = await notificationCenter.notificationSettings()

        let status: NotificationAuthorizationStatus
        switch settings.authorizationStatus {
        case .notDetermined:
            status = .notDetermined
        case .denied:
            status = .denied
        case .authorized:
            status = .authorized
        case .provisional:
            status = .provisional
        case .ephemeral:
            status = .authorized
        @unknown default:
            status = .notDetermined
        }

        return status
    }

    public func scheduleNotification(
        for item: TimetableItemWithFavorite, with settings: NotificationSettings
    ) async -> Bool {
        guard settings.isEnabled else {
            return false
        }

        guard item.isFavorited else {
            return false
        }

        // Use Japan Standard Time (JST) for conference events
        let jstTimeZone = TimeZone(identifier: "Asia/Tokyo") ?? TimeZone.current
        var jstCalendar = Calendar(identifier: .gregorian)
        jstCalendar.timeZone = jstTimeZone

        // Calculate notification time in JST
        let sessionStartTime = item.timetableItem.startsAt
        let reminderInterval = TimeInterval(settings.reminderTime.rawValue * 60)
        let notificationTime = sessionStartTime.addingTimeInterval(-reminderInterval)

        // Don't schedule notifications for past events (compare in JST timezone)
        let currentTimeInJST = Date()

        guard notificationTime > currentTimeInJST else {
            return false
        }

        // Create notification content with localized strings based on app language
        let content = UNMutableNotificationContent()
        content.title = NSLocalizedString(
            "DroidKaigi Session Reminder", comment: "Notification title for session reminders")

        let bodyFormat = NSLocalizedString(
            "%@ starts in %d minutes",
            comment: "Notification body format with session title and minutes"
        )
        content.body = String.localizedStringWithFormat(
            bodyFormat, item.timetableItem.title.currentLangTitle, settings.reminderTime.rawValue)

        // Add custom sound if enabled and available
        if settings.useCustomSound {
            // Check if custom sound file exists in bundle
            if let soundPath = Bundle.main.path(forResource: "droidkaigi_notification", ofType: "wav"),
                FileManager.default.fileExists(atPath: soundPath)
            {
                content.sound = UNNotificationSound(named: UNNotificationSoundName("droidkaigi_notification.wav"))
            } else {
                content.sound = .default
            }
        } else {
            content.sound = .default
        }

        // Rich notification data
        content.userInfo = [
            "itemId": item.id.value,
            "sessionTitle": item.timetableItem.title.currentLangTitle,
            "room": item.timetableItem.room.name.currentLangTitle,
            "startTime": sessionStartTime.timeIntervalSince1970,
            "reminderMinutes": settings.reminderTime.rawValue,
        ]

        // Add category for actionable notifications
        content.categoryIdentifier = "SESSION_REMINDER"

        // Create date trigger using JST timezone
        var dateComponents = jstCalendar.dateComponents([.year, .month, .day, .hour, .minute], from: notificationTime)
        dateComponents.timeZone = jstTimeZone
        let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: false)

        // Create request
        let identifier = "session-\(item.id.value)"
        let request = UNNotificationRequest(
            identifier: identifier,
            content: content,
            trigger: trigger
        )

        do {
            try await notificationCenter.add(request)
            return true
        } catch {
            print("Error scheduling notification for \(item.id.value): \(error.localizedDescription)")
            return false
        }
    }

    public func cancelNotification(for itemId: TimetableItemId) async {
        let identifier = "session-\(itemId.value)"
        notificationCenter.removePendingNotificationRequests(withIdentifiers: [identifier])
    }

    public func rescheduleAllNotifications(
        for items: [TimetableItemWithFavorite], with settings: NotificationSettings
    ) async {
        // Cancel all existing notifications
        await cancelAllNotifications()

        // Schedule new notifications if enabled
        guard settings.isEnabled else {
            return
        }

        let authStatus = await checkAuthorizationStatus()
        guard authStatus == .authorized || authStatus == .provisional else {
            return
        }

        // Filter eligible items (favorites only)
        let eligibleItems = items.filter(\.isFavorited)

        // Apply 64-notification limit with priority system
        let prioritizedItems = prioritizeNotifications(eligibleItems, limit: Self.maxNotificationLimit)

        // Schedule notifications for prioritized items
        var successCount = 0
        for item in prioritizedItems {
            if await scheduleNotification(for: item, with: settings) {
                successCount += 1
            }
        }

        await debugPendingNotifications()
    }

    public func cancelAllNotifications() async {
        notificationCenter.removeAllPendingNotificationRequests()
    }

    // Debug function to check pending notifications
    func debugPendingNotifications() async {
        let pendingRequests = await notificationCenter.pendingNotificationRequests()

        for request in pendingRequests {
            let trigger = request.trigger
            var triggerDescription = "Unknown trigger"

            if let calendarTrigger = trigger as? UNCalendarNotificationTrigger {
                triggerDescription = "Calendar trigger: \(String(describing: calendarTrigger.dateComponents))"
            } else if let timeIntervalTrigger = trigger as? UNTimeIntervalNotificationTrigger {
                triggerDescription = "Time interval trigger: \(timeIntervalTrigger.timeInterval) seconds"
            }

            print("Notification \(request.identifier): '\(request.content.title)' - \(triggerDescription)")
        }
    }

    private func prioritizeNotifications(
        _ items: [TimetableItemWithFavorite], limit: Int
    ) -> [TimetableItemWithFavorite] {
        let sortedItems = items.sorted { item1, item2 in
            // First priority: favorited status
            if item1.isFavorited != item2.isFavorited {
                return item1.isFavorited && !item2.isFavorited
            }

            // Second priority: earlier sessions first
            return item1.timetableItem.startsAt < item2.timetableItem.startsAt
        }

        // Filter out past sessions (using JST timezone for consistency)
        let jstTimeZone = TimeZone(identifier: "Asia/Tokyo") ?? TimeZone.current
        let currentTime = Date()

        var pastItemsCount = 0
        let futureItems = sortedItems.filter { item in
            let isPast = item.timetableItem.startsAt <= currentTime
            if isPast {
                pastItemsCount += 1
            }
            return !isPast
        }

        // Apply limit
        let finalItems = Array(futureItems.prefix(limit))

        return finalItems
    }
}

extension NotificationUseCaseImpl: UNUserNotificationCenterDelegate {
    nonisolated public func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo

        if let itemIdString = userInfo["itemId"] as? String {
            // Handle different action types
            switch response.actionIdentifier {
            case "VIEW_SESSION", UNNotificationDefaultActionIdentifier:
                // Navigate to session detail
                // Extract needed values to avoid capturing the entire dictionary
                let sessionTitle = userInfo["sessionTitle"] as? String ?? "Unknown Session"
                let room = userInfo["room"] as? String ?? "Unknown Room"
                let itemId = itemIdString  // Create local copy to avoid sending parameter race
                Task { @MainActor in
                    await handleNotificationTap(itemId: itemId, sessionTitle: sessionTitle, room: room)
                }
            default: break
            }
        }

        completionHandler()
    }

    nonisolated public func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        // Show notification even when app is in foreground
        completionHandler([.banner, .sound, .badge])
    }

    private func handleNotificationTap(itemId: String, sessionTitle: String, room: String) async {
        // Store the itemId in UserDefaults as a fallback for when app is launched from terminated state
        // This ensures that if the navigation handler is not available (e.g., app just launched),
        // the navigation info is preserved for the next app launch cycle.
        UserDefaults.standard.set(itemId, forKey: "pending_notification_item_id")

        guard let navigationHandler = self.navigationHandler else {
            return
        }

        // Navigate to session detail
        await navigationHandler.navigateToSession(itemId: itemId)

        // If navigation was successful, clear the pending notification
        UserDefaults.standard.removeObject(forKey: "pending_notification_item_id")
    }

    public func scheduleNotification(_ item: TimetableItemWithFavorite, _ settings: NotificationSettings) async -> Bool
    {
        await scheduleNotification(for: item, with: settings)
    }

    public func cancelNotification(_ itemId: TimetableItemId) async {
        await cancelNotification(for: itemId)
    }

    public func rescheduleAllNotifications(_ items: [TimetableItemWithFavorite], _ settings: NotificationSettings) async
    {
        await rescheduleAllNotifications(for: items, with: settings)
    }
}

enum NotificationError: Error, LocalizedError {
    case permissionDenied
    case soundFileNotFound(String)
    case schedulingFailed(String)
    case invalidItemId(String)
    case navigationHandlerMissing

    var errorDescription: String? {
        switch self {
        case .permissionDenied:
            return NSLocalizedString("Notification permission was denied", comment: "Permission error")
        case .soundFileNotFound(let filename):
            return NSLocalizedString("Sound file '\(filename)' not found in app bundle", comment: "Sound file error")
        case .schedulingFailed(let reason):
            return NSLocalizedString("Failed to schedule notification: \(reason)", comment: "Scheduling error")
        case .invalidItemId(let itemId):
            return NSLocalizedString("Invalid item ID: \(itemId)", comment: "Invalid ID error")
        case .navigationHandlerMissing:
            return NSLocalizedString("Navigation handler not available", comment: "Navigation error")
        }
    }
}
