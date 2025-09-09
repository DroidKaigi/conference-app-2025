import SwiftUI
import Theme
import Component

struct ToastView: View {
    let message: String
    let action: (title: String, handler: () -> Void)?

    var body: some View {
        HStack {
            Text(message)
                .font(Typography.bodyMedium)
                .foregroundStyle(AssetColors.inverseOnSurface.swiftUIColor)
            Spacer()
            if let action {
                Button {
                    action.handler()
                } label: {
                    Text(action.title)
                        .font(Typography.labelLarge)
                        .foregroundStyle(AssetColors.inversePrimary.swiftUIColor)
                }
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 14)
        .padding(.horizontal, 16)
        .background(AssetColors.inverseSurface.swiftUIColor, in: RoundedRectangle(cornerRadius: 4))
        .padding(.horizontal, 16)
        .padding(.bottom, Constant.bottomPadding)  // Tab bar padding
    }
}

#Preview {
    ToastView(message: "", action: ("一覧を表示", {}))
}
