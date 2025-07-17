import Foundation
import Model
import shared

// MARK: - Lang Converters

extension Model.Lang {
    init(from shared: shared.Lang) {
        switch shared {
        case .mixed:
            self = .mixed
        case .japanese:
            self = .japanese
        case .english:
            self = .english
        }
    }
}

extension shared.Lang {
    init(from swift: Model.Lang) {
        switch swift {
        case .mixed:
            self = .mixed
        case .japanese:
            self = .japanese
        case .english:
            self = .english
        }
    }
}

// MARK: - MultiLangText Converters

extension Model.MultiLangText {
    init(from shared: shared.MultiLangText) {
        self.init(
            jaTitle: shared.jaTitle,
            enTitle: shared.enTitle
        )
    }
}

// MARK: - RoomType Converters

extension Model.RoomType {
    init(from shared: shared.RoomType) {
        switch shared {
        case .roomF:
            self = .roomF
        case .roomG:
            self = .roomG
        case .roomH:
            self = .roomH
        case .roomI:
            self = .roomI
        case .roomJ:
            self = .roomJ
        case .roomIj:
            self = .roomIJ
        }
    }
}

// MARK: - Room Converters

extension Model.Room {
    init(from shared: shared.TimetableRoom) {
        self.init(
            id: shared.id,
            name: Model.MultiLangText(from: shared.name),
            type: Model.RoomType(from: shared.type),
            sort: shared.sort
        )
    }
}

// MARK: - Speaker Converters

extension Model.Speaker {
    init(from shared: shared.TimetableSpeaker) {
        self.init(
            id: shared.id,
            name: shared.name,
            iconUrl: shared.iconUrl,
            bio: shared.bio,
            tagLine: shared.tagLine
        )
    }
}

// MARK: - TimetableItemId Converters

extension Model.TimetableItemId {
    init(from shared: shared.TimetableItemId) {
        self.init(value: shared.value)
    }
}

// MARK: - TimetableAsset Converters

extension Model.TimetableAsset {
    init(from shared: shared.TimetableAsset) {
        self.init(
            videoUrl: shared.videoUrl,
            slideUrl: shared.slideUrl
        )
    }
}

// MARK: - TimetableCategory Converters

extension Model.TimetableCategory {
    init(from shared: shared.TimetableCategory) {
        self.init(
            id: shared.id,
            title: Model.MultiLangText(from: shared.title)
        )
    }
}

// MARK: - TimetableLanguage Converters

extension Model.TimetableLanguage {
    init(from shared: shared.TimetableLanguage) {
        self.init(
            langOfSpeaker: shared.langOfSpeaker,
            isInterpretationTarget: shared.isInterpretationTarget
        )
    }
}

// MARK: - TimetableSessionType Converters

extension Model.TimetableSessionType {
    init?(from shared: shared.TimetableSessionType) {
        // Assuming shared.name is a non-optional String property
        self.init(rawValue: shared.name)
    }
}

// MARK: - DroidKaigi2024Day Converters

extension Model.DroidKaigi2024Day {
    init?(from shared: shared.DroidKaigi2024Day) {
        switch shared {
        case .workday:
            self = .workday
        case .conferenceDay1:
            self = .conferenceDay1
        case .conferenceDay2:
            self = .conferenceDay2
        default:
            return nil
        }
    }
}

// MARK: - TimetableItem Converters

extension Model.TimetableItemSession {
    init(from shared: shared.TimetableItem.Session) {
        self.init(
            id: Model.TimetableItemId(from: shared.id),
            title: Model.MultiLangText(from: shared.title),
            startsAt: Date(timeIntervalSince1970: TimeInterval(shared.startsAt.epochSeconds)),
            endsAt: Date(timeIntervalSince1970: TimeInterval(shared.endsAt.epochSeconds)),
            category: Model.TimetableCategory(from: shared.category),
            sessionType: Model.TimetableSessionType(from: shared.sessionType) ?? .regular,
            room: Model.Room(from: shared.room),
            targetAudience: shared.targetAudience,
            language: Model.TimetableLanguage(from: shared.language),
            asset: Model.TimetableAsset(from: shared.asset),
            levels: shared.levels,
            speakers: shared.speakers.map { Model.Speaker(from: $0) },
            description: Model.MultiLangText(from: shared.description_),
            message: shared.message.map { Model.MultiLangText(from: $0) }
        )
    }
}

