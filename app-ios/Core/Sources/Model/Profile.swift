import Foundation

public enum ProfileCardVariant: String, Sendable, CaseIterable {
    case nightPill
    case dayPill
    case nightDiamond
    case dayDiamond
    case nightFlower
    case dayFlower
}

public struct Profile: Sendable, Equatable {
    public let name: String
    public let occupation: String
    public let url: URL
    public let image: Data
    public let cardVariant: ProfileCardVariant

    public init(
        name: String,
        occupation: String,
        url: URL,
        image: Data,
        cardVariant: ProfileCardVariant
    ) {
        self.name = name
        self.occupation = occupation
        self.url = url
        self.image = image
        self.cardVariant = cardVariant
    }
}
