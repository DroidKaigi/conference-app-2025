import Component
import Model
import SwiftUI
import Theme

struct EditProfileCardForm: View {
    @Binding var presenter: ProfileCardPresenter

    enum Field: Hashable {
        case nickName
        case occupation
        case link
        case image
    }
    @FocusState private var focusedField: Field?

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
                        presenter.formState.nameError = nil
                    }
                ),
                errorMessage: presenter.formState.nameError
            )
            .focused($focusedField, equals: .nickName)
            .submitLabel(.next)
            .onSubmit {
                focusedField = .occupation
            }

            ProfileCardInputTextField(
                title: String(localized: "Occupation", bundle: .module),
                text: .init(
                    get: {
                        presenter.formState.occupation
                    },
                    set: {
                        presenter.setOccupation($0)
                        presenter.formState.occupationError = nil
                    }
                ),
                errorMessage: presenter.formState.occupationError
            )
            .focused($focusedField, equals: .occupation)
            .submitLabel(.next)
            .onSubmit {
                focusedField = .link
            }

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
                        presenter.formState.urlError = nil
                    }
                ),
                errorMessage: presenter.formState.urlError
            )
            .focused($focusedField, equals: .link)
            .onSubmit {
                focusedField = nil
            }

            ProfileCardInputImage(
                selectedPhoto: .init(
                    get: {
                        presenter.formState.image
                    },
                    set: {
                        presenter.setImage($0)
                        presenter.formState.imageError = nil
                    }
                ),
                title: String(localized: "Image", bundle: .module),
                dismissKeyboard: {
                    focusedField = nil
                },
                errorMessage: presenter.formState.imageError
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
                focusedField = nil
                presenter.createCard()
            } label: {
                Text(String(localized: "Create Card", bundle: .module))
                    .frame(maxWidth: .infinity)
            }
            .filledButtonStyle()
        }
        .padding(.horizontal, 16)
        .onTapGesture {
            focusedField = nil
        }
    }
}
