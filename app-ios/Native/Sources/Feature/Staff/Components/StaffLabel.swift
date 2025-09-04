import Component
import Model
import SwiftUI
import Theme

struct StaffLabel: View {
    let staff: Model.Staff

    var body: some View {
        HStack(spacing: 12) {
            // Avatar with circular shape and border
            CacheAsyncImage(url: staff.iconUrl) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                // Placeholder with background color from Figma
                Circle()
                    .fill(AssetColors.onSurface.swiftUIColor)
            }
            .frame(width: 52, height: 52)
            .clipShape(Circle())
            .overlay(
                Circle()
                    .stroke(AssetColors.outline.swiftUIColor, lineWidth: 1)
            )

            VStack(alignment: .leading, spacing: 0) {
                Text(staff.name)
                    .font(.system(size: 16, weight: .regular))
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                    .lineLimit(1)

                if let role = staff.role {
                    Text(role)
                        .font(.system(size: 12, weight: .regular))
                        .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                        .lineLimit(1)
                }
            }

            Spacer()
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .contentShape(Rectangle())
    }
}
