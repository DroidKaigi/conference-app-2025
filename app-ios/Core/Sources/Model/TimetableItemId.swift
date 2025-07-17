import Foundation

public struct TimetableItemId: Equatable, Hashable, Sendable {
    public let value: String
    
    public init(value: String) {
        self.value = value
    }
}