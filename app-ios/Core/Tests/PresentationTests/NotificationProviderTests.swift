import Dependencies
import Foundation
import Model
@testable import Presentation
import Testing
import UseCase

enum NotificationProviderTestData {
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

@MainActor
struct NotificationProviderTests {
    @Test("Test notification provider initialization")
    func testInitialization() async throws {
        let provider = withDependencies {
            $0.notificationUseCase.load = {
                NotificationSettings(isEnabled: true, reminderTime: .tenMinutes, useCustomSound: false)
            }
            $0.notificationUseCase.checkAuthorizationStatus = {
                NotificationAuthorizationStatus.authorized
            }
        } operation: {
            NotificationProvider()
        }

        // Initially, settings should be default
        #expect(provider.settings.isEnabled == false)
        #expect(provider.authorizationStatus == NotificationAuthorizationStatus.notDetermined)

        provider.initialize()

        // Wait for async initialization
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds

        #expect(provider.settings.isEnabled == true)
        #expect(provider.settings.reminderTime == NotificationReminderTime.tenMinutes)
        #expect(provider.authorizationStatus == NotificationAuthorizationStatus.authorized)
    }

    @Test("Test handle favorite change")
    func testHandleFavoriteChange() async throws {
        let item = NotificationProviderTestData.createTimetableItemWithFavorite(
            id: "test-session",
            isFavorited: true
        )

        let scheduleCalled = LockIsolated<Bool>(false)

        let provider = withDependencies {
            $0.notificationUseCase.load = {
                NotificationSettings(isEnabled: true, reminderTime: .tenMinutes, useCustomSound: false)
            }
            $0.notificationUseCase.checkAuthorizationStatus = {
                NotificationAuthorizationStatus.authorized
            }
            $0.notificationUseCase.scheduleNotification = { _, _ in
                scheduleCalled.setValue(true)
                return true
            }
        } operation: {
            NotificationProvider()
        }

        provider.initialize()
        try await Task.sleep(nanoseconds: 50_000_000) // 0.05 seconds

        provider.handleFavoriteChange(item)
        try await Task.sleep(nanoseconds: 50_000_000) // 0.05 seconds

        #expect(scheduleCalled.value == true)
    }

    @Test("Test update notifications")
    func testUpdateNotifications() async throws {
        let items = [
            NotificationProviderTestData.createTimetableItemWithFavorite(id: "session-1", isFavorited: true),
            NotificationProviderTestData.createTimetableItemWithFavorite(id: "session-2", isFavorited: false),
            NotificationProviderTestData.createTimetableItemWithFavorite(id: "session-3", isFavorited: true)
        ]

        let rescheduleCallCount = LockIsolated<Int>(0)

        let provider = withDependencies {
            $0.notificationUseCase.load = {
                NotificationSettings(isEnabled: true, reminderTime: .tenMinutes, useCustomSound: false)
            }
            $0.notificationUseCase.checkAuthorizationStatus = {
                NotificationAuthorizationStatus.authorized
            }
            $0.notificationUseCase.rescheduleAllNotifications = { _, _ in
                rescheduleCallCount.setValue(rescheduleCallCount.value + 1)
            }
        } operation: {
            NotificationProvider()
        }

        provider.initialize()
        try await Task.sleep(nanoseconds: 50_000_000) // 0.05 seconds

        provider.updateNotifications(for: items)
        try await Task.sleep(nanoseconds: 50_000_000) // 0.05 seconds

        #expect(rescheduleCallCount.value == 1)
    }

    @Test("Test refresh settings")
    func testRefreshSettings() async throws {
        let loadCallCount = LockIsolated<Int>(0)

        let provider = withDependencies {
            $0.notificationUseCase.load = {
                let count = loadCallCount.value + 1
                loadCallCount.setValue(count)
                return NotificationSettings(
                    isEnabled: count > 1, // Change after first call
                    reminderTime: count == 1 ? .fiveMinutes : .tenMinutes,
                    useCustomSound: false
                )
            }
            $0.notificationUseCase.checkAuthorizationStatus = {
                NotificationAuthorizationStatus.notDetermined
            }
        } operation: {
            NotificationProvider()
        }

        // Initial state
        #expect(provider.settings.isEnabled == false)
        #expect(provider.authorizationStatus == NotificationAuthorizationStatus.notDetermined)

        // First initialization
        provider.initialize()
        try await Task.sleep(nanoseconds: 50_000_000) // 0.05 seconds

        #expect(loadCallCount.value == 1)
        #expect(provider.settings.isEnabled == false)
        #expect(provider.settings.reminderTime == .fiveMinutes)

        // Refresh settings
        provider.refreshSettings()
        try await Task.sleep(nanoseconds: 50_000_000) // 0.05 seconds

        #expect(loadCallCount.value == 2)
        #expect(provider.settings.isEnabled == true)
        #expect(provider.settings.reminderTime == .tenMinutes)
    }
}
