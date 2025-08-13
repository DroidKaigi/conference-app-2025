import SwiftUI

public let changoFontName: String = "Chango-Regular"

public enum Typography {
    // MARK: - Display
    public static let displayLarge: SwiftUI.Font = .system(size: 57, weight: .regular)
        .leading(.tight)

    public static let displayMedium: SwiftUI.Font = .system(size: 45, weight: .regular)

    public static let displaySmall: SwiftUI.Font = .system(size: 36, weight: .regular)

    // MARK: - Headline
    public static let headlineLarge: SwiftUI.Font = .system(size: 32, weight: .regular)

    public static let headlineMedium: SwiftUI.Font = .system(size: 28, weight: .regular)

    public static let headlineSmall: SwiftUI.Font = .system(size: 24, weight: .regular)

    // MARK: - Title
    public static let titleLarge: SwiftUI.Font = .system(size: 22, weight: .regular)

    public static let titleMedium: SwiftUI.Font = .system(size: 16, weight: .regular)

    public static let titleSmall: SwiftUI.Font = .system(size: 14, weight: .regular)

    // MARK: - Label
    public static let labelLarge: SwiftUI.Font = .system(size: 14, weight: .medium)

    public static let labelMedium: SwiftUI.Font = .system(size: 12, weight: .medium)

    public static let labelSmall: SwiftUI.Font = .system(size: 11, weight: .medium)

    // MARK: - Body
    public static let bodyLarge: SwiftUI.Font = .system(size: 16, weight: .regular)

    public static let bodyMedium: SwiftUI.Font = .system(size: 14, weight: .regular)

    public static let bodySmall: SwiftUI.Font = .system(size: 12, weight: .regular)
}

// MARK: - View Extension for Typography
extension View {
    public func typography(_ style: SwiftUI.Font) -> some View {
        self.font(style)
    }
}

// MARK: - Text Style Definitions with Line Height
public struct TypographyStyle: Sendable {
    public let font: SwiftUI.Font
    public let lineHeight: CGFloat
    public let letterSpacing: CGFloat

    public init(font: SwiftUI.Font, lineHeight: CGFloat, letterSpacing: CGFloat) {
        self.font = font
        self.lineHeight = lineHeight
        self.letterSpacing = letterSpacing
    }

    // MARK: - Display Styles
    public static let displayLarge = TypographyStyle(
        font: .system(size: 57, weight: .regular),
        lineHeight: 64,
        letterSpacing: -0.25
    )

    public static let displayMedium = TypographyStyle(
        font: .system(size: 45, weight: .regular),
        lineHeight: 52,
        letterSpacing: 0
    )

    public static let displaySmall = TypographyStyle(
        font: .system(size: 36, weight: .regular),
        lineHeight: 44,
        letterSpacing: 0
    )

    // MARK: - Headline Styles
    public static let headlineLarge = TypographyStyle(
        font: .system(size: 32, weight: .regular),
        lineHeight: 40,
        letterSpacing: 0
    )

    public static let headlineMedium = TypographyStyle(
        font: .system(size: 28, weight: .regular),
        lineHeight: 36,
        letterSpacing: 0
    )

    public static let headlineSmall = TypographyStyle(
        font: .system(size: 24, weight: .regular),
        lineHeight: 32,
        letterSpacing: 0
    )

    // MARK: - Title Styles
    public static let titleLarge = TypographyStyle(
        font: .system(size: 22, weight: .regular),
        lineHeight: 28,
        letterSpacing: 0
    )

    public static let titleMedium = TypographyStyle(
        font: .system(size: 16, weight: .regular),
        lineHeight: 24,
        letterSpacing: 0.15
    )

    public static let titleSmall = TypographyStyle(
        font: .system(size: 14, weight: .regular),
        lineHeight: 20,
        letterSpacing: 0.1
    )

    // MARK: - Label Styles
    public static let labelLarge = TypographyStyle(
        font: .system(size: 14, weight: .medium),
        lineHeight: 20,
        letterSpacing: 0.1
    )

    public static let labelMedium = TypographyStyle(
        font: .system(size: 12, weight: .medium),
        lineHeight: 16,
        letterSpacing: 0.5
    )

    public static let labelSmall = TypographyStyle(
        font: .system(size: 11, weight: .medium),
        lineHeight: 16,
        letterSpacing: 0.5
    )

    // MARK: - Body Styles
    public static let bodyLarge = TypographyStyle(
        font: .system(size: 16, weight: .regular),
        lineHeight: 24,
        letterSpacing: 0.5
    )

    public static let bodyMedium = TypographyStyle(
        font: .system(size: 14, weight: .regular),
        lineHeight: 20,
        letterSpacing: 0.25
    )

    public static let bodySmall = TypographyStyle(
        font: .system(size: 12, weight: .regular),
        lineHeight: 16,
        letterSpacing: 0.4
    )
}

// MARK: - View Modifier for Typography Style
public struct TypographyModifier: ViewModifier {
    let style: TypographyStyle

    public func body(content: Content) -> some View {
        content
            .font(style.font)
            .tracking(style.letterSpacing)
            .lineSpacing(style.lineHeight - UIFont.systemFont(ofSize: 1).lineHeight)
    }
}

extension View {
    public func typographyStyle(_ style: TypographyStyle) -> some View {
        self.modifier(TypographyModifier(style: style))
    }
}
