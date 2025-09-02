import Model
import SwiftUI
import Theme

public struct RoomTheme {
    public let primaryColor: Color
    public let containerColor: Color
}

extension Room {
    public var color: Color {
        // NOTE: The color names in Figma do not match the room names.
        switch type {
        case .roomJ:
            // JELLYFISH
            return AssetColors.jellyfish.swiftUIColor
        case .roomK:
            // KOALA
            return AssetColors.koala.swiftUIColor
        case .roomL:
            // LADYBUG
            return AssetColors.ladybug.swiftUIColor
        case .roomM:
            // MEERKAT
            return AssetColors.meerkat.swiftUIColor
        case .roomN:
            // NARWHAL
            return AssetColors.narwhal.swiftUIColor
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
        case .roomJ, .roomK:
            return "1F"
        case .roomL, .roomM, .roomN:
            return "B1F"
        }
    }

    public var iconName: String {
        switch type {
        case .roomJ:
            return "ic_square"
        case .roomK:
            return "ic_diamond"
        case .roomL:
            return "ic_circle"
        case .roomM:
            return "ic_rhombus"
        case .roomN:
            return "ic_triangle"
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
