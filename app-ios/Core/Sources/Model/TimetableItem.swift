import Foundation

public struct TimetableItem: Identifiable, Equatable, Sendable {
    public let id: String
    public let title: String
    public let startsAt: Date
    public let endsAt: Date
    public let room: Room
    public let speakers: [Speaker]
    public let description: String
    public let language: String

    public init(
        id: String? = nil,
        title: String,
        startsAt: Date,
        endsAt: Date,
        room: Room,
        speakers: [Speaker] = [],
        description: String = "",
        language: String = "EN"
    ) {
        self.id = id ?? UUID().uuidString
        self.title = title
        self.startsAt = startsAt
        self.endsAt = endsAt
        self.room = room
        self.speakers = speakers
        self.description = description
        self.language = language
    }
}
