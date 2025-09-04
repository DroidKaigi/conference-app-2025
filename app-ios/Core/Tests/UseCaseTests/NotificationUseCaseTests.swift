import Dependencies
import Foundation
import Model
@testable import UseCase
import Testing

// Test utilities
enum NotificationTestData {
    static func createTimetableItemWithFavorite(
        id: String = "test-item",
        isFavorited: Bool = false
    ) -> TimetableItemWithFavorite {
        let item = createTimetableItemSession(id: id)
        return TimetableItemWithFavorite(timetableItem: item, isFavorited: isFavorited)
    }

    static func createTimetableItemSession(
        id: String = "1",
        startsAt: Date = Date().addingTimeInterval(3600), // 1 hour in the future
        endsAt: Date = Date().addingTimeInterval(7200) // 2 hours in the future
    ) -> TimetableItemSession {
        TimetableItemSession(
            id: TimetableItemId(value: id),
            title: MultiLangText(jaTitle: "テストセッション", enTitle: "Test Session"),
            startsAt: startsAt,
            endsAt: endsAt,
            category: TimetableCategory(id: 1, title: MultiLangText(jaTitle: "開発", enTitle: "Development")),
            sessionType: .regular,
            room: createRoom(),
            targetAudience: "All levels",
            language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: true),
            asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
            levels: ["Beginner"],
            speakers: [],
            description: MultiLangText(jaTitle: "説明", enTitle: "Description"),
            message: nil,
            day: .conferenceDay1
        )
    }

    static func createRoom() -> Room {
        Room(
            id: 1,
            name: MultiLangText(jaTitle: "Room J", enTitle: "Room J"),
            type: .roomJ,
            sort: 1
        )
    }
}

