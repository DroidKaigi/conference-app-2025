import Extension
import Model
import SwiftUI
import Theme

public struct RoomTag: View {
    let room: Room
    let isOn: Bool

    public init(room: Room, isOn: Bool = true) {
        self.room = room
        self.isOn = isOn
    }

    public var body: some View {
        HStack(spacing: 4) {
            Image(room.iconName, bundle: .module)
                .renderingMode(.template)
            Text(room.displayName)
                .font(Typography.labelMedium)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 2)
        .background(backgroundColor, in: RoundedRectangle(cornerRadius: 6))
        .foregroundStyle(surfaceColor)
        .overlay {
            RoundedRectangle(cornerRadius: 6)
                .stroke(room.roomTheme.primaryColor, lineWidth: 1)
        }
    }

    private var surfaceColor: Color {
        return isOn ? AssetColors.surface.swiftUIColor : room.roomTheme.primaryColor
    }

    private var backgroundColor: Color {
        return isOn ? room.roomTheme.primaryColor : room.roomTheme.containerColor
    }
}

#Preview {
    RoomTag(
        room: Room(id: 0, name: .init(jaTitle: "JELLYFISH", enTitle: "JELLYFISH"), type: .roomJ, sort: 0), isOn: true)
    RoomTag(
        room: Room(id: 0, name: .init(jaTitle: "JELLYFISH", enTitle: "JELLYFISH"), type: .roomJ, sort: 0), isOn: false)
}
