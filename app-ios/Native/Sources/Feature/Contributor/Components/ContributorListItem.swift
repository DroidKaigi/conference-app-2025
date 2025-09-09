import Component
import Model
import SwiftUI
import Theme

struct ContributorListItem: View {
    let contributor: Model.Contributor

    var body: some View {
        HStack(spacing: 16) {
            CircularUserIcon(imageUrl: contributor.iconUrl.absoluteString)
                .frame(width: 56, height: 56)

            Text(contributor.name)
                .font(.body)
                .foregroundColor(AssetColors.onSurface.swiftUIColor)

            Spacer()
        }
        .padding(.vertical, 12)
    }
}
