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
}
