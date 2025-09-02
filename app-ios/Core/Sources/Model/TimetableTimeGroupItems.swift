import Foundation

public struct TimetableTimeGroupItems: Identifiable, Equatable, Sendable {
    public let id: String
    public let startsTimeString: String
    public let endsTimeString: String
    public var items: [TimetableItemWithFavorite]

    public init(
        id: String = UUID().uuidString,
        startsTimeString: String,
        endsTimeString: String,
        items: [TimetableItemWithFavorite]
    ) {
        self.id = id
        self.startsTimeString = startsTimeString
        self.endsTimeString = endsTimeString
        self.items = items
    }
}
