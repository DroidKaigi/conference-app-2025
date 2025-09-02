import Foundation

extension Model.MultiLangText {
    public func getByLang(lang: Model.Lang) -> String {
        switch lang {
        case .japanese:
            return jaTitle
        case .english, .mixed:
            return enTitle
        }
    }

    public var currentLangTitle: String {
        let isJapanese = Locale.current.language.languageCode == .japanese
        return isJapanese ? jaTitle : enTitle
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
        case .roomJ:
            return "roomJ"
        case .roomK:
            return "roomK"
        case .roomL:
            return "roomL"
        case .roomM:
            return "roomM"
        case .roomN:
            return "roomN"
        }
    }
}

extension Model.DroidKaigi2025Day {
    public var start: Date {
        switch self {
        case .workday:
            return DateComponents(calendar: .current, year: 2025, month: 9, day: 10).date ?? Date()
        case .conferenceDay1:
            return DateComponents(calendar: .current, year: 2025, month: 9, day: 11).date ?? Date()
        case .conferenceDay2:
            return DateComponents(calendar: .current, year: 2025, month: 9, day: 12).date ?? Date()
        }
    }

    public var end: Date {
        switch self {
        case .workday:
            return DateComponents(calendar: .current, year: 2025, month: 9, day: 10, hour: 23, minute: 59, second: 59)
                .date ?? Date()
        case .conferenceDay1:
            return DateComponents(calendar: .current, year: 2025, month: 9, day: 11, hour: 23, minute: 59, second: 59)
                .date ?? Date()
        case .conferenceDay2:
            return DateComponents(calendar: .current, year: 2025, month: 9, day: 12, hour: 23, minute: 59, second: 59)
                .date ?? Date()
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

    public static func initialSelectedTabDay(clock: Date = Date()) -> Model.DroidKaigi2025Day {
        let visibleDays = Self.visibleDays()

        for day in visibleDays {
            if clock >= day.start && clock <= day.end {
                return day
            }
        }

        return visibleDays.first ?? .conferenceDay1
    }

    public static func ofOrNull(time: Date) -> Model.DroidKaigi2025Day? {
        for day in Model.DroidKaigi2025Day.allCases {
            if time >= day.start && time <= day.end {
                return day
            }
        }
        return nil
    }

    public static func visibleDays() -> [Model.DroidKaigi2025Day] {
        [.conferenceDay1, .conferenceDay2]
    }
}

// MARK: - TimetableItem Extension

extension Model.TimetableItem {
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
        "https://2025.droidkaigi.jp/timetable/\(id.value)"
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

    public func dayTimetable(droidKaigi2025Day: Model.DroidKaigi2025Day) -> Model.Timetable {
        let filteredItems = timetableItems.filter { $0.day == droidKaigi2025Day }
        return Model.Timetable(timetableItems: filteredItems, bookmarks: bookmarks)
    }
}

extension Model.Filters {
    public func isEmpty() -> Bool {
        days.isEmpty && categories.isEmpty && sessionTypes.isEmpty && languages.isEmpty && !filterFavorite
            && searchWord.isEmpty
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
            return 0xFF42_85F4
        case .japanese:
            return 0xFFE9_1E63
        case .english:
            return 0xFF00_BCD4
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
