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
    public var selectedDay: DayTab = .day1
    public var timetableMode: TimetableMode = .list
    public var favoriteIds: Set<String> = []
    
    // Computed properties
    public var timetableItems: [TimetableTimeGroupItems] {
        guard let timetable = timetable else { return [] }
        
        // TODO: Implement proper grouping logic based on time slots and selected day
        // For now, return empty array until proper implementation
        return []
    }

    public init() {}

    @MainActor
    public func fetchTimetable() async {
        do {
            timetable = try await timeteableUseCase.load()
        } catch {
            print(error)
        }
    }
    
    // UI Actions
    public func selectDay(_ day: DayTab) {
        selectedDay = day
    }
    
    public func toggleViewMode() {
        switch timetableMode {
        case .list:
            timetableMode = .grid
        case .grid:
            timetableMode = .list
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
}
