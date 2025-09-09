import Extension
import Model
import Presentation
import SwiftUI
import Theme

public struct TimetableGridCard: View {
    let timetableItem: any TimetableItem
    let isFavorite: Bool
    let onTap: (any TimetableItem) -> Void

    public init(timetableItem: any TimetableItem, isFavorite: Bool, onTap: @escaping (any TimetableItem) -> Void) {
        self.timetableItem = timetableItem
        self.isFavorite = isFavorite
        self.onTap = onTap
    }

    public var body: some View {
        Button {
            onTap(timetableItem)
        } label: {
            VStack(alignment: .leading, spacing: 4) {
                HStack(spacing: 4) {
                    Image(timetableItem.room.iconName, bundle: .module)
                        .renderingMode(.template)
                        .resizable()
                        .frame(width: 12, height: 12)
                    Text("\(timetableItem.startsTimeString) ~ \(timetableItem.endsTimeString)")
                        .font(Typography.labelLarge)
                        .multilineTextAlignment(.leading)
                }
                .foregroundStyle(contentForegroundColor)

                Text(timetableItem.title.currentLangTitle)
                    .font(Typography.labelLarge)
                    .foregroundStyle(contentForegroundColor)
                    .multilineTextAlignment(.leading)
                    .lineLimit(2)

                Spacer()
                    .frame(maxWidth: .infinity)

                if !timetableItem.speakers.isEmpty {
                    HStack {
                        CircularUserIcon(imageUrl: timetableItem.speakers.first?.iconUrl)
                            .frame(width: 32, height: 32)

                        Text(timetableItem.speakers.map(\.name).joined(separator: ", "))
                            .font(Typography.bodySmall)
                            .foregroundStyle(speakerForegroundColor)
                            .lineLimit(1)

                        Spacer()
                    }
                }
            }
            .padding(8)
            .background(backgroundColor)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(timetableItem.room.color, lineWidth: 1)
            )
            .cornerRadius(8)
        }
        .buttonStyle(PlainButtonStyle())
    }

    private var contentForegroundColor: Color {
        isFavorite ? AssetColors.surface.swiftUIColor : timetableItem.room.color
    }

    private var speakerForegroundColor: Color {
        isFavorite ? AssetColors.surface.swiftUIColor : AssetColors.onSurface.swiftUIColor
    }

    private var backgroundColor: Color {
        timetableItem.room.color.opacity(isFavorite ? 0.7 : 0.1)
    }
}

#Preview {
    TimetableGridCard(
        timetableItem: PreviewData.timetableItemSession,
        isFavorite: false,
        onTap: { _ in }
    )
    .frame(width: 200, height: 100)
}

private enum PreviewData {
    static let timetableItemSession = TimetableItemSession(
        id: TimetableItemId(value: "preview-1"),
        title: MultiLangText(jaTitle: "SwiftUIの最新機能", enTitle: "Latest SwiftUI Features"),
        startsAt: createDate(hour: 10, minute: 0),
        endsAt: createDate(hour: 11, minute: 0),
        category: TimetableCategory(
            id: 1,
            title: MultiLangText(jaTitle: "開発", enTitle: "Development")
        ),
        sessionType: .regular,
        room: Room(
            id: 1,
            name: MultiLangText(jaTitle: "ルームJ", enTitle: "Room J"),
            type: .roomJ,
            sort: 1
        ),
        targetAudience: "All levels",
        language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: true),
        asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
        levels: ["Intermediate"],
        speakers: [
            Speaker(
                id: "speaker-1",
                name: "Preview Speaker",
                iconUrl: "https://example.com/icon.png",
                bio: "Speaker bio",
                tagLine: "iOS Developer"
            )
        ],
        description: MultiLangText(
            jaTitle: "SwiftUIの最新機能について学びます",
            enTitle: "Learn about the latest SwiftUI features"
        ),
        message: nil,
        day: .conferenceDay1
    )

    private static func createDate(hour: Int, minute: Int) -> Date {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = TimeZone(identifier: "Asia/Tokyo") ?? TimeZone.current

        var components = DateComponents()
        components.year = 2025
        components.month = 9
        components.day = 12
        components.hour = hour
        components.minute = minute
        components.second = 0

        return calendar.date(from: components) ?? Date()
    }
}
