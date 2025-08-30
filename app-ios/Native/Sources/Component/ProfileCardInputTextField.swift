import SwiftUI
import Theme

public struct ProfileCardInputTextField: View {
    var title: String
    var placeholder: String = ""
    @Binding var text: String
    var errorMessage: String?

    public init(title: String, placeholder: String = "", text: Binding<String>, errorMessage: String? = nil) {
        self.title = title
        self.placeholder = placeholder
        self._text = text
        self.errorMessage = errorMessage
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .typographyStyle(.titleMedium)
                .foregroundStyle(.white)

            TextField(placeholder, text: $text)
                .padding(.horizontal, 16)
                .padding(.vertical, 4)
                .frame(height: 56)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(
                            errorMessage != nil ? AssetColors.error.swiftUIColor : AssetColors.outline.swiftUIColor,
                            style: StrokeStyle(lineWidth: 1)
                        )
                )

            if let errorMessage = errorMessage {
                Text(errorMessage)
                    .typographyStyle(.bodySmall)
                    .foregroundStyle(AssetColors.error.swiftUIColor)
                    .padding(.leading, 4)
            }
        }
    }
}

#Preview {
    ProfileCardInputTextField(
        title: "Nickname",
        text: .init(get: { "" }, set: { _ in })
    )
}
