import Extension
import Model
import SwiftUI
import Theme

public struct RoomTag: View {
    let room: Room

    public init(room: Room) {
        self.room = room
    }

    public var body: some View {
        HStack(spacing: 4) {
            Image(room.iconName, bundle: .module)
            Text(room.displayName)
                .font(Typography.labelSmall)
                .foregroundStyle(textColor)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
        .background(backgroundColor, in: RoundedRectangle(cornerRadius: 6))
        .overlay {
            RoundedRectangle(cornerRadius: 6)
                .stroke(room.roomTheme.primaryColor, lineWidth: 1)
        }
    }

    private var textColor: Color {
        if room.type == .roomH {
            return AssetColors.surface.swiftUIColor
        }
        return room.roomTheme.primaryColor
    }

    private var backgroundColor: Color {
        if room.type == .roomH {
            return room.roomTheme.primaryColor
        }
        return room.roomTheme.containerColor
    }
}

public struct LanguageTag: View {
    let language: TimetableLanguage

    public init(language: TimetableLanguage) {
        self.language = language
    }

    public var body: some View {
        Text(language.displayLanguage)
            .font(Typography.labelSmall)
            .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            .padding(.horizontal, 6)
            .padding(.vertical, 3)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(AssetColors.outline.swiftUIColor, lineWidth: 1)
            )
    }
}

public struct CircularUserIcon: View {
    let imageUrl: String?

    public init(imageUrl: String?) {
        self.imageUrl = imageUrl
    }

    public var body: some View {
        if let imageUrl = imageUrl, let url = URL(string: imageUrl) {
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Image(systemName: "person.circle.fill")
                    .foregroundStyle(AssetColors.outline.swiftUIColor)
            }
            .clipShape(Circle())
        } else {
            Image(systemName: "person.circle.fill")
                .resizable()
                .foregroundStyle(AssetColors.outline.swiftUIColor)
        }
    }
}
