import Foundation

public struct Speaker: Identifiable, Equatable, Sendable {
    public let id: String
    public let name: String
    public let iconUrl: String
    public let bio: String
    public let tagLine: String

    public init(id: String, name: String, iconUrl: String, bio: String, tagLine: String) {
        self.id = id
        self.name = name
        self.iconUrl = iconUrl
        self.bio = bio
        self.tagLine = tagLine
    }
}
