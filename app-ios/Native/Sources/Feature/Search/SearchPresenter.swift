import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class SearchPresenter {
    let timetable = TimetableProvider()

    var searchWord: String = ""
    var selectedDay: DroidKaigi2025Day?
    var selectedCategory: TimetableCategory?
    var selectedSessionType: TimetableSessionType?
    var selectedLanguage: TimetableLanguage?

    var filteredTimetableItems: [TimetableItemWithFavorite] {
        let allItems = timetable.dayTimetable.values
            .flatMap { $0 }
            .flatMap { $0.items }

        return allItems.filter { item in
            // Search word filter
            if !searchWord.isEmpty {
                let searchLower = searchWord.lowercased()
                let matchesTitle =
                    item.timetableItem.title.jaTitle.lowercased().contains(searchLower)
                    || item.timetableItem.title.enTitle.lowercased().contains(searchLower)
                let matchesDescription =
                    if let session = item.timetableItem as? TimetableItemSession {
                        session.description.jaTitle.lowercased().contains(searchLower)
                            || session.description.enTitle.lowercased().contains(searchLower)
                    } else {
                        false
                    }

                let matchesSpeaker = item.timetableItem.speakers.contains { speaker in
                    speaker.name.lowercased().contains(searchLower)
                }

                if !matchesTitle && !matchesDescription && !matchesSpeaker {
                    return false
                }
            }

            // Day filter
            if let day = selectedDay {
                if item.timetableItem.day != day {
                    return false
                }
            }

            // Category filter
            if let category = selectedCategory {
                if item.timetableItem.category != category {
                    return false
                }
            }

            // Session type filter
            if let sessionType = selectedSessionType {
                if item.timetableItem.sessionType != sessionType {
                    return false
                }
            }

            // Language filter
            if let language = selectedLanguage {
                let itemLang = item.timetableItem.language.langOfSpeaker.uppercased()
                let selectedLang = language.langOfSpeaker.uppercased()

                // Handle different language code formats
                let normalizedItemLang = normalizeLanguageCode(itemLang)
                let normalizedSelectedLang = normalizeLanguageCode(selectedLang)

                if normalizedItemLang != normalizedSelectedLang {
                    return false
                }
            }

            return true
        }
    }

    init() {}

    func loadInitial() {
        timetable.subscribeTimetableIfNeeded()
    }

    func toggleFavorite(_ itemId: TimetableItemId) {
        if let item = filteredTimetableItems.first(where: { $0.timetableItem.id == itemId }) {
            timetable.toggleFavorite(item)
        }
    }

    func timetableItemTapped(_ item: TimetableItemWithFavorite) {
        // print("Search item tapped: \(item.timetableItem.title)")
    }

    private func normalizeLanguageCode(_ langCode: String) -> String {
        let upperCode = langCode.uppercased()

        switch upperCode {
        case "JA", "JAPANESE", "JP":
            return "JA"
        case "EN", "ENGLISH", "ENG":
            return "EN"
        case "MIXED":
            return "MIXED"
        default:
            // Handle codes like "ENGLISH" or "JAPANESE" from actual data
            if upperCode.hasPrefix("JA") || upperCode.contains("JAPANESE") {
                return "JA"
            } else if upperCode.hasPrefix("EN") || upperCode.contains("ENGLISH") {
                return "EN"
            } else if upperCode.contains("MIXED") {
                return "MIXED"
            }
            return upperCode
        }
    }
}
