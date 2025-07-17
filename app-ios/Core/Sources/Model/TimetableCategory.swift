import Foundation

public struct TimetableCategory: Identifiable, Equatable, Hashable, Sendable {
    public let id: Int32
    public let title: MultiLangText
    
    public init(id: Int32, title: MultiLangText) {
        self.id = id
        self.title = title
    }
}