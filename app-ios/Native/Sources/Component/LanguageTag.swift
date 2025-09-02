import Extension
import Model
import SwiftUI
import Theme

public struct LanguageTag: View {
    let language: TimetableLanguage

    public init(language: TimetableLanguage) {
        self.language = language
    }

    public var body: some View {
        HStack(spacing: 4) {
            ForEach(language.displayLanguages, id: \.self) {
                eachTag($0)
            }
        }
    }

    func eachTag(_ languageCode: String) -> some View {
        Text(languageCode)
            .font(Typography.labelMedium)
            .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            .padding(.horizontal, 6)
            .padding(.vertical, 2)
            .overlay(
                RoundedRectangle(cornerRadius: 6)
                    .stroke(AssetColors.outline.swiftUIColor, lineWidth: 1)
            )
    }
}

#Preview {
    LanguageTag(language: TimetableLanguage(langOfSpeaker: "english", isInterpretationTarget: true))
}
