import Foundation

public struct Timetable: Sendable {
    public let timetableItems: [any TimetableItem]
    public let bookmarks: Set<TimetableItemId>
    
    public init(timetableItems: [any TimetableItem], bookmarks: Set<TimetableItemId>) {
        self.timetableItems = timetableItems
        self.bookmarks = bookmarks
    }
}

public struct Filters: Equatable, Sendable {
    public let days: [DroidKaigi2024Day]
    public let categories: [TimetableCategory]
    public let sessionTypes: [TimetableSessionType]
    public let languages: [Lang]
    public let filterFavorite: Bool
    public let searchWord: String
    
    public init(
        days: [DroidKaigi2024Day] = [],
        categories: [TimetableCategory] = [],
        sessionTypes: [TimetableSessionType] = [],
        languages: [Lang] = [],
        filterFavorite: Bool = false,
        searchWord: String = ""
    ) {
        self.days = days
        self.categories = categories
        self.sessionTypes = sessionTypes
        self.languages = languages
        self.filterFavorite = filterFavorite
        self.searchWord = searchWord
    }
}

