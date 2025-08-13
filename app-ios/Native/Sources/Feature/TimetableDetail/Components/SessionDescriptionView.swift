import Model
import SwiftUI
import Theme

struct SessionDescriptionView: View {
    @State private var isDescriptionExpanded = false
    @State private var canBeExpanded = false
    let content: String
    let primaryColor: Color
    let containerColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(.init(content))
                .font(Typography.bodyLarge)
                .textSelection(.enabled)
                .lineLimit(isDescriptionExpanded ? nil : 5)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .background {
                    ViewThatFits(in: .vertical) {
                        Text(content)
                            .hidden()
                        // Just for receiving onAppear event if the description exceeds its line limit
                        Color.clear
                            .onAppear {
                                canBeExpanded = true
                            }
                    }
                }
            if canBeExpanded {
                Button {
                    isDescriptionExpanded = true
                    canBeExpanded = false
                } label: {
                    Text("続きを読む")
                        .font(Typography.labelLarge)
                        .foregroundStyle(primaryColor)
                        .frame(height: 40)
                        .frame(maxWidth: .infinity, alignment: .center)
                        .background(containerColor, in: Capsule())
                }
            }
        }
        .animation(.default, value: isDescriptionExpanded)
    }
}

#Preview {
    VStack {
        SessionDescriptionView(
            content:
                "Kotlin Coroutinesは非同期処理をシンプルに記述できるKotlinの言語機能です。実験的な機能としてこれまでも提供されてきましたがKotlin 1.3で正式にリリース予定です。Androidの誕生から10年たちアプリの利用シーンが増えたKotlin Coroutinesは非同期処理をシンプルに記述できるKotlinの言語機能です。実験的な機能としてこれまでも提供されてきましたがKotlin 1.3で正式にリリース予定です。Androidの誕生から10年たちアプリの利用シーンが増えた.",
            primaryColor: .blue,
            containerColor: .gray
        )
        .padding(.horizontal, 16)
    }
    .background(Color.white)
}
