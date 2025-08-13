import SwiftUI
import Theme

struct InformationRow: View {
    let icon: Image
    let title: String
    let titleColor: Color
    let content: String
    let isStrikethrough: Bool

    init(
        icon: Image,
        title: String,
        titleColor: Color,
        content: String,
        isStrikethrough: Bool = false
    ) {
        self.icon = icon
        self.title = title
        self.titleColor = titleColor
        self.content = content
        self.isStrikethrough = isStrikethrough
    }

    var body: some View {
        HStack(spacing: 12) {
            HStack(spacing: 8) {
                icon
                    .renderingMode(.template)
                    .foregroundStyle(titleColor)
                    .frame(width: 16, height: 16)

                Text(title)
                    .font(Typography.titleSmall)
                    .foregroundStyle(titleColor)
                    .frame(width: 53, alignment: .leading)
            }

            Text(content)
                .font(Typography.bodyMedium)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .strikethrough(isStrikethrough)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    VStack(spacing: 16) {
        InformationRow(
            icon: Image(systemName: "clock"),
            title: "日時",
            titleColor: .blue,
            content: "2024.09.14 / 11:20 ~ 12:00"
        )

        InformationRow(
            icon: Image(systemName: "clock"),
            title: "日時",
            titleColor: .orange,
            content: "2024.09.14 / 11:20 ~ 12:00",
            isStrikethrough: true
        )
    }
    .padding()
}
