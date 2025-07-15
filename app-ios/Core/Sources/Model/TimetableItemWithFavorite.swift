import Foundation

public struct TimetableItemWithFavorite: Identifiable, Equatable, Sendable {
    public let timetableItem: TimetableItem
    public var isFavorited: Bool

    public var id: String { timetableItem.id }

    public init(timetableItem: TimetableItem, isFavorited: Bool = false) {
        self.timetableItem = timetableItem
        self.isFavorited = isFavorited
    }
}
