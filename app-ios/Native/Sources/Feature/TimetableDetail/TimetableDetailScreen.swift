import Component
import Model
import SwiftUI
import Theme

public struct TimetableDetailScreen: View {
    @State private var presenter: TimetableDetailPresenter
    @State private var showingURL: URL?
    @State private var isShowFabMenu = false
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

                fabMenu
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
            if let toast = presenter.toast {
                ToastView(
                    message: toast.message,
                    action: toast.action
                )
                .transition(.move(edge: .bottom).combined(with: .opacity))
                .onAppear {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        withAnimation {
                            presenter.toast = nil
                        }
                    }
                }
            }
        }
        .animation(.easeInOut, value: presenter.toast != nil)
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
                    icon: AssetImages.icSchedule.swiftUIImage,
                    title: String(localized: "DateTime", bundle: .module),
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: formattedDateTimeString,
                    isStrikethrough: presenter.isCancelledSession
                )
                InformationRow(
                    icon: AssetImages.icLocationOn.swiftUIImage,
                    title: String(localized: "Location", bundle: .module),
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: presenter.timetableItem.timetableItem.room.displayNameWithFloor,
                    isStrikethrough: presenter.isCancelledSession
                )
                InformationRow(
                    icon: AssetImages.icLanguage.swiftUIImage,
                    title: String(localized: "Supported Languages", bundle: .module),
                    titleColor: presenter.timetableItem.timetableItem.room.roomTheme.primaryColor,
                    content: getSupportedLangString(),
                    isStrikethrough: presenter.isCancelledSession
                )
                InformationRow(
                    icon: AssetImages.icCategory.swiftUIImage,
                    title: String(localized: "Category", bundle: .module),
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
            Text(String(localized: "Target Audience", bundle: .module))
                .font(Typography.titleLarge)
                .foregroundStyle(presenter.timetableItem.timetableItem.room.roomTheme.primaryColor)

            Text(presenter.timetableItem.timetableItem.targetAudience)
                .font(Typography.bodyLarge)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    var fabMenu: some View {
        VStack {
            Spacer()
            HStack {
                Spacer()
                VStack(alignment: .trailing, spacing: 4) {
                    if isShowFabMenu {
                        Button {
                            presenter.toggleFavorite()
                            isShowFabMenu = false
                        } label: {
                            HStack(spacing: 8) {
                                if presenter.isFavorite {
                                    AssetImages.icFavFill.swiftUIImage
                                } else {
                                    AssetImages.icFav.swiftUIImage
                                }

                                Text(
                                    presenter.isFavorite
                                        ? String(localized: "Remove bookmark", bundle: .module)
                                        : String(localized: "Add to bookmark", bundle: .module)
                                )
                                .font(Typography.titleMedium)
                            }
                            .foregroundStyle(AssetColors.tertiaryContainer.swiftUIColor)
                            .padding(.horizontal, 24)
                            .padding(.vertical, 16)
                            .background(
                                AssetColors.onTertiaryContainer.swiftUIColor, in: RoundedRectangle(cornerRadius: 28))
                        }

                        Button {
                            presenter.addToCalendar()
                            isShowFabMenu = false
                        } label: {
                            HStack(spacing: 8) {
                                AssetImages.icTimetable.swiftUIImage

                                Text(String(localized: "Add to calendar", bundle: .module))
                                    .font(Typography.titleMedium)
                            }
                            .foregroundStyle(AssetColors.tertiaryContainer.swiftUIColor)
                            .padding(.horizontal, 24)
                            .padding(.vertical, 16)
                            .background(
                                AssetColors.onTertiaryContainer.swiftUIColor, in: RoundedRectangle(cornerRadius: 28))
                        }

                        if let shareLink = presenter.shareSession() {
                            ShareLink(item: shareLink) {
                                HStack(spacing: 8) {
                                    AssetImages.icShare.swiftUIImage

                                    Text(String(localized: "Share link", bundle: .module))
                                        .font(Typography.titleMedium)
                                }
                                .foregroundStyle(AssetColors.tertiaryContainer.swiftUIColor)
                                .padding(.horizontal, 24)
                                .padding(.vertical, 16)
                                .background(
                                    AssetColors.onTertiaryContainer.swiftUIColor, in: RoundedRectangle(cornerRadius: 28)
                                )
                            }
                        }

                        if let slideUrlString = presenter.timetableItem.timetableItem.asset.slideUrl,
                            let slideUrl = URL(string: slideUrlString)
                        {
                            Button {
                                showingURL = slideUrl
                                isShowFabMenu = false
                            } label: {
                                HStack(spacing: 8) {
                                    AssetImages.icDescription.swiftUIImage

                                    Text(String(localized: "Open presentation slides", bundle: .module))
                                        .font(Typography.titleMedium)
                                }
                                .foregroundStyle(AssetColors.tertiaryContainer.swiftUIColor)
                                .padding(.horizontal, 24)
                                .padding(.vertical, 16)
                                .background(
                                    AssetColors.onTertiaryContainer.swiftUIColor, in: RoundedRectangle(cornerRadius: 28)
                                )
                            }
                        }

                        if let videoUrlString = presenter.timetableItem.timetableItem.asset.videoUrl,
                            let videoUrl = URL(string: videoUrlString)
                        {
                            Button {
                                showingURL = videoUrl
                                isShowFabMenu = false
                            } label: {
                                HStack(spacing: 8) {
                                    AssetImages.icPlayCircle.swiftUIImage

                                    Text(String(localized: "Watch video", bundle: .module))
                                        .font(Typography.titleMedium)
                                }
                                .foregroundStyle(AssetColors.tertiaryContainer.swiftUIColor)
                                .padding(.horizontal, 24)
                                .padding(.vertical, 16)
                                .background(
                                    AssetColors.onTertiaryContainer.swiftUIColor, in: RoundedRectangle(cornerRadius: 28)
                                )
                            }
                        }
                    }

                    Button {
                        isShowFabMenu.toggle()
                    } label: {
                        Group {
                            if isShowFabMenu {
                                Image(systemName: "xmark")
                            } else {
                                if presenter.isFavorite {
                                    AssetImages.icFavFill.swiftUIImage
                                } else {
                                    AssetImages.icFav.swiftUIImage
                                }
                            }
                        }
                        .foregroundStyle(AssetColors.onTertiaryContainer.swiftUIColor)
                        .accessibilityLabel(
                            presenter.isFavorite
                                ? String(localized: "Remove from favorites", bundle: .module)
                                : String(localized: "Add to favorites", bundle: .module)
                        )
                        .frame(width: 56, height: 56)
                        .background {
                            Group {
                                if isShowFabMenu {
                                    Circle()
                                } else {
                                    RoundedRectangle(cornerRadius: 16)
                                }
                            }
                            .foregroundStyle(AssetColors.tertiaryContainer.swiftUIColor)
                        }
                    }
                }
            }
            .padding(.horizontal, 16)
        }
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
                    type: .roomJ,
                    sort: 1
                ),
                targetAudience: "初心者向け",
                language: TimetableLanguage(langOfSpeaker: "JA", isInterpretationTarget: false),
                asset: TimetableAsset(
                    videoUrl: "https://2025.droidkaigi.jp/", slideUrl: "https://2025.droidkaigi.jp/en/"),
                levels: ["初級"],
                speakers: [],
                description: MultiLangText(jaTitle: "セッションの説明", enTitle: "Session description"),
                message: MultiLangText(jaTitle: "このセッションは中止になりました", enTitle: "This session has been cancelled")
            ),
            isFavorited: false
        )
    )
}
