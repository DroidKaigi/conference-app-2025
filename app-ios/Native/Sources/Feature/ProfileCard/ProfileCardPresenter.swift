import Foundation
import Model
import Observation
import Presentation
import _PhotosUI_SwiftUI

@Observable
class FormState {
    var name: String
    var occupation: String
    var urlString: String
    var image: PhotosPickerItem?
    var cardVariant: ProfileCardVariant

    var nameError: String?
    var occupationError: String?
    var urlError: String?
    var imageError: String?

    init(
        name: String, occupation: String, urlString: String, image: PhotosPickerItem?, cardVariant: ProfileCardVariant
    ) {
        self.name = name
        self.occupation = occupation
        self.urlString = urlString
        self.image = image
        self.cardVariant = cardVariant
    }

    func validate() -> Bool {
        // Name validation
        nameError = name.isEmpty ? String(localized: "Nickname is required", bundle: .module) : nil

        // Occupation validation
        occupationError = occupation.isEmpty ? String(localized: "Occupation is required", bundle: .module) : nil

        // URL validation
        if urlString.isEmpty {
            urlError = String(localized: "Link is required", bundle: .module)
        } else if !isValidURL(urlString) {
            urlError = String(localized: "Invalid URL format", bundle: .module)
        } else {
            urlError = nil
        }

        // Image validation
        imageError = image == nil ? String(localized: "Image is required", bundle: .module) : nil

        return nameError == nil && occupationError == nil && urlError == nil && imageError == nil
    }

    private func isValidURL(_ urlString: String) -> Bool {
        guard let url = URL(string: urlString) else { return false }
        return url.scheme == "http" || url.scheme == "https"
    }

    @MainActor
    func createProfile() async throws -> Profile {
        let imageData = try await image?.loadTransferable(type: Data.self)
        guard let imageData else {
            fatalError("Failed to load image data")
        }

        return Profile(
            name: name,
            occupation: occupation,
            url: URL(string: urlString)!,
            image: imageData,
            cardVariant: cardVariant
        )
    }
}

@MainActor
@Observable
final class ProfileCardPresenter {
    let profile = ProfileProvider()

    var formState: FormState
    var isEditing: Bool

    var shouldEditing: Bool {
        isEditing || profile.profile == nil
    }

    init() {
        formState = FormState(name: "", occupation: "", urlString: "", image: nil, cardVariant: .nightPill)
        isEditing = false
    }

    func loadInitial() {
        profile.subscribeProfileIfNeeded()
    }

    func shareProfileCard() {
        // print("Share profile card tapped")
        // TODO: Implement sharing functionality
    }

    func editProfile() {
        formState = FormState(
            name: profile.profile?.name ?? "",
            occupation: profile.profile?.occupation ?? "",
            urlString: profile.profile?.url.absoluteString ?? "",
            // TODO: enable to set loaded image
            image: nil,
            cardVariant: profile.profile?.cardVariant ?? .nightPill
        )
        isEditing = true
    }

    @MainActor
    func createCard() {
        if !formState.validate() { return }

        Task {
            let profileData = try await formState.createProfile()
            profile.saveProfile(profileData)
            self.isEditing = false
        }
    }

    func setName(_ name: String) {
        formState.name = name
    }

    func setOccupation(_ occupation: String) {
        formState.occupation = occupation
    }

    func setLink(_ linkString: String) {
        formState.urlString = linkString
    }

    func setImage(_ image: PhotosPickerItem?) {
        formState.image = image
    }

    func setCardVariant(_ variant: ProfileCardVariant) {
        formState.cardVariant = variant
    }
}
