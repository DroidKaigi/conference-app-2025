import SwiftUI
import Theme

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
                            Button(action: {
                                presenter.staffTapped(staff)
                            }) {
                                StaffLabel(staff: staff)
                            }
                            .buttonStyle(PlainButtonStyle())
                        }
                    }
                    .padding(.bottom, 80)
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
