import Dependencies
import Model
import Observation
import UseCase

@Observable
public final class ProfileProvider {
    @ObservationIgnored
    @Dependency(\.profileUseCase) private var profileUseCase

    @ObservationIgnored
    private var fetchProfile: Task<Void, Never>?

    public var isLoading: Bool = false
    public var profile: Profile?

    public init() {}

    @MainActor
    public func subscribeProfileIfNeeded() {
        guard fetchProfile == nil else { return }

        isLoading = true

        self.fetchProfile = Task {
            for await profile in profileUseCase.load() {
                self.profile = profile
                self.isLoading = false
            }
        }
    }

    @MainActor
    public func saveProfile(_ profile: Profile) {
        Task {
            await profileUseCase.save(profile)
        }
    }
}
