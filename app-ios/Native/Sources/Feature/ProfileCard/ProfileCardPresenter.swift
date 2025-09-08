import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class ProfileCardPresenter {
    let profile = ProfileProvider()

    func loadInitial() {
        profile.subscribeProfileIfNeeded()
    }

    func shareProfileCard() {
        // print("Share profile card tapped")
        // TODO: Implement sharing functionality
    }
}
