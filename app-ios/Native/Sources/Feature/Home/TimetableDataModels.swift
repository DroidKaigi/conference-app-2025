import Foundation
import Model
import SwiftUI

extension RoomType {
    var color: Color {
        return .blue
    }
}

extension MultiLangText {
    var currentLangTitle: String {
        // TODO: Use actual locale logic
        return enTitle
    }
}

extension TimetableItem {
    var startsTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: startsAt)
    }

    var endsTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: endsAt)
    }
}

extension TimetableTimeGroupItems {
    public func getItem(for room: Room) -> TimetableItemWithFavorite? {
        items.first { $0.timetableItem.room.id == room.id }
    }
    
    public func isLunchTime() -> Bool {
        items.count == 1 && items[0].timetableItem.title.currentLangTitle.lowercased().contains("lunch")
    }
}
