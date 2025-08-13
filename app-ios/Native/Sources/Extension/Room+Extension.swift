import Model
import SwiftUI
import Theme

public struct RoomTheme {
    public let primaryColor: Color
    public let containerColor: Color
}

extension Room {
    public var color: Color {
        switch type {
        case .roomF:
            return AssetColors.flamingo.swiftUIColor
        case .roomG:
            return AssetColors.giraffe.swiftUIColor
        case .roomH:
            return AssetColors.koala.swiftUIColor
        case .roomI:
            return AssetColors.jellyfish.swiftUIColor
        case .roomJ:
            return Color.red  // TODO: Add narwhal color
        case .roomIJ:
            return AssetColors.jellyfish.swiftUIColor
        }
    }

    public var roomTheme: RoomTheme {
        RoomTheme(
            primaryColor: color,
            containerColor: color.opacity(0.1)
        )
    }

    public var displayName: String {
        Locale.current.identifier == "ja-JP"
            ? name.jaTitle
            : name.enTitle
    }

    public var displayNameWithFloor: String {
        "\(displayName) (\(floorInfo))"
    }

    public var floorInfo: String {
        switch type {
        case .roomF, .roomG:
            return "1F"
        case .roomH, .roomI, .roomJ, .roomIJ:
            return "B1F"
        }
    }

    public var iconName: String {
        switch type {
        case .roomF:
            return "ic_rhombus"
        case .roomG:
            return "ic_circle"
        case .roomH:
            return "ic_diamond"
        case .roomI:
            return "ic_square"
        case .roomJ:
            return "ic_triangle"
        case .roomIJ:
            return "ic_square"
        }
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
