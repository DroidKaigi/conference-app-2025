import Component
import Model
import SwiftUI
import Theme

public struct TimetableDetailScreen: View {
    @State private var presenter: TimetableDetailPresenter
    @State private var showingURL: URL?
    @Environment(\.dismiss) private var dismiss

    public init(timetableItem: TimetableItemWithFavorite) {
        self._presenter = State(initialValue: TimetableDetailPresenter(timetableItem: timetableItem))
    }

    public var body: some View {
        GeometryReader { proxy in
            ZStack {
                ScrollView {
                    VStack(spacing: 0) {
                        presenter.timetableItem.timetableItem.room.roomTheme.containerColor
                            .frame(height: proxy.safeAreaInsets.top)
                        headLine
                            .padding(.bottom, 24)
                    }

                    if let session = presenter.timetableItem.timetableItem as? TimetableItemSession,
                        let message = session.message
                    {
                        SessionCancellationBanner(message: message.currentLangTitle)
                            .padding(.horizontal, 16)
                            .padding(.bottom, 20)
                    }
                    detail
                        .padding(.horizontal, 16)
                    targetAudience
                        .padding(16)
                }

                VStack {
                    Spacer()
                    HStack {
                        Spacer()
                        Button {
                            presenter.toggleFavorite()
                        } label: {
                            Group {
                                if presenter.isFavorite {
                                    Image(systemName: "heart.fill")
                                } else {
                                    Image(systemName: "heart")
                                }
                            }
                            .foregroundStyle(AssetColors.onTertiaryContainer.swiftUIColor)
                            .accessibilityLabel(presenter.isFavorite ? "Remove from favorites" : "Add to favorites")
                            .frame(width: 56, height: 56)
                            .background(AssetColors.tertiaryContainer.swiftUIColor)
                            .clipShape(RoundedRectangle(cornerRadius: 16))
                        }
                    }
                    .padding(.horizontal, 16)
                }
            }
            .padding(.bottom, 80)  // Tab bar padding
            .background(AssetColors.background.swiftUIColor)
            .frame(maxWidth: .infinity)
            .ignoresSafeArea(edges: [.top])
        }
        .tint(presenter.timetableItem.timetableItem.room.roomTheme.primaryColor)
        .sheet(
            isPresented: .init(
                get: { showingURL != nil },
                set: { if !$0 { showingURL = nil } }
            )
        ) {
            if let url = showingURL {
                SafariView(url: url)
                    .ignoresSafeArea()
            }
        }
        .overlay(alignment: .bottom) {
            if presenter.showToast {
                ToastView(
                    message: presenter.toastMessage,
                    actionMessage: "一覧を見る",
                    action: presenter.navigateToFavorite
                )
                .transition(.move(edge: .bottom).combined(with: .opacity))
                .onAppear {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        withAnimation {
                            presenter.showToast = false
                        }
                    }
                }
            }
        }
        .animation(.easeInOut, value: presenter.showToast)
    }

    var headLine: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 4) {
                RoomTag(room: presenter.timetableItem.timetableItem.room)

                LanguageTag(language: presenter.timetableItem.timetableItem.language)
            }
            .padding(.bottom, 8)

            Text(presenter.timetableItem.timetableItem.title.currentLangTitle)
                .font(Typography.headlineSmall)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .padding(.bottom, 20)
                .frame(maxWidth: .infinity, alignment: .leading)

            ForEach(presenter.timetableItem.timetableItem.speakers, id: \.id) { speaker in
                HStack(spacing: 12) {
                    CircularUserIcon(imageUrl: speaker.imageUrl)
                        .frame(width: 52, height: 52)

                    VStack(alignment: .leading, spacing: 8) {
                        Text(speaker.name)
                            .font(Typography.bodyLarge)
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        Text(speaker.tagLine)
                            .font(Typography.bodySmall)
                            .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                    }

                    Spacer()
                }
            }
            .padding(.bottom, 20)
        }
        .padding([.top, .horizontal], 16)
        .background(presenter.timetableItem.timetableItem.room.roomTheme.containerColor)
    }

    var detail: some View {
        VStack(alignment: .leading, spacing: 20) {
            VStack(spacing: 16) {
                InformationRow(
                    icon: Image("ic_schedule", bundle: .module),
                    title: "日時",
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: formattedDateTimeString,
                    isStrikethrough: presenter.isCancelledSession
                )
                InformationRow(
                    icon: Image("ic_location_on", bundle: .module),
                    title: "場所",
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: presenter.timetableItem.timetableItem.room.displayNameWithFloor,
                    isStrikethrough: presenter.isCancelledSession
                )
                InformationRow(
                    icon: Image("ic_language", bundle: .module),
                    title: "対応言語",
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: getSupportedLangString(),
                    isStrikethrough: presenter.isCancelledSession
                )
                InformationRow(
                    icon: Image("ic_category", bundle: .module),
                    title: "カテゴリ",
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: presenter.timetableItem.timetableItem.category.title.currentLangTitle,
                    isStrikethrough: presenter.isCancelledSession
                )
            }
            .padding(16)
            .background(
                presenter.timetableItem.timetableItem.room.roomTheme.containerColor,
                in: RoundedRectangle(cornerRadius: 8))

            if let session = presenter.timetableItem.timetableItem as? TimetableItemSession {
                SessionDescriptionView(
                    content: session.description.currentLangTitle,
                    primaryColor: session.room.roomTheme.primaryColor,
                    containerColor: session.room.roomTheme.containerColor
                )
                .padding(.bottom, 24)
            }
        }
    }

    var targetAudience: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("対象者")
                .font(Typography.titleLarge)
                .foregroundStyle(presenter.timetableItem.timetableItem.room.roomTheme.primaryColor)

            Text(presenter.timetableItem.timetableItem.targetAudience)
                .font(Typography.bodyLarge)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var formattedDateTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy.MM.dd"
        let date = presenter.timetableItem.timetableItem.startsAt
        let dateString = formatter.string(from: date)

        formatter.dateFormat = "HH:mm"
        let startTime = formatter.string(from: date)
        let endDate = presenter.timetableItem.timetableItem.endsAt
        let endTime = formatter.string(from: endDate)

        return "\(dateString) / \(startTime) ~ \(endTime)"
    }

    private func getSupportedLangString() -> String {
        _ = Locale.current.language.languageCode == .japanese
        return presenter.timetableItem.timetableItem.language.langOfSpeaker
    }
}

