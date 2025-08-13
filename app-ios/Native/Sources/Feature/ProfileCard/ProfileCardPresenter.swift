import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class ProfileCardPresenter {
    var userName: String = "DroidKaigi"
    var userRole: String = "Android Developer"
    var userCompany: String = "Tech Company"
    var userBio: String = "Passionate about Android development and attending DroidKaigi 2025!"

    init() {}

    func shareProfileCard() {
        // print("Share profile card tapped")
        // TODO: Implement sharing functionality
    }

    func editProfile() {
        // print("Edit profile tapped")
        // TODO: Implement profile editing
    }
}
