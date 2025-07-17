import Dependencies
import SwiftUI
import HomeFeature

private enum TabType {
    case home
}

public struct RootScreen: View {
    @Environment(\.scenePhase) private var scenePhase
    private let presenter = RootPresenter()

    public init() {}

    public var body: some View {
        TabView {
            Tab {
                HomeScreen()
            } label: {
                Image(systemName: "house")
            }
            Tab {
                HomeScreen()
            } label: {
                Image(systemName: "house.lodge")
            }
        }
        .onAppear {
            presenter.prepareWindow()
        }
        .onChange(of: scenePhase) {
            ScenePhaseHandler.handle(scenePhase)
        }
    }
}