struct ToastView: View {
    let message: String
    let actionMessage: String
    let action: () -> Void

    var body: some View {
        HStack {
            Text(message)
                .font(Typography.bodyMedium)
                .foregroundStyle(AssetColors.inverseOnSurface.swiftUIColor)
            Spacer()
            Button {
                action()
            } label: {
                Text(actionMessage)
                    .font(Typography.labelLarge)
                    .foregroundStyle(AssetColors.inversePrimary.swiftUIColor)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 14)
        .padding(.horizontal, 16)
        .background(AssetColors.inverseSurface.swiftUIColor, in: RoundedRectangle(cornerRadius: 4))
        .padding(.bottom, 100)  // Tab bar padding
    }
}

#Preview {
    TimetableDetailScreen(
        timetableItem: TimetableItemWithFavorite(
            timetableItem: TimetableItemSession(
                id: TimetableItemId(value: "1"),
                title: MultiLangText(jaTitle: "サンプルセッション", enTitle: "Sample Session"),
                startsAt: Date(),
                endsAt: Date().addingTimeInterval(3_600),
                category: TimetableCategory(id: 1, title: MultiLangText(jaTitle: "開発", enTitle: "Development")),
                sessionType: .regular,
                room: Room(
                    id: 1,
                    name: MultiLangText(jaTitle: "Arctic Fox", enTitle: "Arctic Fox"),
                    type: .roomF,
                    sort: 1
                ),
                targetAudience: "初心者向け",
                language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: false),
                asset: TimetableAsset(videoUrl: nil, slideUrl: nil),
                levels: ["初級"],
                speakers: [],
                description: MultiLangText(jaTitle: "セッションの説明", enTitle: "Session description"),
                message: MultiLangText(jaTitle: "このセッションは中止になりました", enTitle: "This session has been cancelled")
            ),
            isFavorited: false
        )
    )
}
