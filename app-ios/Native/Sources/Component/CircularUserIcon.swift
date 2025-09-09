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
            } placeholder: {
                // NOTE: If you only use ProgressView, it cannot determine the correct size.
                ZStack {
                    Circle()
                        .foregroundStyle(AssetColors.surface.swiftUIColor)
                    ProgressView()
                }
            }
            .background(AssetColors.surface.swiftUIColor)
            .clipShape(Circle())
            .overlay {
                Circle()
                    .stroke(AssetColors.outline.swiftUIColor)
            }
        } else {
            Image(systemName: "person.circle.fill")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .background(AssetColors.surface.swiftUIColor)
                .overlay {
                    Circle()
                        .stroke(AssetColors.outline.swiftUIColor)
                }
        }
    }
}

#Preview {
    CircularUserIcon(imageUrl: "https://placeholder.jp/150x150.png")
}
