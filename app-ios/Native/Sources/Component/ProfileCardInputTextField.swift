import SwiftUI
import Theme

public struct ProfileCardInputTextField: View {
    var title: String
    var placeholder: String = ""
    var keyboardType: UIKeyboardType = .default
    @Binding var text: String

    public init(title: String, placeholder: String = "", keyboardType: UIKeyboardType = .default, text: Binding<String>)
    {
        self.title = title
        self.placeholder = placeholder
        self.keyboardType = keyboardType
        self._text = text
    }

    public var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .typographyStyle(.titleMedium)
                .foregroundStyle(.white)

            TextField(placeholder, text: $text)
                .keyboardType(keyboardType)
                .padding(.horizontal, 16)
                .padding(.vertical, 4)
                .frame(height: 56)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(
                            AssetColors.outline.swiftUIColor,
                            style: StrokeStyle(lineWidth: 1)
                        )
                )
        }
    }
}

#Preview {
    ProfileCardInputTextField(
        title: "Nickname",
        text: .init(get: { "" }, set: { _ in })
    )
}
