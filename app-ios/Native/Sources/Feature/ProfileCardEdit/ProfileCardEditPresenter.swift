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
    var existingImageData: Data?

    var nameError: String?
    var occupationError: String?
    var urlError: String?
    var imageError: String?

    init(
        name: String, occupation: String, urlString: String, image: PhotosPickerItem?, cardVariant: ProfileCardVariant,
        existingImageData: Data? = nil
    ) {
        self.name = name
        self.occupation = occupation
        self.urlString = urlString
        self.image = image
        self.cardVariant = cardVariant
        self.existingImageData = existingImageData
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
        imageError =
            (image == nil && existingImageData == nil) ? String(localized: "Image is required", bundle: .module) : nil

        return nameError == nil && occupationError == nil && urlError == nil && imageError == nil
    }

    private func isValidURL(_ urlString: String) -> Bool {
        guard let url = URL(string: urlString) else { return false }
        return url.scheme == "http" || url.scheme == "https"
    }

    enum ProfileCreationError: Error {
        case failedToLoadImageData
        case missingImageData
        case invalidURL
    }
    @MainActor
    func createProfile() async throws -> Profile {
        let imageData: Data

        if let newImage = image {
            guard let data = try await newImage.loadTransferable(type: Data.self) else {
                throw ProfileCreationError.failedToLoadImageData
            }
            imageData = data
        } else if let existingData = existingImageData {
            imageData = existingData
        } else {
            throw ProfileCreationError.missingImageData
        }

        guard let url = URL(string: urlString) else {
            throw ProfileCreationError.invalidURL
        }

        return Profile(
            name: name,
            occupation: occupation,
            url: url,
            image: imageData,
            cardVariant: cardVariant
        )
    }
}

@MainActor
@Observable
final class ProfileCardEditPresenter {
    let profile = ProfileProvider()
    var formState: FormState
    var onComplete: (() -> Void)?

    init() {
        self.formState = FormState(
            name: "", occupation: "", urlString: "", image: nil, cardVariant: .nightPill, existingImageData: nil)
    }

    func loadForEditing() {
        formState = FormState(
            name: profile.profile?.name ?? "",
            occupation: profile.profile?.occupation ?? "",
            urlString: profile.profile?.url.absoluteString ?? "",
            image: nil,
            cardVariant: profile.profile?.cardVariant ?? .nightPill,
            existingImageData: profile.profile?.image
        )
    }

    @MainActor
    func createCard() {
        if !formState.validate() { return }

        Task {
            let profileData = try await formState.createProfile()
            profile.saveProfile(profileData)
            onComplete?()
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
