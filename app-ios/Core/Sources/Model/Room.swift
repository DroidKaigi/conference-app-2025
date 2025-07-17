import Foundation

public struct Room: Identifiable, Equatable, Hashable, Sendable, Comparable {
    public let id: Int32
    public let name: MultiLangText
    public let type: RoomType
    public let sort: Int32
    
    public init(id: Int32, name: MultiLangText, type: RoomType, sort: Int32) {
        self.id = id
        self.name = name
        self.type = type
        self.sort = sort
    }
    
    public static func < (lhs: Room, rhs: Room) -> Bool {
        lhs.sort < rhs.sort
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(id)
        hasher.combine(name.jaTitle)
        hasher.combine(type)
        hasher.combine(sort)
        _ = hasher.finalize()
    }
}
