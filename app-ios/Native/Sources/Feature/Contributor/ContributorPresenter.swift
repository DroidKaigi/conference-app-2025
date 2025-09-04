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
final class ContributorPresenter {
    let contributorProvider = Presentation.ContributorProvider()

    init() {}

    func loadContributors() async {
        await contributorProvider.loadContributors()
    }

    func contributorTapped(_ contributor: Model.Contributor) {
        #if os(iOS)
            UIApplication.shared.open(contributor.url)
        #endif
    }
}
