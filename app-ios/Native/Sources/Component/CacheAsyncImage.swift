import SwiftUI

public struct CacheAsyncImage<Content, PlaceHolder>: View where Content: View, PlaceHolder: View {

    private let url: URL?
    private let scale: CGFloat
    private let contentImage: (Image) -> Content
    private let placeholder: () -> PlaceHolder

    @State private var cachedImage: Image?

    public init(
        url: URL?,
        scale: CGFloat = 1.0,
        contentImage: @escaping (Image) -> Content,
        placeholder: @escaping () -> PlaceHolder
    ) {
        self.url = url
        self.scale = scale
        self.contentImage = contentImage
        self.placeholder = placeholder
    }

    public var body: some View {
        Group {
            if let cachedImage {
                contentImage(cachedImage)
            } else {
                AsyncImage(
                    url: url,
                    scale: scale,
                    content: { image in
                        cacheAndRender(image: image)
                    },
                    placeholder: placeholder
                )
            }
        }
        .task(id: url) {
            // Load from cache asynchronously when the view appears or url changes
            cachedImage = await ImageCache.shared.get(for: url)
        }
    }
}

extension CacheAsyncImage {
    fileprivate func cacheAndRender(image: Image) -> some View {
        // Hop to the ImageCache actor to perform the mutation.
        Task { @MainActor in
            await ImageCache.shared.set(image: image, for: url)
        }
        return contentImage(image)
    }
}

private actor ImageCache {
    static let shared = ImageCache()
    private var cache: [URL: Image] = [:]

    func set(image: Image, for url: URL?) {
        guard let url else { return }
        cache[url] = image
    }

    func get(for url: URL?) -> Image? {
        guard let url else { return nil }
        return cache[url]
    }
}

#Preview {
    CacheAsyncImage(
        url: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=48&v=4")!,
        scale: 1.0
    ) { image in
        image.resizable()
            .frame(width: 24, height: 24)
    } placeholder: {
        Color.gray
            .frame(width: 24, height: 24)
            .cornerRadius(12)
    }

}
