import Foundation
import Model
import SwiftUI

extension RoomType {
    var color: Color {
        .blue
    }
}

extension MultiLangText {
    var currentLangTitle: String {
        let isJapanese = Locale.current.language.languageCode == .japanese
        return isJapanese ? jaTitle : enTitle
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

    public func isLunchTime() -> Bool {
        title.currentLangTitle.lowercased().contains("lunch")
    }
}

extension TimetableTimeGroupItems {
    public func getItem(for room: Room) -> TimetableItemWithFavorite? {
        items.first { $0.timetableItem.room.id == room.id }
    }
}