extension Model.TimetableItemSpecial {
    init(from shared: shared.TimetableItem.Special) {
        self.init(
            id: Model.TimetableItemId(from: shared.id),
            title: Model.MultiLangText(from: shared.title),
            startsAt: Date(timeIntervalSince1970: TimeInterval(shared.startsAt.epochSeconds)),
            endsAt: Date(timeIntervalSince1970: TimeInterval(shared.endsAt.epochSeconds)),
            category: Model.TimetableCategory(from: shared.category),
            sessionType: Model.TimetableSessionType(from: shared.sessionType) ?? .other,
            room: Model.Room(from: shared.room),
            targetAudience: shared.targetAudience,
            language: Model.TimetableLanguage(from: shared.language),
            asset: Model.TimetableAsset(from: shared.asset),
            levels: shared.levels,
            speakers: shared.speakers.map { Model.Speaker(from: $0) },
            description: Model.MultiLangText(from: shared.description_),
            message: shared.message.map { Model.MultiLangText(from: $0) }
        )
    }
}

// MARK: - TimetableItemWithFavorite Converters

extension Model.TimetableItemWithFavorite {
    init(from shared: shared.TimetableItemWithFavorite) {
        let timetableItem: any Model.TimetableItem
        if let session = shared.timetableItem as? shared.TimetableItem.Session {
            timetableItem = Model.TimetableItemSession(from: session)
        } else if let special = shared.timetableItem as? shared.TimetableItem.Special {
            timetableItem = Model.TimetableItemSpecial(from: special)
        } else {
            // Fallback - should not happen if KMP model is correct
            fatalError("Unknown TimetableItem type")
        }
        
        self.init(
            timetableItem: timetableItem,
            isFavorited: shared.isFavorited
        )
    }
}

// MARK: - Timetable Converters

extension Model.Timetable {
    init(from shared: shared.Timetable) {
        let timetableItems: [any Model.TimetableItem] = shared.timetableItems.map { item in
            if let session = item as? shared.TimetableItem.Session {
                return Model.TimetableItemSession(from: session)
            } else if let special = item as? shared.TimetableItem.Special {
                return Model.TimetableItemSpecial(from: special)
            } else {
                fatalError("Unknown TimetableItem type")
            }
        }
        
        let bookmarks = Set(shared.bookmarks.map { Model.TimetableItemId(from: $0) })
        
        self.init(
            timetableItems: timetableItems,
            bookmarks: bookmarks
        )
    }
}

// MARK: - Filters Converters

extension Model.Filters {
    init(from shared: shared.Filters) {
        self.init(
            days: shared.days.compactMap { Model.DroidKaigi2024Day(from: $0) },
            categories: shared.categories.map { Model.TimetableCategory(from: $0) },
            sessionTypes: shared.sessionTypes.compactMap { Model.TimetableSessionType(from: $0) },
            languages: shared.languages.map { Model.Lang(from: $0) },
            filterFavorite: shared.filterFavorite,
            searchWord: shared.searchWord
        )
    }
}

// MARK: - Helper Extensions

extension shared.Kotlinx_datetimeInstant {
    var date: Date {
        Date(timeIntervalSince1970: TimeInterval(epochSeconds))
    }
}

// MARK: - Utility Functions

public func defaultLang() -> Model.Lang {
    let locale = Locale.current
    let languageCode = locale.language.languageCode?.identifier ?? ""
    
    if languageCode == "ja" {
        return .japanese
    } else {
        return .english
    }
}

extension Model.MultiLangText {
    public var currentLangTitle: String {
        getByLang(lang: defaultLang())
    }
    
    public func getByLang(lang: Model.Lang) -> String {
        switch lang {
        case .japanese:
            return jaTitle
        case .english, .mixed:
            return enTitle
        }
    }
}

