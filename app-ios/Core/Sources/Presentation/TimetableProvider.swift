import Dependencies
import UseCase
import Observation
import Model
import Foundation
import SwiftUI

// Enums for UI state
public enum DayTab: String, CaseIterable, Identifiable {
    public var id: RawValue { rawValue }
    
    case day1 = "Day 1"
    case day2 = "Day 2"
}

public enum TimetableMode {
    case list
    case grid
}

@Observable
public final class TimetableProvider {
    @ObservationIgnored
    @Dependency(\.timetableUseCase) private var timeteableUseCase

    public var timetable: Timetable?
    
    // UI State
    public var favoriteIds: Set<String> = []
    public var dayTimetable: [DroidKaigi2024Day: [TimetableTimeGroupItems]] = [:]

    public init() {}

    @MainActor
    public func fetchTimetable() async {
        do {
            timetable = try await timeteableUseCase.load()
            for day in DroidKaigi2024Day.allCases {
                dayTimetable[day] = sortListIntoTimeGroups(
                    timetableItems: timetable?.dayTimetable(droidKaigi2024Day: day).contents ?? []
                )
            }
        } catch {
            print(error)
        }
    }

    public func toggleFavorite(_ item: TimetableItemWithFavorite) {
        if item.isFavorited {
            favoriteIds.remove(item.timetableItem.id.value)
        } else {
            favoriteIds.insert(item.timetableItem.id.value)
        }
    }
    
    public func isFavorite(_ itemId: String) -> Bool {
        favoriteIds.contains(itemId)
    }

    private func sortListIntoTimeGroups(timetableItems: [TimetableItemWithFavorite]) -> [TimetableTimeGroupItems] {
        let sortedItems: [(Date, Date, TimetableItemWithFavorite)] = timetableItems.map {
            ($0.timetableItem.startsAt, $0.timetableItem.endsAt, $0)
        }

        let myDict = sortedItems.reduce(into: [Date: TimetableTimeGroupItems]()) {
            if $0[$1.0] == nil {
                $0[$1.0] = TimetableTimeGroupItems(
                    startsTimeString:$1.0.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute()),
                    endsTimeString:$1.1.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute()),
                    items:[]
                )
            }
            $0[$1.0]?.items.append($1.2)
        }

        return myDict.values.sorted {
            $0.items[0].timetableItem.startsAt.timeIntervalSince1970 < $1.items[0].timetableItem.startsAt.timeIntervalSince1970
        }
    }
}
