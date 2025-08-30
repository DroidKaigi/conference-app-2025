import Component
import Model
import SwiftUI
import Theme

// TODO: add varidation
struct EditProfileCardForm: View {
    @Binding var presenter: ProfileCardPresenter

    var body: some View {
        VStack(spacing: 32) {
            Text(
                String(
                    localized: "Let's create a profile card to introduce yourself at events or on social media!",
                    bundle: .module)
            )
            .typography(Typography.bodyLarge)
            .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            .multilineTextAlignment(.leading)

            ProfileCardInputTextField(
                title: String(localized: "Nickname", bundle: .module),
                text: .init(
                    get: {
                        presenter.formState.name
                    },
                    set: {
                        presenter.setName($0)
                    }
                ),
            )

            ProfileCardInputTextField(
                title: String(localized: "Occupation", bundle: .module),
                text: .init(
                    get: {
                        presenter.formState.occupation
                    },
                    set: {
                        presenter.setOccupation($0)
                    }
                ),
            )

            ProfileCardInputTextField(
                title: String(localized: "Link（ex.X、Instagram...）", bundle: .module),
                placeholder: "https://",
                keyboardType: .URL,
                text: .init(
                    get: {
                        presenter.formState.urlString
                    },
                    set: {
                        presenter.setLink($0)
                    }
                )
            )

            ProfileCardInputImage(
                selectedPhoto: .init(
                    get: {
                        presenter.formState.image
                    },
                    set: {
                        presenter.setImage($0)
                    }
                ),
                title: String(localized: "Image", bundle: .module)
            )

            ProfileCardInputCardVariant(
                selectedCardType: .init(
                    get: {
                        presenter.formState.cardVariant
                    },
                    set: {
                        presenter.setCardVariant($0)
                    }
                )
            )

            Button {
                presenter.createCard()
            } label: {
                Text(String(localized: "Create Card", bundle: .module))
                    .frame(maxWidth: .infinity)
            }
            .filledButtonStyle()
        }
        .padding(.horizontal, 16)
    }
}