struct NotificationUseCaseTests {
    @MainActor
    @Test("Test notification settings loading")
    func testLoad() async throws {
        let expectedSettings = NotificationSettings(
            isEnabled: true,
            reminderTime: .fiveMinutes,
            useCustomSound: true
        )

        let settings = await withDependencies {
            $0.notificationUseCase.load = { expectedSettings }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.load()

        #expect(settings.isEnabled == true)
        #expect(settings.reminderTime == .fiveMinutes)
        #expect(settings.reminderTime.rawValue == 5)
        #expect(settings.useCustomSound == true)
    }

    @MainActor
    @Test("Test notification settings saving")
    func testSave() async throws {
        let settingsToSave = NotificationSettings(
            isEnabled: true,
            reminderTime: .tenMinutes,
            useCustomSound: false
        )

        let savedSettings = LockIsolated<NotificationSettings?>(nil)

        await withDependencies {
            $0.notificationUseCase.save = { settings in
                savedSettings.setValue(settings)
            }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.save(settingsToSave)

        #expect(savedSettings.value != nil)
        #expect(savedSettings.value?.isEnabled == true)
        #expect(savedSettings.value?.reminderTime == .tenMinutes)
        #expect(savedSettings.value?.reminderTime.rawValue == 10)
        #expect(savedSettings.value?.useCustomSound == false)
    }

    @MainActor
    @Test("Test request permission success")
    func testRequestPermissionSuccess() async throws {
        let granted = await withDependencies {
            $0.notificationUseCase.requestPermission = { true }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.requestPermission()

        #expect(granted == true)
    }

    @MainActor
    @Test("Test request permission denied")
    func testRequestPermissionDenied() async throws {
        let granted = await withDependencies {
            $0.notificationUseCase.requestPermission = { false }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.requestPermission()

        #expect(granted == false)
    }

    @MainActor
    @Test("Test authorization status check")
    func testCheckAuthorizationStatus() async throws {
        let expectedStatus = NotificationAuthorizationStatus.authorized

        let status = await withDependencies {
            $0.notificationUseCase.checkAuthorizationStatus = { expectedStatus }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.checkAuthorizationStatus()

        #expect(status == .authorized)
    }

    @MainActor
    @Test("Test schedule notification for favorited item")
    func testScheduleNotificationFavorited() async throws {
        let favoritedItem = NotificationTestData.createTimetableItemWithFavorite(
            id: "test-session",
            isFavorited: true
        )

        let settings = NotificationSettings(
            isEnabled: true,
            reminderTime: .tenMinutes,
            useCustomSound: false
        )

        let scheduledItem = LockIsolated<TimetableItemWithFavorite?>(nil)
        let scheduledSettings = LockIsolated<NotificationSettings?>(nil)

        let success = await withDependencies {
            $0.notificationUseCase.scheduleNotification = { item, settings in
                scheduledItem.setValue(item)
                scheduledSettings.setValue(settings)
                return true
            }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.scheduleNotification(favoritedItem, settings)

        #expect(success == true)
        #expect(scheduledItem.value?.id == favoritedItem.id)
        #expect(scheduledItem.value?.isFavorited == true)
        #expect(scheduledSettings.value?.isEnabled == true)
        #expect(scheduledSettings.value?.reminderTime == .tenMinutes)
    }

    @MainActor
    @Test("Test schedule notification fails when disabled")
    func testScheduleNotificationDisabled() async throws {
        let favoritedItem = NotificationTestData.createTimetableItemWithFavorite(
            id: "test-session",
            isFavorited: true
        )

        let settings = NotificationSettings(
            isEnabled: false, // Disabled
            reminderTime: .tenMinutes,
            useCustomSound: false
        )

        let success = await withDependencies {
            $0.notificationUseCase.scheduleNotification = { _, _ in false }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.scheduleNotification(favoritedItem, settings)

        #expect(success == false)
    }

    @MainActor
    @Test("Test cancel notification")
    func testCancelNotification() async throws {
        let itemId = TimetableItemId(value: "test-session")
        let cancelledId = LockIsolated<TimetableItemId?>(nil)

        await withDependencies {
            $0.notificationUseCase.cancelNotification = { id in
                cancelledId.setValue(id)
            }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.cancelNotification(itemId)

        #expect(cancelledId.value == itemId)
    }

    @MainActor
    @Test("Test reschedule all notifications")
    func testRescheduleAllNotifications() async throws {
        let items = [
            NotificationTestData.createTimetableItemWithFavorite(id: "session-1", isFavorited: true),
            NotificationTestData.createTimetableItemWithFavorite(id: "session-2", isFavorited: false),
            NotificationTestData.createTimetableItemWithFavorite(id: "session-3", isFavorited: true)
        ]

        let settings = NotificationSettings(
            isEnabled: true,
            reminderTime: .fiveMinutes,
            useCustomSound: false
        )

        let rescheduledItems = LockIsolated<[TimetableItemWithFavorite]?>(nil)
        let rescheduledSettings = LockIsolated<NotificationSettings?>(nil)

        await withDependencies {
            $0.notificationUseCase.rescheduleAllNotifications = { items, settings in
                rescheduledItems.setValue(items)
                rescheduledSettings.setValue(settings)
            }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.rescheduleAllNotifications(items, settings)

        #expect(rescheduledItems.value?.count == 3)
        #expect(rescheduledSettings.value?.isEnabled == true)
    }

    @MainActor
    @Test("Test cancel all notifications")
    func testCancelAllNotifications() async throws {
        let cancelAllCalled = LockIsolated<Bool>(false)

        await withDependencies {
            $0.notificationUseCase.cancelAllNotifications = {
                cancelAllCalled.setValue(true)
            }
        } operation: {
            @Dependency(\.notificationUseCase) var notificationUseCase
            return notificationUseCase
        }.cancelAllNotifications()

        #expect(cancelAllCalled.value == true)
    }
}

struct NotificationSettingsTests {
    @Test("Test default notification settings")
    func testDefaultSettings() {
        let settings = NotificationSettings()

        #expect(settings.isEnabled == false)
        #expect(settings.reminderTime == .tenMinutes)
        #expect(settings.reminderTime.rawValue == 10)
        #expect(settings.useCustomSound == false)
    }

    @Test("Test type-safe notification settings")
    func testTypeSafeSettings() {
        let settings = NotificationSettings(
            isEnabled: true,
            reminderTime: .fiveMinutes,
            useCustomSound: true
        )

        #expect(settings.isEnabled == true)
        #expect(settings.reminderTime == .fiveMinutes)
        #expect(settings.reminderTime.rawValue == 5)
        #expect(settings.useCustomSound == true)
    }

    @Test("Test notification settings equality")
    func testSettingsEquality() {
        let settings1 = NotificationSettings(
            isEnabled: true,
            reminderTime: .fiveMinutes,
            useCustomSound: false
        )

        let settings2 = NotificationSettings(
            isEnabled: true,
            reminderTime: .fiveMinutes,
            useCustomSound: false
        )

        let settings3 = NotificationSettings(
            isEnabled: false, // Different
            reminderTime: .fiveMinutes,
            useCustomSound: false
        )

        #expect(settings1 == settings2)
        #expect(settings1 != settings3)
    }
}

struct NotificationReminderTimeTests {
    @Test("Test reminder time cases")
    func testReminderTimeCases() {
        let allCases = NotificationReminderTime.allCases

        #expect(allCases.count == 2)
        #expect(allCases.contains(.fiveMinutes))
        #expect(allCases.contains(.tenMinutes))
    }

    @Test("Test reminder time values (updated)")
    func testReminderTimeValues() {
        #expect(NotificationReminderTime.fiveMinutes.rawValue == 5)
        #expect(NotificationReminderTime.tenMinutes.rawValue == 10)
    }

    @Test("Test reminder time display text (updated)")
    func testReminderTimeDisplayText() {
        #expect(NotificationReminderTime.fiveMinutes.displayTextKey == "5 minutes before")
        #expect(NotificationReminderTime.tenMinutes.displayTextKey == "10 minutes before")
    }

    @Test("Test reminder time identifiable")
    func testReminderTimeIdentifiable() {
        let fiveMinutes = NotificationReminderTime.fiveMinutes
        #expect(fiveMinutes.id == fiveMinutes.rawValue)
        #expect(fiveMinutes.id == 5)

        let tenMinutes = NotificationReminderTime.tenMinutes
        #expect(tenMinutes.id == tenMinutes.rawValue)
        #expect(tenMinutes.id == 10)
    }
}
