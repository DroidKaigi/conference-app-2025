import SwiftUI

extension View {
    public func hiddenTabBarIfNeeded() -> some View {
        self.closureModifier { content in
            if #available(iOS 26.0, *) {
                content
            } else {
                content
                    .toolbarVisibility(.hidden, for: .tabBar)
            }
        }
    }
}
