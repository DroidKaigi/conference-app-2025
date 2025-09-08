import Component
import Model
import Observation
import Presentation
import SwiftUI
import Theme

@MainActor
public struct ProfileCardScreen: View {
    @State private var presenter = ProfileCardPresenter()
    let onNavigate: (ProfileCardNavigationDestination) -> Void

    public init(onNavigate: @escaping (ProfileCardNavigationDestination) -> Void = { _ in }) {
        self.onNavigate = onNavigate
    }

    public var body: some View {
        profileCardScrollView
            .background(AssetColors.surface.swiftUIColor)
            .navigationTitle("Profile Card")
            #if os(iOS)
                .navigationBarTitleDisplayMode(.large)
            #endif
            .onAppear {
                presenter.loadInitial()
            }
            .onChange(of: presenter.profile.isLoading) { _, isLoading in
                // If there is no profile when loading is complete, the edit screen will automatically appear
                if !isLoading && presenter.profile.profile == nil {
                    onNavigate(.edit)
                }
            }
    }

    @ViewBuilder
    private var profileCardScrollView: some View {
        let profile = presenter.profile.profile
        let isLoading = presenter.profile.isLoading
        ScrollView {
            Group {
                if isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                } else if profile != nil {
                    cardView(profile!)
                } else {
                    // when creating a new profile, the editing screen will be navigated automatically
                    EmptyView()
                }
            }
            .padding(.bottom, 80)  // Tab bar padding
        }
    }

    @ViewBuilder
    private func cardView(_ profile: Model.Profile) -> some View {
        VStack(spacing: 0) {
            profileCard(profile)
            actionButtons
        }
        .padding(.vertical, 20)
    }

    @ViewBuilder
    @MainActor
    private func profileCard(_ profile: Model.Profile) -> some View {
        TiltFlipCard(
            front: { normal in
                FrontCard(
                    userRole: profile.occupation,
                    userName: profile.name,
                    cardVariant: profile.cardVariant,
                    image: profile.image,
                    normal: (normal.x, normal.y, normal.z),
                )
            },
            back: { normal in
                BackCard(
                    cardType: profile.cardVariant.type,
                    url: profile.url,
                    normal: (normal.x, normal.y, normal.z),
                )
            }
        )
        .padding(.horizontal, 56)
        .padding(.vertical, 32)
    }

    private var actionButtons: some View {
        VStack(spacing: 8) {
            shareButton
            editButton
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }

    private var shareButton: some View {
        let uiImage = OGPProfileShareImage(profile: presenter.profile.profile!).render()!
        let ogpImage = uiImage.pngData()!
        let shareText = String(localized: "Share Message", bundle: .module)

        return ShareLink(
            item: ShareOGPItem(ogpImage: ogpImage), message: Text(shareText),
            preview: SharePreview(shareText, image: Image(uiImage: uiImage))
        ) {
            HStack {
                AssetImages.icShare.swiftUIImage
                    .resizable()
                    .frame(width: 18, height: 18)
                Text(String(localized: "Share", bundle: .module))
            }
            .frame(maxWidth: .infinity)
        }
        .filledButtonStyle()
    }

    private var editButton: some View {
        Button {
            onNavigate(.edit)
        } label: {
            Text(String(localized: "Edit", bundle: .module))
                .frame(maxWidth: .infinity)
        }
        .textButtonStyle()
    }
}

struct ShareOGPItem: Transferable {
    static var transferRepresentation: some TransferRepresentation {
        DataRepresentation(exportedContentType: .png) { item in
            item.ogpImage
        }
    }

    let ogpImage: Data
}

#Preview {
    ProfileCardScreen()
}
