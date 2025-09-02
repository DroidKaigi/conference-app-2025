import Foundation

public struct NotificationSettings: Equatable, Sendable {
    public let isEnabled: Bool
    public let reminderTime: NotificationReminderTime
    public let useCustomSound: Bool

    public init(
        isEnabled: Bool = false,
        reminderTime: NotificationReminderTime = .tenMinutes,
        useCustomSound: Bool = false
    ) {
        self.isEnabled = isEnabled
        self.reminderTime = reminderTime
        self.useCustomSound = useCustomSound
    }
}

public enum NotificationReminderTime: Int, CaseIterable, Identifiable, Sendable {
    case fiveMinutes = 5
    case tenMinutes = 10

    public var id: Int { rawValue }

    public var displayTextKey: String {
        switch self {
        case .fiveMinutes:
            return "5 minutes before"
        case .tenMinutes:
            return "10 minutes before"
        }
    }

}
