import Foundation

public struct Speaker: Identifiable, Equatable, Sendable {
    public let id: String
    public let name: String
    public let imageUrl: String?

    public init(id: String? = nil, name: String, imageUrl: String? = nil) {
        self.id = id ?? UUID().uuidString
        self.name = name
        self.imageUrl = imageUrl
    }
}
