import Model
import SwiftUI
import Theme

struct SessionCancellationBanner: View {
    let message: String

    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: "info.circle.fill")
                .foregroundStyle(AssetColors.errorContainer.swiftUIColor)
                .frame(width: 16, height: 16)

            Text(message)
                .font(Typography.bodySmall)
                .foregroundStyle(AssetColors.errorContainer.swiftUIColor)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
}

#Preview {
    SessionCancellationBanner(message: "このセッションは中止になりました")
        .padding()
}
