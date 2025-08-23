import SwiftUI
import Theme

struct KeyVisual: View {
    var body: some View {
        VStack(spacing: 0) {
            // Header Logo
            Image("X_Header", bundle: .module)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity)
                .padding(.bottom, 16)

            // Conference description text
            Text("DroidKaigi is a conference for Android developers")
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .typographyStyle(.titleMedium)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 16)
                .padding(.bottom, 20)

            // Date and Location info container
            VStack(alignment: .leading, spacing: 12) {
                // Date row
                HStack(spacing: 8) {
                    AssetImages.icSchedule.swiftUIImage
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)

                    Text("Date:")
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .typographyStyle(.titleSmall)
                        .padding(.trailing, 4)

                    Text("2025.09.11(Thu) - 12(Fri)")
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .typographyStyle(.titleSmall)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                // Location row
                HStack(spacing: 8) {
                    AssetImages.icLocationOn.swiftUIImage
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)

                    Text("Location:")
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .typographyStyle(.titleSmall)
                        .padding(.trailing, 4)

                    Text("Bellesalle Shibuya Garden")
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .font(.system(size: 14, weight: .regular))
                        .lineSpacing(-6)

                    if let mapURL = URL(string: "https://goo.gl/maps/vv9sE19JvRjYKtSP9") {
                        Link(destination: mapURL) {
                            Text("Check Map")
                                .typographyStyle(.titleSmall)
                                .foregroundStyle(AssetColors.jellyfish.swiftUIColor)
                                .underline()
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 20)
            .padding(.horizontal, 16)
            .background(AssetColors.surfaceContainerLow.swiftUIColor, in: RoundedRectangle(cornerRadius: 10))
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(style: StrokeStyle(lineWidth: 1, dash: [2, 2]))
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            )
            .padding(.horizontal, 16)
        }
    }
}

#Preview {
    KeyVisual()
        .background(AssetColors.surface.swiftUIColor)
}