extension Model.TimetableLanguage {
    public var labels: [String] {
        var result = [langOfSpeaker]
        if isInterpretationTarget {
            result.append("EN")
        }
        return result
    }
    
    public func toLang() -> Model.Lang {
        switch langOfSpeaker {
        case "JA":
            return .japanese
        case "EN":
            return .english
        default:
            return .mixed
        }
    }
}

extension Model.TimetableAsset {
    public var isAvailable: Bool {
        videoUrl != nil || slideUrl != nil
    }
}

extension Model.Room {
    public func getThemeKey() -> String {
        switch type {
        case .roomF:
            return "roomF"
        case .roomG:
            return "roomG"
        case .roomH:
            return "roomH"
        case .roomI:
            return "roomI"
        case .roomJ:
            return "roomJ"
        case .roomIJ:
            return "roomIJ"
        }
    }
}

extension Model.DroidKaigi2024Day {
    public var start: Date {
        switch self {
        case .workday:
            return DateComponents(calendar: .current, year: 2024, month: 9, day: 11).date!
        case .conferenceDay1:
            return DateComponents(calendar: .current, year: 2024, month: 9, day: 12).date!
        case .conferenceDay2:
            return DateComponents(calendar: .current, year: 2024, month: 9, day: 13).date!
        }
    }
    
    public var end: Date {
        switch self {
        case .workday:
            return DateComponents(calendar: .current, year: 2024, month: 9, day: 11, hour: 23, minute: 59, second: 59).date!
        case .conferenceDay1:
            return DateComponents(calendar: .current, year: 2024, month: 9, day: 12, hour: 23, minute: 59, second: 59).date!
        case .conferenceDay2:
            return DateComponents(calendar: .current, year: 2024, month: 9, day: 13, hour: 23, minute: 59, second: 59).date!
        }
    }
    
    public var dayOfMonth: Int32 {
        Int32(Calendar.current.component(.day, from: start))
    }
    
    public func monthAndDay() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "M/d"
        return formatter.string(from: start)
    }
    
    public func tabIndex() -> Int32 {
        switch self {
        case .workday:
            return 0
        case .conferenceDay1:
            return 1
        case .conferenceDay2:
            return 2
        }
    }
    
    public static func initialSelectedTabDay(clock: Date = Date()) -> Model.DroidKaigi2024Day {
        let visibleDays = Self.visibleDays()
        
        for day in visibleDays {
            if clock >= day.start && clock <= day.end {
                return day
            }
        }
        
        return visibleDays.first ?? .conferenceDay1
    }
    
    public static func ofOrNull(time: Date) -> Model.DroidKaigi2024Day? {
        for day in Model.DroidKaigi2024Day.allCases {
            if time >= day.start && time <= day.end {
                return day
            }
        }
        return nil
    }
    
    public static func visibleDays() -> [Model.DroidKaigi2024Day] {
        return [.conferenceDay1, .conferenceDay2]
    }
}

// MARK: - TimetableItem Extension

extension Model.TimetableItem {
    public var day: Model.DroidKaigi2024Day? {
        Model.DroidKaigi2024Day.ofOrNull(time: startsAt)
    }
    
    public var startsLocalTime: Date {
        startsAt
    }
    
    public var endsLocalTime: Date {
        endsAt
    }
    
    public var startsTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: startsAt)
    }
    
    public var endsTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: endsAt)
    }
    
    public var formattedTimeString: String {
        "\(startsTimeString) - \(endsTimeString)"
    }
    
    public var formattedDateTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "M/d HH:mm"
        let start = formatter.string(from: startsAt)
        let end = formatter.string(from: endsAt)
        return "\(start) - \(end)"
    }
    
    public var formattedMonthAndDayString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "M/d"
        return formatter.string(from: startsAt)
    }
    
    public var url: String {
        "https://2024.droidkaigi.jp/timetable/\(id.value)"
    }
    
    public func getSupportedLangString(isJapaneseLocale: Bool) -> String {
        let labels = language.labels
        if labels.count > 1 {
            return isJapaneseLocale ? labels[0] : labels[1]
        }
        return labels.first ?? ""
    }
}

