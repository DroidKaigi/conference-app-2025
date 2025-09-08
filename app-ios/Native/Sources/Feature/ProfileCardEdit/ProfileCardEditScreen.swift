import Component
import Model
import Presentation
import SwiftUI
import Theme

public struct ProfileCardEditScreen: View {
    @State private var presenter = ProfileCardEditPresenter()
    let onNavigate: (ProfileCardEditNavigationDestination) -> Void

    public init(onNavigate: @escaping (ProfileCardEditNavigationDestination) -> Void = { _ in }) {
        self.onNavigate = onNavigate
    }

    public var body: some View {
        ScrollView {
            EditProfileCardForm(presenter: $presenter)
                .padding(.bottom, 80)
        }
        .background(AssetColors.surface.swiftUIColor)
        .navigationTitle("Profile Card")
        .navigationBarTitleDisplayMode(.large)
        .navigationBarBackButtonHidden(presenter.profile.profile == nil)
        .onAppear {
            presenter.profile.subscribeProfileIfNeeded()
            presenter.onComplete = {
                onNavigate(.completed)
            }
        }
        .onChange(of: presenter.profile.isLoading) { _, isLoading in
            // Load form data when profile loading is complete
            if !isLoading {
                presenter.loadForEditing()
            }
        }
    }
}
