import SwiftUI

// Sponsor card style
struct SponsorCardStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        ZStack {
            configuration.label
            if configuration.isPressed {
                Color.black
                    .opacity(0.2)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
            }
        }
    }
}

extension View {
    func sponsorCardStyle() -> some View {
        buttonStyle(SponsorCardStyle())
    }
}
