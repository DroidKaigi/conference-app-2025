import Dependencies
import Model
import Testing
import Foundation
import UseCase
@testable import Presentation

struct TimetableProviderTest {
    @MainActor
    @Test
    func fetchTimetableSuccess() async throws {
        let expectedTimetable = TestData.createSampleTimetable()
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                expectedTimetable
            }
        } operation: {
            TimetableProvider()
        }

        await provider.fetchTimetable()
        
        #expect(provider.timetable != nil)
        #expect(provider.dayTimetable.count == DroidKaigi2024Day.allCases.count)
    }
    
    @MainActor
    @Test
    func fetchTimetableEmptyData() async throws {
        let emptyTimetable = Timetable(timetableItems: [], bookmarks: Set())
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                emptyTimetable
            }
        } operation: {
            TimetableProvider()
        }

        await provider.fetchTimetable()
        
        #expect(provider.timetable != nil)
        #expect(provider.timetable?.timetableItems.count == 0)
        #expect(provider.dayTimetable.count == DroidKaigi2024Day.allCases.count)
        
        for day in DroidKaigi2024Day.allCases {
            let dayItems = provider.dayTimetable[day]
            #expect(dayItems != nil)
            #expect(dayItems?.isEmpty == true)
        }
    }
    
    @Test
    func toggleFavoriteAddItem() async throws {
        let provider = TimetableProvider()
        let item = TestData.createTimetableItemWithFavorite(isFavorited: false)
        
        #expect(provider.favoriteIds.isEmpty)
        
        provider.toggleFavorite(item)
        
        #expect(provider.favoriteIds.count == 1)
        #expect(provider.favoriteIds.contains(item.timetableItem.id.value))
        #expect(provider.isFavorite(item.timetableItem.id.value))
    }
    
    @Test
    func toggleFavoriteRemoveItem() async throws {
        let provider = TimetableProvider()
        let item = TestData.createTimetableItemWithFavorite(isFavorited: true)
        provider.favoriteIds.insert(item.timetableItem.id.value)
        
        #expect(provider.favoriteIds.count == 1)
        
        provider.toggleFavorite(item)
        
        #expect(provider.favoriteIds.isEmpty)
        #expect(provider.isFavorite(item.timetableItem.id.value) == false)
    }
    
    @Test
    func toggleFavoriteMultipleItems() async throws {
        let provider = TimetableProvider()
        let item1 = TestData.createTimetableItemWithFavorite(id: "1", isFavorited: false)
        let item2 = TestData.createTimetableItemWithFavorite(id: "2", isFavorited: false)
        let item3 = TestData.createTimetableItemWithFavorite(id: "3", isFavorited: true)
        
        provider.favoriteIds.insert(item3.timetableItem.id.value)
        
        provider.toggleFavorite(item1)
        provider.toggleFavorite(item2)
        provider.toggleFavorite(item3)
        
        #expect(provider.favoriteIds.count == 2)
        #expect(provider.isFavorite("1"))
        #expect(provider.isFavorite("2"))
        #expect(provider.isFavorite("3") == false)
    }
    
    @Test
    func isFavoriteMethod() async throws {
        let provider = TimetableProvider()
        
        #expect(provider.isFavorite("nonexistent") == false)
        
        provider.favoriteIds.insert("test-id")
        
        #expect(provider.isFavorite("test-id"))
        #expect(provider.isFavorite("another-id") == false)
    }
    
    @MainActor
    @Test
    func dayTimetableGrouping() async throws {
        let timetable = TestData.createTimetableWithMultipleTimeSlots()
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                timetable
            }
        } operation: {
            TimetableProvider()
        }

        await provider.fetchTimetable()
        
        for day in DroidKaigi2024Day.allCases {
            if let groups = provider.dayTimetable[day], !groups.isEmpty {
                for group in groups {
                    let firstItem = group.items.first!
                    for item in group.items {
                        #expect(item.timetableItem.startsAt == firstItem.timetableItem.startsAt)
                    }
                }
                
                #expect(groups.allSatisfy { !$0.items.isEmpty })
                
                #expect(groups.allSatisfy { group in
                    group.startsTimeString == group.items[0].timetableItem.startsAt.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute())
                })
                
                #expect(groups.allSatisfy { group in
                    group.endsTimeString == group.items[0].timetableItem.endsAt.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute())
                })
            }
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
            createTimetableItemSession(id: "8", day: .conferenceDay2, startsAt: createDate(hour: 10, minute: 0), endsAt: createDate(hour: 11, minute: 0), room: .roomG),
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
        endsAt: Date = Date().addingTimeInterval(3600),
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
            message: nil
        )
    }
    
    static func createTimetableItemSpecial(
        id: String = "special-1",
        day: DroidKaigi2024Day = .conferenceDay1,
        startsAt: Date = Date(),
        endsAt: Date = Date().addingTimeInterval(3600),
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
            message: nil
        )
    }
    
    static func createRoom(type: RoomType) -> Room {
        let id: Int32
        switch type {
        case .roomF: id = 1
        case .roomG: id = 2
        case .roomH: id = 3
        case .roomI: id = 4
        case .roomJ: id = 5
        case .roomIJ: id = 6
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
        calendar.timeZone = TimeZone(identifier: "Asia/Tokyo")!
        
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

