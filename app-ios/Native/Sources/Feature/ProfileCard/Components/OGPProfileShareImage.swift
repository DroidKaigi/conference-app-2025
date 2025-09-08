import Model
import SwiftUI

public struct OGPProfileShareImage: View {
    let profile: Model.Profile

    public var body: some View {
        ZStack(alignment: .topLeading) {
            Image(profile.cardVariant.type == .day ? .nightOgpBase : .dayOgpBase)
                .resizable()
            FrontCard(
                userRole: profile.occupation,
                userName: profile.name,
                cardVariant: profile.cardVariant,
                image: profile.image,
                effectEnabled: false
            )
            .offset(x: 280, y: 60)
            BackCard(cardType: profile.cardVariant.type, url: profile.url, effectEnabled: false)
                .offset(x: 620, y: 190)
            Image(.ogpTopStar)
        }
        .frame(width: 1200, height: 630)
    }

    public func render() -> UIImage? {
        let renderer = ImageRenderer(content: self)
        return renderer.uiImage
    }
}

#Preview("OGPProfileShareImage(dayPill)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 160, height: 160).background(Color.white))
                .uiImage!
                .pngData()!,
            cardVariant: .dayPill
        )
    )
}

#Preview("OGPProfileShareImage(dayFlower)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 160, height: 160).background(Color.white))
                .uiImage!
                .pngData()!,
            cardVariant: .dayFlower
        )
    )
}

#Preview("OGPProfileShareImage(dayDiamond)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 160, height: 160).background(Color.white))
                .uiImage!
                .pngData()!,
            cardVariant: .dayDiamond
        )
    )
}

#Preview("OGPProfileShareImage(nightPill)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 160, height: 160).background(Color.white))
                .uiImage!
                .pngData()!,
            cardVariant: .nightPill
        )
    )
}

#Preview("OGPProfileShareImage(nightFlower)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 160, height: 160).background(Color.white))
                .uiImage!
                .pngData()!,
            cardVariant: .nightFlower
        )
    )
}

#Preview("OGPProfileShareImage(nightDiamond)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 160, height: 160).background(Color.white))
                .uiImage!
                .pngData()!,
            cardVariant: .nightDiamond
        )
    )
}
