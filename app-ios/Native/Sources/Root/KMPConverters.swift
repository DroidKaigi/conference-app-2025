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
        case .roomJ:
            self = .roomJ
        case .roomK:
            self = .roomK
        case .roomL:
            self = .roomL
        case .roomM:
            self = .roomM
        case .roomN:
            self = .roomN
        }
    }
}

// MARK: - Room Converters

extension Model.Room {
    init(from shared: shared.Room) {
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

// MARK: - DroidKaigi2025Day Converters

extension Model.DroidKaigi2025Day {
    init?(from shared: shared.DroidKaigi2025Day) {
        switch shared {
        case .workday:
            self = .workday
        case .conferenceDay1:
            self = .conferenceDay1
        case .conferenceDay2:
            self = .conferenceDay2
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
            message: shared.message.map { Model.MultiLangText(from: $0) },
            day: shared.day.flatMap { Model.DroidKaigi2025Day(from: $0) }
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
            message: shared.message.map { Model.MultiLangText(from: $0) },
            day: shared.day.flatMap { Model.DroidKaigi2025Day(from: $0) }
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
            // Fallback - create a placeholder special item for unknown types
            timetableItem = Model.TimetableItemSpecial(
                id: Model.TimetableItemId(value: "unknown-\(UUID().uuidString)"),
                title: Model.MultiLangText(jaTitle: "不明なアイテム", enTitle: "Unknown Item"),
                startsAt: Date(),
                endsAt: Date().addingTimeInterval(3600),
                category: Model.TimetableCategory(
                    id: 0,
                    title: Model.MultiLangText(jaTitle: "その他", enTitle: "Other")
                ),
                sessionType: .other,
                room: Model.Room(
                    id: 0,
                    name: Model.MultiLangText(jaTitle: "未定", enTitle: "TBD"),
                    type: .roomJ,
                    sort: 999
                ),
                targetAudience: "All",
                language: Model.TimetableLanguage(langOfSpeaker: "EN", isInterpretationTarget: false),
                asset: Model.TimetableAsset(videoUrl: nil, slideUrl: nil),
                levels: [],
                speakers: [],
                description: Model.MultiLangText(jaTitle: "詳細不明", enTitle: "Details unknown"),
                message: nil,
                day: .conferenceDay1
            )
        }

        self.init(
            timetableItem: timetableItem,
            isFavorited: shared.isFavorited
        )
    }
}

// MARK: - Timetable Converters

extension Model.Timetable {
    public init(from shared: shared.Timetable) {
        let timetableItems: [any Model.TimetableItem] = shared.timetableItems.map { item in
            if let session = item as? shared.TimetableItem.Session {
                return Model.TimetableItemSession(from: session)
            } else if let special = item as? shared.TimetableItem.Special {
                return Model.TimetableItemSpecial(from: special)
            } else {
                // Fallback - create a placeholder special item for unknown types
                return Model.TimetableItemSpecial(
                    id: Model.TimetableItemId(value: "unknown-\(UUID().uuidString)"),
                    title: Model.MultiLangText(jaTitle: "不明なアイテム", enTitle: "Unknown Item"),
                    startsAt: Date(),
                    endsAt: Date().addingTimeInterval(3600),
                    category: Model.TimetableCategory(
                        id: 0,
                        title: Model.MultiLangText(jaTitle: "その他", enTitle: "Other")
                    ),
                    sessionType: .other,
                    room: Model.Room(
                        id: 0,
                        name: Model.MultiLangText(jaTitle: "未定", enTitle: "TBD"),
                        type: .roomJ,
                        sort: 999
                    ),
                    targetAudience: "All",
                    language: Model.TimetableLanguage(langOfSpeaker: "EN", isInterpretationTarget: false),
                    asset: Model.TimetableAsset(videoUrl: nil, slideUrl: nil),
                    levels: [],
                    speakers: [],
                    description: Model.MultiLangText(jaTitle: "詳細不明", enTitle: "Details unknown"),
                    message: nil,
                    day: .conferenceDay1
                )
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
            days: shared.days.compactMap { Model.DroidKaigi2025Day(from: $0) },
            categories: shared.categories.map { Model.TimetableCategory(from: $0) },
            sessionTypes: shared.sessionTypes.compactMap { Model.TimetableSessionType(from: $0) },
            languages: shared.languages.map { Model.Lang(from: $0) },
            filterFavorite: shared.filterFavorite,
            searchWord: shared.searchWord
        )
    }
}

// MARK: - Sponsor Converters

extension Model.Sponsor {
    init(from shared: shared.Sponsor) {
        // Use FileManager URL as a safe fallback
        let fallbackURL = URL(fileURLWithPath: "/")
        let logoURL = URL(string: shared.logo) ?? fallbackURL
        let websiteURL = URL(string: shared.link) ?? fallbackURL

        self.init(
            id: shared.id,
            name: shared.name,
            logoUrl: logoURL,
            websiteUrl: websiteURL,
            plan: Model.SponsorPlan(from: shared.plan)
        )
    }
}

extension Model.SponsorPlan {
    init(from shared: shared.SponsorPlan) {
        switch shared {
        case .platinum:
            self = .platinum
        case .gold:
            self = .gold
        case .silver:
            self = .silver
        case .bronze:
            self = .bronze
        case .supporter:
            self = .supporter
        }
    }
}

// MARK: - Staff Converters

extension Model.Staff {
    init(from shared: shared.Staff) {
        // Use FileManager URL as a safe fallback
        let fallbackURL = URL(fileURLWithPath: "/")
        let iconURL = URL(string: shared.iconUrl) ?? fallbackURL

        self.init(
            id: String(shared.id),
            name: shared.username,
            iconUrl: iconURL,
            profileUrl: URL(string: shared.profileUrl),
            role: nil  // KMP Staff doesn't have role field
        )
    }
}

// MARK: - Contributor Converters

extension Model.Contributor {
    init(from shared: shared.Contributor) {
        // Use FileManager URL as a safe fallback
        let fallbackURL = URL(fileURLWithPath: "/")
        let profileURL = shared.profileUrl.flatMap { URL(string: $0) }
        let iconURL = URL(string: shared.iconUrl) ?? fallbackURL

        self.init(
            id: String(shared.id),
            name: shared.username,
            url: profileURL ?? fallbackURL,
            iconUrl: iconURL
        )
    }
}

// MARK: - EventMapEvent Converters

extension Model.EventMapEvent {
    init(from shared: shared.EventMapEvent) {
        self.init(
            name: .init(from: shared.name),
            description: .init(from: shared.description_),
            room: .init(from: shared.room),
            moreDetailUrl: shared.moreDetailsUrl.flatMap { URL(string: $0) },
            message: shared.message.map { .init(from: $0) }
        )
    }
}

// MARK: - Helper Extensions

extension shared.KotlinInstant {
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
