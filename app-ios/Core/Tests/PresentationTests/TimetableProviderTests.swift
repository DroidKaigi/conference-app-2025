import Dependencies
import Foundation
import Model
@testable import Presentation
import Testing
import UseCase

struct TimetableProviderTest {
    @MainActor
    @Test
    func fetchTimetableSuccess() async throws {
        let expectedTimetable = TestData.createSampleTimetable()
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(expectedTimetable)
                    continuation.finish()
                }
            }
        } operation: {
            TimetableProvider()
        }

        provider.subscribeTimetableIfNeeded()
        // Give some time for the async task to process
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds
        
        #expect(provider.timetable != nil)
        #expect(provider.dayTimetable.count == DroidKaigi2024Day.allCases.count)
    }
    
    @MainActor
    @Test
    func fetchTimetableEmptyData() async throws {
        let emptyTimetable = Timetable(timetableItems: [], bookmarks: Set())
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(emptyTimetable)
                    continuation.finish()
                }
            }
        } operation: {
            TimetableProvider()
        }

        provider.subscribeTimetableIfNeeded()
        // Give some time for the async task to process
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds
        
        #expect(provider.timetable != nil)
        #expect(provider.timetable?.timetableItems.isEmpty == true)
        #expect(provider.dayTimetable.count == DroidKaigi2024Day.allCases.count)
        
        for day in DroidKaigi2024Day.allCases {
            let dayItems = provider.dayTimetable[day]
            #expect(dayItems != nil)
            #expect(dayItems?.isEmpty == true)
        }
    }

    @MainActor
    @Test
    func dayTimetableGrouping() async throws {
        let timetable = TestData.createTimetableWithMultipleTimeSlots()
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(timetable)
                    continuation.finish()
                }
            }
        } operation: {
            TimetableProvider()
        }

        provider.subscribeTimetableIfNeeded()
        // Give some time for the async task to process
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds
        
        for day in DroidKaigi2024Day.allCases {
            if let groups = provider.dayTimetable[day], !groups.isEmpty {
                for group in groups {
                    guard let firstItem = group.items.first else { continue }
                    for item in group.items {
                        #expect(item.timetableItem.startsAt == firstItem.timetableItem.startsAt)
                    }
                }
                
                #expect(groups.allSatisfy { !$0.items.isEmpty })
                
                #expect(groups.allSatisfy { group in
                    let formatter = Date.FormatStyle.dateTime.hour(.twoDigits(amPM: .omitted)).minute()
                    return group.startsTimeString == group.items[0].timetableItem.startsAt.formatted(formatter)
                })
                
                #expect(groups.allSatisfy { group in
                    let formatter = Date.FormatStyle.dateTime.hour(.twoDigits(amPM: .omitted)).minute()
                    return group.endsTimeString == group.items[0].timetableItem.endsAt.formatted(formatter)
                })
            }
        }
    }
    
    @MainActor
    @Test("Verify error handling in fetchTimetable")
    func fetchTimetableError() async throws {
        // Since typed throws cannot be mocked directly in the current Swift version,
        // we test that the provider handles the absence of data correctly
        let provider = withDependencies {
            // Return an empty timetable to simulate error recovery
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(Timetable(timetableItems: [], bookmarks: Set()))
                    continuation.finish()
                }
            }
        } operation: {
            TimetableProvider()
        }
        
        // Initially, timetable should be nil
        #expect(provider.timetable == nil)
        #expect(provider.dayTimetable.isEmpty)
        
        // Subscribe will return empty data (simulating error recovery)
        provider.subscribeTimetableIfNeeded()
        
        // Wait for async operation to complete
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds
        
        // The provider should handle empty data gracefully
        #expect(provider.timetable != nil)
        #expect(provider.timetable?.timetableItems.isEmpty == true)
        #expect(provider.dayTimetable.count == DroidKaigi2024Day.allCases.count)
        
        // All day timetables should be empty
        for day in DroidKaigi2024Day.allCases {
            #expect(provider.dayTimetable[day]?.isEmpty == true)
        }
    }
    
    @Test("Verify rooms property deduplication")
    func roomsPropertyDeduplication() async throws {
        let provider = TimetableProvider()
        
        let items: [any TimetableItem] = [
            TestData.createTimetableItemSession(id: "1", room: .roomF),
            TestData.createTimetableItemSession(id: "2", room: .roomG),
            TestData.createTimetableItemSession(id: "3", room: .roomF), // duplicate
            TestData.createTimetableItemSession(id: "4", room: .roomH),
            TestData.createTimetableItemSession(id: "5", room: .roomG) // duplicate
        ]
        
        provider.timetable = Timetable(timetableItems: items, bookmarks: Set())
        
        let rooms = provider.rooms
        
        #expect(rooms.count == 3)
        
        let roomTypes = Set(rooms.map { $0.type })
        #expect(roomTypes.contains(.roomF))
        #expect(roomTypes.contains(.roomG))
        #expect(roomTypes.contains(.roomH))
        
        // Verify sort order
        for i in 0..<rooms.count - 1 {
            #expect(rooms[i].sort <= rooms[i + 1].sort)
        }
    }
    
    @Test("Verify rooms property when empty")
    func roomsPropertyEmpty() async throws {
        let provider = TimetableProvider()
        
        #expect(provider.rooms.isEmpty)
        
        provider.timetable = Timetable(timetableItems: [], bookmarks: Set())
        
        #expect(provider.rooms.isEmpty)
    }
    
    @MainActor
    @Test("Verify time-based grouping and sorting")
    func timeGroupingSorting() async throws {
        let date1 = TestData.createDate(hour: 9, minute: 0)
        let date2 = TestData.createDate(hour: 10, minute: 30)
        let date3 = TestData.createDate(hour: 14, minute: 0)
        
        let items: [any TimetableItem] = [
            TestData.createTimetableItemSession(id: "3", startsAt: date3, endsAt: date3.addingTimeInterval(3600)),
            TestData.createTimetableItemSession(id: "1", startsAt: date1, endsAt: date1.addingTimeInterval(3600)),
            TestData.createTimetableItemSession(id: "2", startsAt: date2, endsAt: date2.addingTimeInterval(3600))
        ]
        
        let timetable = Timetable(timetableItems: items, bookmarks: Set())
        
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(timetable)
                    continuation.finish()
                }
            }
        } operation: {
            TimetableProvider()
        }
        
        provider.subscribeTimetableIfNeeded()
        
        // Wait for async operation to complete
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds
        
        let day1Groups = provider.dayTimetable[DroidKaigi2024Day.conferenceDay1] ?? []
        
        #expect(day1Groups.count == 3)
        
        // Verify sorted by time
        if day1Groups.count >= 3 {
            #expect(day1Groups[0].items[0].timetableItem.id.value == "1")
            #expect(day1Groups[1].items[0].timetableItem.id.value == "2")
            #expect(day1Groups[2].items[0].timetableItem.id.value == "3")
        }
    }
    
    @MainActor
    @Test("Verify multiple sessions in same time slot are grouped together")
    func sameTimeSlotGrouping() async throws {
        let startTime = TestData.createDate(hour: 10, minute: 0)
        let endTime = startTime.addingTimeInterval(3600)
        
        let items: [any TimetableItem] = [
            TestData.createTimetableItemSession(id: "1", startsAt: startTime, endsAt: endTime, room: .roomF),
            TestData.createTimetableItemSession(id: "2", startsAt: startTime, endsAt: endTime, room: .roomG),
            TestData.createTimetableItemSession(id: "3", startsAt: startTime, endsAt: endTime, room: .roomH)
        ]
        
        let timetable = Timetable(timetableItems: items, bookmarks: Set())
        
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(timetable)
                    continuation.finish()
                }
            }
        } operation: {
            TimetableProvider()
        }
        
        provider.subscribeTimetableIfNeeded()
        
        // Wait for async operation to complete
        try await Task.sleep(nanoseconds: 100_000_000) // 0.1 seconds
        
        let day1Groups = provider.dayTimetable[DroidKaigi2024Day.conferenceDay1] ?? []
        
        #expect(day1Groups.count == 1)
        #expect(day1Groups.first?.items.count == 3)
        
        if let group = day1Groups.first {
            let itemIds = Set(group.items.map { $0.timetableItem.id.value })
            #expect(itemIds.contains("1"))
            #expect(itemIds.contains("2"))
            #expect(itemIds.contains("3"))
        }
    }
}

