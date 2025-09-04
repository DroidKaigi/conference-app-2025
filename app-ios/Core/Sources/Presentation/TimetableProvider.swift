import Dependencies
import Foundation
import Model
import Observation
import UseCase

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
    @Dependency(\.timetableUseCase) private var timetableUseCase

    @ObservationIgnored
    private var fetchTask: Task<Void, Never>?

    @ObservationIgnored
    public var notificationProvider: NotificationProvider?

    public var timetable: Timetable?
    public var dayTimetable: [DroidKaigi2025Day: [TimetableTimeGroupItems]] = [:]

    // Extract unique rooms from timetable items
    public var rooms: [Room] {
        var uniqueRooms: [Room] = []
        var seenRoomIds: Set<Int32> = []

        for item in timetable?.timetableItems ?? [] {
            let room = item.room
            if !seenRoomIds.contains(room.id) {
                seenRoomIds.insert(room.id)
                uniqueRooms.append(room)
            }
        }

        // Sort rooms by their sort order
        return uniqueRooms.sorted()
    }

    public init() {}

    @MainActor
    public func setNotificationProvider(_ provider: NotificationProvider) {
        self.notificationProvider = provider
    }

    @MainActor
    public func subscribeTimetableIfNeeded() {
        guard fetchTask == nil else {
            return
        }

        self.fetchTask = Task {
            for await timetable in timetableUseCase.load() {
                self.timetable = timetable

                for day in DroidKaigi2025Day.allCases {
                    dayTimetable[day] = sortListIntoTimeGroups(
                        timetableItems: timetable.dayTimetable(droidKaigi2025Day: day).contents
                    )
                }

                // Update notifications when timetable changes
                let allItemsWithFavoriteStatus = timetable.timetableItems.compactMap {
                    item -> TimetableItemWithFavorite? in
                    TimetableItemWithFavorite(timetableItem: item, isFavorited: timetable.bookmarks.contains(item.id))
                }
                notificationProvider?.updateNotifications(for: allItemsWithFavoriteStatus)
            }
        }
    }

    @MainActor
    public func toggleFavorite(_ item: TimetableItemWithFavorite) {
        Task {
            await timetableUseCase.toggleFavorite(itemId: item.id)

            // Notify the notification provider about the favorite change
            // The updated favorite status will be reflected in the next timetable update
            notificationProvider?.handleFavoriteChange(item)
        }
    }

    private func sortListIntoTimeGroups(timetableItems: [TimetableItemWithFavorite]) -> [TimetableTimeGroupItems] {
        let sortedItems: [(Date, Date, TimetableItemWithFavorite)] = timetableItems.map {
            ($0.timetableItem.startsAt, $0.timetableItem.endsAt, $0)
        }

        let myDict = sortedItems.reduce(into: [Date: TimetableTimeGroupItems]()) {
            if $0[$1.0] == nil {
                $0[$1.0] = TimetableTimeGroupItems(
                    startsTimeString: $1.0.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute()),
                    endsTimeString: $1.1.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute()),
                    items: []
                )
            }
            $0[$1.0]?.items.append($1.2)
        }

        return myDict.values.sorted {
            $0.items[0].timetableItem.startsAt.timeIntervalSince1970
                < $1.items[0].timetableItem.startsAt.timeIntervalSince1970
        }
    }
}
