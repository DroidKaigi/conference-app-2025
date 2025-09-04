import Component
import Model
import SwiftUI
import Theme

struct ContributorListItem: View {
    let contributor: Model.Contributor

    var body: some View {
        HStack(spacing: 16) {
            CacheAsyncImage(url: contributor.iconUrl) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Image(systemName: "person.circle.fill")
                    .resizable()
                    .foregroundColor(AssetColors.onSurface.swiftUIColor.opacity(0.6))
            }
            .frame(width: 56, height: 56)
            .clipShape(Circle())

            Text(contributor.name)
                .font(.body)
                .foregroundColor(AssetColors.onSurface.swiftUIColor)

            Spacer()
        }
        .padding(.vertical, 12)
    }
}
