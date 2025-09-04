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
            Text(String(localized: "DroidKaigi is a conference for Android developers", bundle: .module))
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .font(Typography.titleMedium)
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

                    Text(String(localized: "Date", bundle: .module))
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .typographyStyle(.titleSmall)
                        .padding(.trailing, 4)

                    Text(String(localized: "2025.09.10(Wed) - 12(Fri)", bundle: .module))
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

                    Text(String(localized: "Location", bundle: .module))
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .typographyStyle(.titleSmall)
                        .padding(.trailing, 4)

                    Text(String(localized: "Bellesalle Shibuya Garden", bundle: .module))
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                        .font(Typography.titleSmall)
                        .lineSpacing(-6)

                    if let mapURL = URL(string: "https://goo.gl/maps/vv9sE19JvRjYKtSP9") {
                        Link(destination: mapURL) {
                            Text(String(localized: "Check Map", bundle: .module))
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
            .background(AssetColors.surfaceContainerLow.swiftUIColor, in: RoundedRectangle(cornerRadius: 24))
            .padding(.horizontal, 16)
        }
    }
}

#Preview {
    KeyVisual()
        .background(AssetColors.surface.swiftUIColor)
}