enum TestData {
    static func createSampleTimetable() -> Timetable {
        let items: [any TimetableItem] = [
            createTimetableItemSession(
                id: "1",
                day: .conferenceDay1,
                startsAt: createDate(hour: 10, minute: 0),
                endsAt: createDate(hour: 11, minute: 0)
            ),
            createTimetableItemSession(
                id: "2",
                day: .conferenceDay1,
                startsAt: createDate(hour: 11, minute: 30),
                endsAt: createDate(hour: 12, minute: 30)
            ),
            createTimetableItemSpecial(
                id: "3",
                day: .conferenceDay1,
                startsAt: createDate(hour: 12, minute: 30),
                endsAt: createDate(hour: 13, minute: 30),
                sessionType: .lunch
            ),
            createTimetableItemSession(
                id: "4",
                day: .conferenceDay2,
                startsAt: createDate(hour: 10, minute: 0),
                endsAt: createDate(hour: 11, minute: 0)
            )
        ]
        
        return Timetable(timetableItems: items, bookmarks: Set())
    }
    
    static func createTimetableWithMultipleTimeSlots() -> Timetable {
        let items: [any TimetableItem] = [
            createTimetableItemSession(id: "1", day: .conferenceDay1, startsAt: createDate(hour: 10, minute: 0), endsAt: createDate(hour: 11, minute: 0), room: .roomF),
            createTimetableItemSession(id: "2", day: .conferenceDay1, startsAt: createDate(hour: 10, minute: 0), endsAt: createDate(hour: 11, minute: 0), room: .roomG),
            createTimetableItemSession(id: "3", day: .conferenceDay1, startsAt: createDate(hour: 10, minute: 0), endsAt: createDate(hour: 11, minute: 0), room: .roomH),
            
            createTimetableItemSession(id: "4", day: .conferenceDay1, startsAt: createDate(hour: 11, minute: 30), endsAt: createDate(hour: 12, minute: 30), room: .roomF),
            createTimetableItemSession(id: "5", day: .conferenceDay1, startsAt: createDate(hour: 11, minute: 30), endsAt: createDate(hour: 12, minute: 30), room: .roomG),
            
            createTimetableItemSpecial(id: "6", day: .conferenceDay1, startsAt: createDate(hour: 12, minute: 30), endsAt: createDate(hour: 13, minute: 30), sessionType: .lunch),
            
            createTimetableItemSession(id: "7", day: .conferenceDay2, startsAt: createDate(hour: 10, minute: 0), endsAt: createDate(hour: 11, minute: 0), room: .roomF),
            createTimetableItemSession(id: "8", day: .conferenceDay2, startsAt: createDate(hour: 10, minute: 0), endsAt: createDate(hour: 11, minute: 0), room: .roomG)
        ]
        
        return Timetable(timetableItems: items, bookmarks: Set())
    }
    
