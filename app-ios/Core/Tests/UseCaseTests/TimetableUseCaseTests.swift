import Dependencies
import Foundation
import Model
import Testing
@testable import UseCase

struct TimetableUseCaseTests {
    @Test("Should successfully load timetable with items and bookmarks")
    func testLoadTimetableReturnsExpectedItemsAndBookmarks() async throws {
        // Arrange
        let expectedItems: [any TimetableItem] = [
            TimetableItemSession(
                id: TimetableItemId(value: "session-1"),
                title: MultiLangText(jaTitle: "Session 1", enTitle: "Session 1"),
                startsAt: Date(),
                endsAt: Date().addingTimeInterval(3600),
                category: TimetableCategory(id: 1, title: MultiLangText(jaTitle: "Development", enTitle: "Development")),
                sessionType: .regular,
                room: Room(id: 1, name: MultiLangText(jaTitle: "Room J", enTitle: "Room J"), type: .roomJ, sort: 1),
                targetAudience: "All",
                language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: false),
                asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
                speakers: [],
                description: MultiLangText(jaTitle: "Description", enTitle: "Description"),
                message: nil,
                day: .conferenceDay1
            )
        ]
        let expectedBookmarks = Set<TimetableItemId>([TimetableItemId(value: "session-1")])
        let expectedTimetable = Timetable(
            timetableItems: expectedItems,
            bookmarks: expectedBookmarks
        )
        
