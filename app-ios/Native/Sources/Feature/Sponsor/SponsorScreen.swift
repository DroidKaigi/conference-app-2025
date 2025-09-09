import Component
import SwiftUI
import Theme

public struct SponsorScreen: View {
    @State private var presenter = SponsorPresenter()

    public init() {}

    public var body: some View {
        ScrollView {
            LazyVStack(spacing: 32) {
                ForEach(presenter.sponsorProvider.sponsors) { category in
                    SponsorSection(category: category) { sponsor in
                        presenter.sponsorTapped(sponsor)
                    }
                }
            }
            .padding(.top, 24)
            .padding(.bottom, Constant.bottomPadding)
        }
        .background(AssetColors.background.swiftUIColor)  // #111418
        .navigationTitle("スポンサー")
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .task {
            await presenter.loadSponsors()
        }
    }
}

#Preview {
    SponsorScreen()
}