// MARK: - Timetable Extensions

extension Model.Timetable {
    public var contents: [Model.TimetableItemWithFavorite] {
        timetableItems.map { item in
            Model.TimetableItemWithFavorite(
                timetableItem: item,
                isFavorited: bookmarks.contains(item.id)
            )
        }
    }
    
    public var days: [Model.DroidKaigi2024Day] {
        Array(Set(timetableItems.compactMap { $0.day })).sorted { $0.tabIndex() < $1.tabIndex() }
    }
    
    public var rooms: [Model.Room] {
        Array(Set(timetableItems.map { $0.room })).sorted()
    }
    
    public var categories: [Model.TimetableCategory] {
        Array(Set(timetableItems.map { $0.category })).sorted { $0.id < $1.id }
    }
    
    public var sessionTypes: [Model.TimetableSessionType] {
        Array(Set(timetableItems.map { $0.sessionType }))
    }
    
    public var languages: [Model.TimetableLanguage] {
        Array(Set(timetableItems.map { $0.language }))
    }
    
    public func dayTimetable(droidKaigi2024Day: Model.DroidKaigi2024Day) -> Model.Timetable {
        let filteredItems = timetableItems.filter { $0.day == droidKaigi2024Day }
        return Model.Timetable(timetableItems: filteredItems, bookmarks: bookmarks)
    }
    
    public func filtered(filters: Model.Filters) -> Model.Timetable {
        var filteredItems = timetableItems
        
        if !filters.days.isEmpty {
            filteredItems = filteredItems.filter { item in
                guard let day = item.day else { return false }
                return filters.days.contains(day)
            }
        }
        
        if !filters.categories.isEmpty {
            filteredItems = filteredItems.filter { item in
                filters.categories.contains { $0.id == item.category.id }
            }
        }
        
        if !filters.sessionTypes.isEmpty {
            filteredItems = filteredItems.filter { item in
                filters.sessionTypes.contains(item.sessionType)
            }
        }
        
        if !filters.languages.isEmpty {
            filteredItems = filteredItems.filter { item in
                filters.languages.contains(item.language.toLang())
            }
        }
        
        if filters.filterFavorite {
            filteredItems = filteredItems.filter { item in
                bookmarks.contains(item.id)
            }
        }
        
        if !filters.searchWord.isEmpty {
            let searchWord = filters.searchWord.lowercased()
            filteredItems = filteredItems.filter { item in
                item.title.currentLangTitle.lowercased().contains(searchWord)
            }
        }
        
        return Model.Timetable(timetableItems: filteredItems, bookmarks: bookmarks)
    }
    
    public func isEmpty() -> Bool {
        timetableItems.isEmpty
    }
    
    public func copy(
        timetableItems: [any Model.TimetableItem]? = nil,
        bookmarks: Set<Model.TimetableItemId>? = nil
    ) -> Model.Timetable {
        Model.Timetable(
            timetableItems: timetableItems ?? self.timetableItems,
            bookmarks: bookmarks ?? self.bookmarks
        )
    }
}

extension Model.Filters {
    public func isEmpty() -> Bool {
        days.isEmpty && 
        categories.isEmpty && 
        sessionTypes.isEmpty && 
        languages.isEmpty && 
        !filterFavorite && 
        searchWord.isEmpty
    }
}


extension Model.Lang {
    public var tagName: String {
        switch self {
        case .mixed:
            return "MIXED"
        case .japanese:
            return "JA"
        case .english:
            return "EN"
        }
    }
    
    public var backgroundColor: Int64 {
        switch self {
        case .mixed:
            return 0xFF4285F4
        case .japanese:
            return 0xFFE91E63
        case .english:
            return 0xFF00BCD4
        }
    }
    
    public func secondLang() -> Model.Lang? {
        switch self {
        case .mixed:
            return nil
        case .japanese:
            return .english
        case .english:
            return .japanese
        }
    }
}
