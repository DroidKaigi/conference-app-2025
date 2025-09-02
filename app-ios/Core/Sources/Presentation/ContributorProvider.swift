import Dependencies
import Foundation
import Model
import Observation
import UseCase

@Observable
@MainActor
public final class ContributorProvider {
    @ObservationIgnored
    @Dependency(\.contributorsUseCase) private var contributorsUseCase

    public var contributors: [Model.Contributor] = []
    public var isLoading = false

    public init() {}

    public func loadContributors() async {
        isLoading = true
        defer { isLoading = false }

        for await contributorsList in contributorsUseCase.load() {
            contributors = contributorsList
        }
    }
}
