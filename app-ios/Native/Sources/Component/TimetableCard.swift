import Model
import SwiftUI
import Theme

public struct TimetableCard: View {
    let timetableItem: any TimetableItem
    let isFavorite: Bool
    let onTap: (any TimetableItem) -> Void
    let onTapFavorite: (any TimetableItem, CGPoint?) -> Void

    @State private var dragLocation: CGPoint?

    public init(
        timetableItem: any TimetableItem,
        isFavorite: Bool,
        onTap: @escaping (any TimetableItem) -> Void,
        onTapFavorite: @escaping (any TimetableItem, CGPoint?) -> Void
    ) {
        self.timetableItem = timetableItem
        self.isFavorite = isFavorite
        self.onTap = onTap
        self.onTapFavorite = onTapFavorite
    }

    public var body: some View {
        Button {
            onTap(timetableItem)
        } label: {
            HStack(alignment: .top, spacing: 0) {
                VStack(alignment: .leading, spacing: 8) {
                    headerRow

                    VStack(alignment: .leading, spacing: 6) {
                        Text(timetableItem.title.currentLangTitle)
                            .font(Typography.titleLarge)
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                            .multilineTextAlignment(.leading)
                            .fixedSize(horizontal: false, vertical: true)

                        if !timetableItem.speakers.isEmpty {
                            speakersList
                        }
                    }
                }
                favoriteButton
            }
            .padding(.leading, 16)
            .padding(.top, 16)
            .padding(.bottom, 16)
            .padding(.trailing, 0)
            .frame(maxWidth: .infinity, alignment: .leading)
            .overlay(
                RoundedRectangle(cornerRadius: 24)
                    .stroke(AssetColors.outlineVariant.swiftUIColor, lineWidth: 1)
            )
            .cornerRadius(24)
        }
        .buttonStyle(PlainButtonStyle())
    }

    private var headerRow: some View {
        HStack(spacing: 4) {
            RoomTag(room: timetableItem.room)
            LanguageTag(language: timetableItem.language)
            Spacer()
        }
    }

    private var favoriteButton: some View {
        Button {
            let location = dragLocation
            onTapFavorite(timetableItem, location)
        } label: {
            (isFavorite ? AssetImages.icFavFill.swiftUIImage : AssetImages.icFav.swiftUIImage)
                .resizable()
                .foregroundStyle(timetableItem.room.color)
                .frame(width: 24, height: 24)
                .padding(.leading, 16)
                .padding(.trailing, 12)
                .padding(.bottom, 16)
                .accessibilityLabel(isFavorite ? "Remove from favorites" : "Add to favorites")
        }
        .buttonStyle(PlainButtonStyle())
        .background(
            GeometryReader { _ in
                Color.clear
                    .onContinuousHover { phase in
                        switch phase {
                        case .active(let location):
                            dragLocation = location
                        case .ended:
                            break
                        }
                    }
            }
        )
    }

    private var speakersList: some View {
        VStack(alignment: .leading, spacing: 4) {
            ForEach(timetableItem.speakers, id: \.id) { speaker in
                HStack(spacing: 8) {
                    CircularUserIcon(imageUrl: speaker.iconUrl)
                        .frame(width: 32, height: 32)

                    Text(speaker.name)
                        .font(Typography.titleSmall)
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                }
            }
        }
    }
}

#Preview {
    TimetableCard(
        timetableItem: TimetableItemSession(
            id: TimetableItemId(value: "1"),
            title: MultiLangText(jaTitle: "テストセッション", enTitle: "Test Session"),
            startsAt: .now,
            endsAt: .init(timeIntervalSinceNow: 10000),
            category: TimetableCategory(id: 1, title: MultiLangText(jaTitle: "開発", enTitle: "Development")),
            sessionType: .regular,
            room: .init(id: 0, name: .init(jaTitle: "JELLYFISH", enTitle: "JELLYFISH"), type: .roomJ, sort: 1),
            targetAudience: "All levels",
            language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: true),
            asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
            levels: ["Beginner"],
            speakers: [
                Speaker(
                    id: "speaker-1",
                    name: "Test Speaker",
                    iconUrl: "https://example.com/icon.png",
                    bio: "Speaker bio",
                    tagLine: "Test Engineer"
                )
            ],
            description: MultiLangText(jaTitle: "説明", enTitle: "Description"),
            message: nil,
            day: .conferenceDay1
        ),
        isFavorite: false,
        onTap: { _ in },
        onTapFavorite: { _, _ in }
    )
    .padding()
    .background(AssetColors.surface.swiftUIColor)
}
