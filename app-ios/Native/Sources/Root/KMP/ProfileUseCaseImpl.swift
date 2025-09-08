@preconcurrency import Foundation
import Model
import UseCase
import shared

struct ProfileUseCaseImpl {
    func load() -> any AsyncSequence<Model.Profile?, Never> {
        let profileRepository = KMPDependencyProvider.shared.appGraph.profileRepository
        let profileFlow = profileRepository.profileFlow()

        return profileFlow.map { profileWithImages in
            Model.Profile(from: profileWithImages)
        }
    }

    func save(_ profile: Model.Profile) async {
        do {
            let kmpProfile = shared.Profile(from: profile)
            try await KMPDependencyProvider.shared.appGraph.profileRepository.save(profile: kmpProfile)
        } catch {
            print("Failed to save profile: \(error)")
        }
    }

    private func loadImageDataFromFile(_ filePath: String) -> Data {
        let fileURL = URL(fileURLWithPath: filePath)
        do {
            return try Data(contentsOf: fileURL)
        } catch {
            print("Failed to load image from file \(filePath): \(error)")
            return Data()
        }
    }
}
