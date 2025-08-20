import SwiftUI
import Theme

// Filled style
public struct FilledButtonStyle: ButtonStyle {
    public func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .typographyStyle(.labelLarge)
            .foregroundColor(AssetColors.primary0.swiftUIColor)
            .padding()
            .background(AssetColors.primary.swiftUIColor)
            .clipShape(.capsule)
            .opacity(configuration.isPressed ? 0.8 : 1)
            .frame(height: 56)
    }
}

// Text style
public struct TextButtonStyle: ButtonStyle {
    public func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .typographyStyle(.labelLarge)
            .foregroundColor(AssetColors.primary.swiftUIColor)
            .padding()
            .background(.clear)
            .contentShape(.capsule)
            .opacity(configuration.isPressed ? 0.8 : 1)
            .frame(height: 56)
    }
}

extension View {
    public func filledButtonStyle() -> some View {
        buttonStyle(FilledButtonStyle())
    }

    public func textButtonStyle() -> some View {
        buttonStyle(TextButtonStyle())
    }
}
