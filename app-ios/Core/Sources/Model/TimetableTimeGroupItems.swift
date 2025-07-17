import Foundation

public struct TimetableTimeGroupItems: Identifiable, Equatable, Sendable {
    public let id: String
    public let startsTimeString: String
    public let endsTimeString: String
    public var items: [TimetableItemWithFavorite]

    public init(id: String? = nil, startsTimeString: String, endsTimeString: String, items: [TimetableItemWithFavorite]) {
        self.id = id ?? UUID().uuidString
        self.startsTimeString = startsTimeString
        self.endsTimeString = endsTimeString
        self.items = items
    }
    
}
