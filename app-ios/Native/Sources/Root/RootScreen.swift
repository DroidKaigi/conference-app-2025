import AboutFeature
import ContributorFeature
import Dependencies
import EventMapFeature
import FavoriteFeature
import HomeFeature
import Model
import ProfileCardFeature
import SearchFeature
import SponsorFeature
import StaffFeature
import SwiftUI
import Theme
import TimetableDetailFeature

private enum TabType: CaseIterable, Hashable {
    case timetable
    case map
    case favorite
    case info
    case profileCard

    func tabImageName(_ selectedTab: TabType) -> String {
        switch self {
        case .timetable:
            return selectedTab == self ? "ic_timetable.fill" : "ic_timetable"
        case .map:
            return selectedTab == self ? "ic_map.fill" : "ic_map"
        case .favorite:
            return selectedTab == self ? "ic_fav.fill" : "ic_fav"
        case .info:
            return selectedTab == self ? "ic_info.fill" : "ic_info"
        case .profileCard:
            return selectedTab == self ? "ic_profileCard.fill" : "ic_profileCard"
        }
    }
}

public struct RootScreen: View {
    @Environment(\.scenePhase) private var scenePhase
    @State private var selectedTab: TabType = .timetable
    @State private var navigationPath = NavigationPath()
    @State private var aboutNavigationPath = NavigationPath()
    @State private var favoriteNavigationPath = NavigationPath()
    @State private var composeMultiplatformEnabled = false
    private let presenter = RootPresenter()

    public init() {
        UITabBar.appearance().unselectedItemTintColor = UIColor(named: "tab_inactive")
    }

    public var body: some View {
        Group {
            if composeMultiplatformEnabled {
                KmpAppComposeViewControllerWrapper()
                    .ignoresSafeArea(.all)
            } else {
                ZStack(alignment: .bottom) {
                    tabContent
                    tabBar
                }
            }
        }
        .environment(\.colorScheme, .dark)
        .onAppear {
            // Register custom fonts from Theme bundle so Font.custom can resolve them.
            ThemeFonts.registerAll()
            presenter.prepareWindow()
        }
    }

    @ViewBuilder
    private var tabContent: some View {
        switch selectedTab {
        case .timetable:
            timetableTab
        case .map:
            mapTab
        case .favorite:
            favoriteTab
        case .info:
            infoTab
        case .profileCard:
            profileCardTab
        }
    }

    private var timetableTab: some View {
        NavigationStack(path: $navigationPath) {
            HomeScreen(onNavigate: handleHomeNavigation)
                .navigationDestination(for: NavigationDestination.self) { destination in
                    let navigationHandler = NavigationHandler(
                        handleSearchNavigation: handleSearchNavigation
                    )
                    destination.view(with: navigationHandler)
                }
        }
    }

    private var mapTab: some View {
        NavigationStack {
            EventMapScreen()
        }
    }

    private var favoriteTab: some View {
        NavigationStack(path: $favoriteNavigationPath) {
            FavoriteScreen(onNavigate: handleFavoriteNavigation)
                .navigationDestination(for: FavoriteNavigationDestination.self) { destination in
                    switch destination {
                    case .timetableDetail(let item):
                        TimetableDetailScreen(timetableItem: item)
                    }
                }
        }
    }

    private var infoTab: some View {
        NavigationStack(path: $aboutNavigationPath) {
            AboutScreen(
                onNavigate: handleAboutNavigation,
                onEnableComposeMultiplatform: handleEnableComposeMultiplatform
            )
            .navigationDestination(for: AboutNavigationDestination.self) { destination in
                aboutDestinationView(for: destination)
            }
        }
    }

    @ViewBuilder
    private func aboutDestinationView(for destination: AboutNavigationDestination) -> some View {
        switch destination {
        case .contributors:
            ContributorScreen()
        case .staff:
            StaffScreen()
        case .sponsors:
            SponsorScreen()
        case .codeOfConduct:
            Text("Code of Conduct")
                .navigationTitle("Code of Conduct")
        case .privacyPolicy:
            Text("Privacy Policy")
                .navigationTitle("Privacy Policy")
        case .licenses:
            Text("Licenses")
                .navigationTitle("Licenses")
        case .settings:
            Text("Settings")
                .navigationTitle("Settings")
        }
    }

    private var profileCardTab: some View {
        NavigationStack {
            ProfileCardScreen()
        }
    }

    private func handleHomeNavigation(_ destination: HomeNavigationDestination) {
        switch destination {
        case .timetableDetail(let item):
            navigationPath.append(NavigationDestination.timetableDetail(item))
        case .search:
            navigationPath.append(NavigationDestination.search)
        }
    }

    private func handleAboutNavigation(_ destination: AboutNavigationDestination) {
        aboutNavigationPath.append(destination)
    }

    private func handleFavoriteNavigation(_ destination: FavoriteNavigationDestination) {
        favoriteNavigationPath.append(destination)
    }

    private func handleSearchNavigation(_ destination: SearchNavigationDestination) {
        switch destination {
        case .timetableDetail(let item):
            navigationPath.append(NavigationDestination.timetableDetail(item))
        }
    }

    private func handleEnableComposeMultiplatform() {
        composeMultiplatformEnabled = true
    }

    @ViewBuilder
    private var tabBar: some View {
        GeometryReader { geometry in
            HStack(spacing: 0) {
                ForEach(TabType.allCases, id: \.self) { item in
                    let isSelected = selectedTab == item
                    Button {
                        selectedTab = item
                    } label: {
                        Image(item.tabImageName(selectedTab))
                            .renderingMode(.template)
                            .tint(
                                isSelected
                                    ? AssetColors.primary40.swiftUIColor : AssetColors.onSurfaceVariant.swiftUIColor
                            )
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                            .contentShape(Rectangle())
                    }
                    .frame(
                        maxWidth: geometry.size.width / CGFloat(TabType.allCases.count),
                        maxHeight: .infinity,
                        alignment: .center
                    )
                }
            }
            .frame(width: geometry.size.width, height: geometry.size.height)
        }
        .frame(height: 64)
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 12)
        .background(.ultraThinMaterial, in: Capsule())
        .overlay(Capsule().stroke(AssetColors.outline.swiftUIColor, lineWidth: 1))
        .environment(\.colorScheme, .dark)
        .padding(.horizontal, 48)
    }
}

#Preview {
    RootScreen()
}
