import LicenseList
import SwiftUI
import Theme

public struct LicenseScreen: View {
    public init() {}

    public var body: some View {
        LicenseListView()
            .background(AssetColors.surface.swiftUIColor)
            .navigationTitle(String(localized: "Licenses", bundle: .module))
            #if os(iOS)
                .navigationBarTitleDisplayMode(.large)
            #endif
    }
}

#Preview {
    NavigationStack {
        LicenseScreen()
    }
}
