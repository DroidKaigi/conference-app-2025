import Foundation

public struct MultiLangText: Equatable, Hashable, Sendable {
    public let jaTitle: String
    public let enTitle: String
    
    public init(jaTitle: String, enTitle: String) {
        self.jaTitle = jaTitle
        self.enTitle = enTitle
    }
}