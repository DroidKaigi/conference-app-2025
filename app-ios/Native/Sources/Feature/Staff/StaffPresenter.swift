import Dependencies
import Foundation
import Model
import Observation
import Presentation
import UseCase

#if os(iOS)
    import UIKit
#endif

@MainActor
@Observable
final class StaffPresenter {
    private let staffProvider = Presentation.StaffProvider()

    var staffList: [Model.Staff] = []
    var isLoading = false

    private var staffTask: Task<Void, Never>? {
        willSet {
            staffTask?.cancel()
        }
    }

    init() {}

    func loadStaff() async {
        isLoading = true

        staffTask = Task {
            for await staffList in staffProvider.loadStaff() {
                guard !Task.isCancelled else { break }
                self.staffList = staffList
                self.isLoading = false
            }
        }
    }

    func staffTapped(_ staff: Model.Staff) {
        // Open GitHub profile if available
        if let profileUrl = staff.profileUrl {
            #if os(iOS)
                UIApplication.shared.open(profileUrl)
            #endif
        }
    }

    func cleanup() {
        staffTask = nil
    }
}
