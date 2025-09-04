import Extension
import Model
import SwiftUI
import Theme

public struct CircularUserIcon: View {
    let imageUrl: String?

    public init(imageUrl: String?) {
        self.imageUrl = imageUrl
    }

    public var body: some View {
        if let imageUrl, let url = URL(string: imageUrl) {
            CacheAsyncImage(url: url) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .overlay {
                        Circle()
                            .stroke(AssetColors.outline.swiftUIColor)
                    }
            } placeholder: {
                Image(systemName: "person.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .foregroundStyle(AssetColors.outline.swiftUIColor)
            }
            .clipShape(Circle())
        } else {
            Image(systemName: "person.circle.fill")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .foregroundStyle(AssetColors.outline.swiftUIColor)
        }
    }
}

#Preview {
    CircularUserIcon(imageUrl: "https://placeholder.jp/150x150.png")
}
