import SwiftUI
import Theme
import Component

public struct StaffScreen: View {
    @State private var presenter = StaffPresenter()

    public init() {}

    public var body: some View {
        Group {
            if presenter.isLoading {
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(AssetColors.surface.swiftUIColor)
            } else {
                ScrollView {
                    LazyVStack(spacing: 0) {
                        ForEach(presenter.staffList) { staff in
                            Button {
                                presenter.staffTapped(staff)
                            } label: {
                                StaffLabel(staff: staff)
                            }
                            .buttonStyle(PlainButtonStyle())
                        }
                    }
                    .padding(.bottom, Constant.bottomPadding)
                }
                .background(AssetColors.surface.swiftUIColor)
            }
        }
        .navigationTitle(String(localized: "スタッフ", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .task {
            await presenter.loadStaff()
        }
    }
}

#Preview {
    StaffScreen()
}
