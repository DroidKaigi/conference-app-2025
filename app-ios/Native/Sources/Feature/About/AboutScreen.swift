import Dependencies
import DependencyExtra
import SwiftUI
import Theme

public struct AboutScreen: View {
    @State private var presenter = AboutPresenter()
    @Dependency(\.safari) var safari
    let onNavigate: (AboutNavigationDestination) -> Void
    let onEnableComposeMultiplatform: () -> Void

    @State private var showSwitchToComposeMultiplatformAlert = false

    var version: String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? ""
    }

    public init(
        onNavigate: @escaping (AboutNavigationDestination) -> Void = { _ in },
        onEnableComposeMultiplatform: @escaping () -> Void = {}
    ) {
        self.onNavigate = onNavigate
        self.onEnableComposeMultiplatform = onEnableComposeMultiplatform
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 32) {
                KeyVisual()

                VStack(spacing: 32) {
                    creditsSection

                    othersSection

                    socialMediaSection

                    versionSection
                }
                .padding(.horizontal, 16)
            }
            .padding(.bottom, 80)  // Tab bar padding
        }
        .background(AssetColors.surface.swiftUIColor)
        .navigationTitle(String(localized: "About DroidKaigi", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
    }

    @ViewBuilder
    private var creditsSection: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("Credits", bundle: .module)
                .foregroundStyle(AssetColors.primaryFixed.swiftUIColor)
                .font(.subheadline)
                .padding(.bottom, 8)

            VStack(spacing: 0) {
                AboutButton(
                    title: String(localized: "Contributors", bundle: .module),
                    image: AssetImages.icDiversity1.swiftUIImage
                ) {
                    onNavigate(.contributors)
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)

                AboutButton(
                    title: String(localized: "Staffs", bundle: .module),
                    image: AssetImages.icSentimentVerySatisfied.swiftUIImage
                ) {
                    onNavigate(.staff)
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)

                AboutButton(
                    title: String(localized: "Sponsors", bundle: .module),
                    image: AssetImages.icApartment.swiftUIImage
                ) {
                    onNavigate(.sponsors)
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)
            }
        }
    }

    @ViewBuilder
    private var othersSection: some View {
        // swiftlint:disable:next closure_body_length
        VStack(alignment: .leading, spacing: 0) {
            Text("Others", bundle: .module)
                .foregroundStyle(AssetColors.primaryFixed.swiftUIColor)
                .font(.subheadline)
                .padding(.bottom, 8)

            VStack(spacing: 0) {
                AboutButton(
                    title: String(localized: "Code of Conduct", bundle: .module),
                    image: AssetImages.icGavel.swiftUIImage
                ) {
                    Task {
                        await safari(URL(string: String(localized: "CodeOfConductURL", bundle: .module))!)
                    }
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)

                AboutButton(
                    title: String(localized: "Licenses", bundle: .module),
                    image: AssetImages.icFileCopy.swiftUIImage
                ) {
                    onNavigate(.licenses)
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)

                AboutButton(
                    title: String(localized: "Privacy Policy", bundle: .module),
                    image: AssetImages.icPrivacyTip.swiftUIImage
                ) {
                    Task {
                        await safari(URL(string: String(localized: "PrivacyPolicyURL", bundle: .module))!)
                    }
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)

                AboutButton(
                    title: String(localized: "Settings", bundle: .module),
                    image: AssetImages.icSettings.swiftUIImage
                ) {
                    onNavigate(.settings)
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)

                AboutButton(
                    title: String(localized: "Switch to Compose Multiplatform", bundle: .module),
                    systemName: "switch.2",
                ) {
                    showSwitchToComposeMultiplatformAlert = true
                }
                .alert(
                    String(localized: "Switch UI", bundle: .module), isPresented: $showSwitchToComposeMultiplatformAlert
                ) {
                    Button(String(localized: "Cancel", bundle: .module), role: .cancel) {}
                    Button(String(localized: "Switch", bundle: .module)) {
                        onEnableComposeMultiplatform()
                    }
                } message: {
                    Text(
                        String(
                            localized:
                                "Switch UI from SwiftUI to Compose Multiplatform. Are you sure you want to do this?",
                            bundle: .module))
                }

                Divider()
                    .background(AssetColors.outlineVariant.swiftUIColor)
            }
        }
    }

    @ViewBuilder
    private var socialMediaSection: some View {
        HStack(spacing: 12) {
            SocialButton(
                imageName: "ic_youtube_logo"
            ) {
                Task {
                    await safari(URL(string: "https://www.youtube.com/c/DroidKaigi")!)
                }
            }

            SocialButton(
                imageName: "ic_xcom_logo"
            ) {
                Task {
                    await safari(URL(string: "https://x.com/DroidKaigi")!)
                }
            }

            SocialButton(
                imageName: "ic_medium_logo"
            ) {
                Task {
                    await safari(URL(string: "https://medium.com/droidkaigi")!)
                }
            }
        }
    }

    @ViewBuilder
    private var versionSection: some View {
        VStack(spacing: 0) {
            Text(String(localized: "App Version", bundle: .module))
                .font(.caption)

            Text(version)
                .font(.caption)
        }
        .foregroundStyle(AssetColors.primaryFixed.swiftUIColor)
        .padding(.bottom, 16)
    }
}

struct AboutButton: View {
    let title: String
    let systemName: String?
    let image: Image?
    let action: () -> Void

    init(title: String, systemName: String? = nil, image: Image? = nil, action: @escaping () -> Void) {
        self.title = title
        self.systemName = systemName
        self.image = image
        self.action = action
    }

    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                if let systemName {
                    Image(systemName: systemName)
                        .frame(width: 24, height: 24)
                        .foregroundColor(AssetColors.primaryFixed.swiftUIColor)
                } else if let image {
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 24, height: 24)
                        .foregroundColor(AssetColors.primaryFixed.swiftUIColor)
                }

                Text(title)
                    .foregroundColor(AssetColors.primaryFixed.swiftUIColor)

                Spacer()
            }
            .padding(.vertical, 16)
            .padding(.horizontal, 12)
            .contentShape(Rectangle())
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct SocialButton: View {
    let imageName: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Image(imageName, bundle: .module)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 48, height: 48)
        }
    }
}

#Preview {
    AboutScreen()
}
