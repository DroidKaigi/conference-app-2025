import SwiftUI
import Model

extension Room {
    public var color: Color {
        switch type {
        case .roomF:
            return Color.blue
        case .roomG:
            return Color.green
        case .roomH:
            return Color.orange
        case .roomI:
            return Color.purple
        case .roomJ:
            return Color.red
        case .roomIJ:
            return Color.indigo
        }
    }
    
    public var displayName: String {
        Locale.current.identifier == "ja-JP"
            ? name.jaTitle
            : name.enTitle
    }
    
    // For backward compatibility with old code
    public var rawValue: String {
        displayName
    }
}


extension Speaker {
    // For backward compatibility
    public var imageUrl: String? {
        iconUrl
    }
}

extension TimetableLanguage {
    // For backward compatibility - returns the primary language label
    public var displayLanguage: String {
        langOfSpeaker
    }
}