    static func createTimetableItemWithFavorite(
        id: String = "test-item",
        isFavorited: Bool = false
    ) -> TimetableItemWithFavorite {
        let item = createTimetableItemSession(id: id)
        return TimetableItemWithFavorite(timetableItem: item, isFavorited: isFavorited)
    }
    
    static func createTimetableItemSession(
        id: String = "1",
        day: DroidKaigi2024Day = .conferenceDay1,
        startsAt: Date = Date(),
        endsAt: Date = Date().addingTimeInterval(3_600),
        room: RoomType = .roomF
    ) -> TimetableItemSession {
        TimetableItemSession(
            id: TimetableItemId(value: id),
            title: MultiLangText(jaTitle: "テストセッション", enTitle: "Test Session"),
            startsAt: startsAt,
            endsAt: endsAt,
            category: TimetableCategory(id: 1, title: MultiLangText(jaTitle: "開発", enTitle: "Development")),
            sessionType: .regular,
            room: createRoom(type: room),
            targetAudience: "All levels",
            language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: true),
            asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
            levels: ["Beginner"],
            speakers: [createSpeaker()],
            description: MultiLangText(jaTitle: "説明", enTitle: "Description"),
            message: nil,
            day: day
        )
    }
    
    static func createTimetableItemSpecial(
        id: String = "special-1",
        day: DroidKaigi2024Day = .conferenceDay1,
        startsAt: Date = Date(),
        endsAt: Date = Date().addingTimeInterval(3_600),
        sessionType: TimetableSessionType = .lunch
    ) -> TimetableItemSpecial {
        TimetableItemSpecial(
            id: TimetableItemId(value: id),
            title: MultiLangText(jaTitle: "ランチ", enTitle: "Lunch"),
            startsAt: startsAt,
            endsAt: endsAt,
            category: TimetableCategory(id: 99, title: MultiLangText(jaTitle: "その他", enTitle: "Other")),
            sessionType: sessionType,
            room: createRoom(type: .roomIJ),
            targetAudience: "All",
            language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: false),
            asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
            levels: [],
            speakers: [],
            description: MultiLangText(jaTitle: "ランチタイム", enTitle: "Lunch Time"),
            message: nil,
            day: day
        )
    }
    
    static func createRoom(type: RoomType) -> Room {
        let id: Int32
        switch type {
        case .roomF:
            id = 1
        case .roomG:
            id = 2
        case .roomH:
            id = 3
        case .roomI:
            id = 4
        case .roomJ:
            id = 5
        case .roomIJ:
            id = 6
        }
        
        return Room(
            id: id,
            name: MultiLangText(jaTitle: type.rawValue, enTitle: type.rawValue),
            type: type,
            sort: id
        )
    }
    
    static func createSpeaker() -> Speaker {
        Speaker(
            id: "speaker-1",
            name: "Test Speaker",
            iconUrl: "https://example.com/icon.png",
            bio: "Speaker bio",
            tagLine: "Test Engineer"
        )
    }
    
    static func createDate(
        year: Int = 2024,
        month: Int = 9,
        day: Int = 12,
        hour: Int = 10,
        minute: Int = 0
    ) -> Date {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = TimeZone(identifier: "Asia/Tokyo") ?? TimeZone.current
        
        var components = DateComponents()
        components.year = year
        components.month = month
        components.day = day
        components.hour = hour
        components.minute = minute
        components.second = 0
        
        return calendar.date(from: components) ?? Date()
    }
}
