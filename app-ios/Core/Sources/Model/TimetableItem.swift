import Foundation

public protocol TimetableItem: Identifiable, Equatable, Sendable {
    var id: TimetableItemId { get }
    var title: MultiLangText { get }
    var startsAt: Date { get }
    var endsAt: Date { get }
    var category: TimetableCategory { get }
    var sessionType: TimetableSessionType { get }
    var room: Room { get }
    var targetAudience: String { get }
    var language: TimetableLanguage { get }
    var asset: TimetableAsset { get }
    var levels: [String] { get }
    var speakers: [Speaker] { get }
}

public struct TimetableItemSession: TimetableItem {
    public let id: TimetableItemId
    public let title: MultiLangText
    public let startsAt: Date
    public let endsAt: Date
    public let category: TimetableCategory
    public let sessionType: TimetableSessionType
    public let room: Room
    public let targetAudience: String
    public let language: TimetableLanguage
    public let asset: TimetableAsset
    public let levels: [String]
    public let speakers: [Speaker]
    public let description: MultiLangText
    public let message: MultiLangText?
    
    public init(
        id: TimetableItemId,
        title: MultiLangText,
        startsAt: Date,
        endsAt: Date,
        category: TimetableCategory,
        sessionType: TimetableSessionType,
        room: Room,
        targetAudience: String,
        language: TimetableLanguage,
        asset: TimetableAsset,
        levels: [String],
        speakers: [Speaker],
        description: MultiLangText,
        message: MultiLangText? = nil
    ) {
        self.id = id
        self.title = title
        self.startsAt = startsAt
        self.endsAt = endsAt
        self.category = category
        self.sessionType = sessionType
        self.room = room
        self.targetAudience = targetAudience
        self.language = language
        self.asset = asset
        self.levels = levels
        self.speakers = speakers
        self.description = description
        self.message = message
    }
}

public struct TimetableItemSpecial: TimetableItem {
    public let id: TimetableItemId
    public let title: MultiLangText
    public let startsAt: Date
    public let endsAt: Date
    public let category: TimetableCategory
    public let sessionType: TimetableSessionType
    public let room: Room
    public let targetAudience: String
    public let language: TimetableLanguage
    public let asset: TimetableAsset
    public let levels: [String]
    public let speakers: [Speaker]
    public let description: MultiLangText
    public let message: MultiLangText?
    
    public init(
        id: TimetableItemId,
        title: MultiLangText,
        startsAt: Date,
        endsAt: Date,
        category: TimetableCategory,
        sessionType: TimetableSessionType,
        room: Room,
        targetAudience: String,
        language: TimetableLanguage,
        asset: TimetableAsset,
        levels: [String],
        speakers: [Speaker],
        description: MultiLangText,
        message: MultiLangText? = nil
    ) {
        self.id = id
        self.title = title
        self.startsAt = startsAt
        self.endsAt = endsAt
        self.category = category
        self.sessionType = sessionType
        self.room = room
        self.targetAudience = targetAudience
        self.language = language
        self.asset = asset
        self.levels = levels
        self.speakers = speakers
        self.description = description
        self.message = message
    }
}