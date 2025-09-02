import Foundation
import Model
import SwiftUI
import UseCase
import os.log

@MainActor
final class NotificationNavigationCoordinator: NotificationNavigationHandler {
    private let navigateToTimetableDetail: (String) -> Void

    init(navigateToTimetableDetail: @escaping (String) -> Void) {
        self.navigateToTimetableDetail = navigateToTimetableDetail
    }

    func navigateToSession(itemId: String) async {
        guard !itemId.isEmpty else {
            return
        }

        // Delegate navigation to RootScreen which handles the actual item finding and navigation
        await MainActor.run {
            navigateToTimetableDetail(itemId)
        }
    }
}