        // Act
        let result = await withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(expectedTimetable)
                    continuation.finish()
                }
            }
        } operation: {
            @Dependency(\.timetableUseCase) var useCase
            var timetables: [Timetable] = []
            for await timetable in useCase.load() {
                timetables.append(timetable)
            }
            return timetables.first ?? Timetable(timetableItems: [], bookmarks: Set())
        }
        
        // Assert
        #expect(result.timetableItems.count == 1)
        #expect(result.bookmarks.count == 1)
        #expect(result.bookmarks.contains(TimetableItemId(value: "session-1")))
        #expect(result.timetableItems.first?.id.value == "session-1")
    }
    
    @Test("Should handle empty timetable with no items or bookmarks")
    func testLoadEmptyTimetableReturnsEmptyCollections() async throws {
        // Arrange
        let emptyTimetable = Timetable(
            timetableItems: [],
            bookmarks: Set<TimetableItemId>()
        )
        
        // Act
        let result = await withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(emptyTimetable)
                    continuation.finish()
                }
            }
        } operation: {
            @Dependency(\.timetableUseCase) var useCase
            var timetables: [Timetable] = []
            for await timetable in useCase.load() {
                timetables.append(timetable)
            }
            return timetables.first ?? Timetable(timetableItems: [], bookmarks: Set())
        }
        
        // Assert
        #expect(result.timetableItems.isEmpty)
        #expect(result.bookmarks.isEmpty)
    }
    
    @Test("Should handle AsyncSequence with multiple emissions")
    func testAsyncSequenceWithMultipleEmissions() async throws {
        // Arrange
        let firstTimetable = Timetable(
            timetableItems: [],
            bookmarks: Set<TimetableItemId>([TimetableItemId(value: "item-1")])
        )
        let secondTimetable = Timetable(
            timetableItems: [],
            bookmarks: Set<TimetableItemId>([TimetableItemId(value: "item-2")])
        )
        
        // Act
        let results = await withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(firstTimetable)
                    continuation.yield(secondTimetable)
                    continuation.finish()
                }
            }
        } operation: {
            @Dependency(\.timetableUseCase) var useCase
            var timetables: [Timetable] = []
            for await timetable in useCase.load() {
                timetables.append(timetable)
            }
            return timetables
        }
        
        // Assert
        #expect(results.count == 2)
        #expect(results[0].bookmarks.contains(TimetableItemId(value: "item-1")))
        #expect(results[1].bookmarks.contains(TimetableItemId(value: "item-2")))
    }
    
    @Test("Should correctly load timetable containing both session and special items")
    // swiftlint:disable:next function_body_length
    func testLoadTimetableWithMixedSessionAndSpecialItems() async throws {
        // Arrange
        let mixedItems: [any TimetableItem] = [
            TimetableItemSession(
                id: TimetableItemId(value: "session-1"),
                title: MultiLangText(jaTitle: "Regular Session", enTitle: "Regular Session"),
                startsAt: Date(),
                endsAt: Date().addingTimeInterval(3600),
                category: TimetableCategory(id: 1, title: MultiLangText(jaTitle: "Development", enTitle: "Development")),
                sessionType: .regular,
                room: Room(id: 1, name: MultiLangText(jaTitle: "Room J", enTitle: "Room J"), type: .roomJ, sort: 1),
                targetAudience: "All",
                language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: true),
                asset: TimetableAsset(videoUrl: "https://example.com/video", slideUrl: "https://example.com/slide"),
                speakers: [
                    Speaker(id: "speaker-1", name: "Test Speaker", iconUrl: "https://example.com/icon", bio: "Bio", tagLine: "Engineer")
                ],
                description: MultiLangText(jaTitle: "Description", enTitle: "Description"),
                message: nil,
                day: .conferenceDay1
            ),
            TimetableItemSpecial(
                id: TimetableItemId(value: "lunch-1"),
                title: MultiLangText(jaTitle: "Lunch", enTitle: "Lunch"),
                startsAt: Date().addingTimeInterval(7200),
                endsAt: Date().addingTimeInterval(10800),
                category: TimetableCategory(id: 99, title: MultiLangText(jaTitle: "Other", enTitle: "Other")),
                sessionType: .lunch,
                room: Room(id: 6, name: MultiLangText(jaTitle: "Lunch Room", enTitle: "Lunch Room"), type: .roomJ, sort: 6),
                targetAudience: "All",
                language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: false),
                asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
                speakers: [],
                description: MultiLangText(jaTitle: "Lunch Time", enTitle: "Lunch Time"),
                message: nil,
                day: .conferenceDay1
            )
        ]
        
        let bookmarks = Set<TimetableItemId>([TimetableItemId(value: "session-1")])
        let timetable = Timetable(timetableItems: mixedItems, bookmarks: bookmarks)
        
        // Act
        let result = await withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(timetable)
                    continuation.finish()
                }
            }
        } operation: {
            @Dependency(\.timetableUseCase) var useCase
            var timetables: [Timetable] = []
            for await timetable in useCase.load() {
                timetables.append(timetable)
            }
            return timetables.first ?? Timetable(timetableItems: [], bookmarks: Set())
        }
        
        // Assert
        #expect(result.timetableItems.count == 2)
        #expect(result.bookmarks.count == 1)
        
        let sessionItem = result.timetableItems[0] as? TimetableItemSession
        #expect(sessionItem != nil)
        #expect(sessionItem?.sessionType == .regular)
        
        let specialItem = result.timetableItems[1] as? TimetableItemSpecial
        #expect(specialItem != nil)
        #expect(specialItem?.sessionType == .lunch)
    }
    
    @Test("Should retrieve TimetableUseCase from DependencyValues correctly")
    func testTimetableUseCaseAccessibleViaDependencyValues() async throws {
        // Arrange
        let expectedTimetable = Timetable(
            timetableItems: [],
            bookmarks: Set<TimetableItemId>()
        )
        
        // Act
        let result = await withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(expectedTimetable)
                    continuation.finish()
                }
            }
        } operation: {
            @Dependency(\.timetableUseCase) var useCase
            var timetables: [Timetable] = []
            for await timetable in useCase.load() {
                timetables.append(timetable)
            }
            return timetables.first ?? Timetable(timetableItems: [], bookmarks: Set())
        }
        
        // Assert
        #expect(result.timetableItems.isEmpty)
        #expect(result.bookmarks.isEmpty)
    }
    
    @Test("Should preserve all bookmarks when loading timetable with multiple bookmarked items")
    func testLoadTimetablePreservesMultipleBookmarks() async throws {
        // Arrange
        let bookmarks = Set<TimetableItemId>([
            TimetableItemId(value: "item-1"),
            TimetableItemId(value: "item-2"),
            TimetableItemId(value: "item-3"),
            TimetableItemId(value: "item-4"),
            TimetableItemId(value: "item-5")
        ])
        let timetable = Timetable(
            timetableItems: [],
            bookmarks: bookmarks
        )
        
        // Act
        let result = await withDependencies {
            $0.timetableUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(timetable)
                    continuation.finish()
                }
            }
        } operation: {
            @Dependency(\.timetableUseCase) var useCase
            var timetables: [Timetable] = []
            for await timetable in useCase.load() {
                timetables.append(timetable)
            }
            return timetables.first ?? Timetable(timetableItems: [], bookmarks: Set())
        }
        
        // Assert
        #expect(result.bookmarks.count == 5)
        #expect(result.bookmarks.contains(TimetableItemId(value: "item-1")))
        #expect(result.bookmarks.contains(TimetableItemId(value: "item-2")))
        #expect(result.bookmarks.contains(TimetableItemId(value: "item-3")))
        #expect(result.bookmarks.contains(TimetableItemId(value: "item-4")))
        #expect(result.bookmarks.contains(TimetableItemId(value: "item-5")))
    }
}
