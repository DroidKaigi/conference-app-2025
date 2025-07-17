import Foundation

public struct TimetableItemWithFavorite: Identifiable, Sendable {
    public let timetableItem: any TimetableItem
    public var isFavorited: Bool

    public var id: TimetableItemId { timetableItem.id }

    public init(timetableItem: any TimetableItem, isFavorited: Bool = false) {
        self.timetableItem = timetableItem
        self.isFavorited = isFavorited
    }
}

extension TimetableItemWithFavorite: Equatable {
    public static func == (lhs: TimetableItemWithFavorite, rhs: TimetableItemWithFavorite) -> Bool {
        lhs.id == rhs.id && lhs.isFavorited == rhs.isFavorited
    }
}
