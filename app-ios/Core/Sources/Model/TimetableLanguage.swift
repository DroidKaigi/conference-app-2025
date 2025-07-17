import Foundation

public struct TimetableLanguage: Equatable, Hashable, Sendable {
    public let langOfSpeaker: String
    public let isInterpretationTarget: Bool
    
    public init(langOfSpeaker: String, isInterpretationTarget: Bool) {
        self.langOfSpeaker = langOfSpeaker
        self.isInterpretationTarget = isInterpretationTarget
    }
}