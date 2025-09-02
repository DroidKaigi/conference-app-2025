import Dependencies
import DependenciesMacros
import Model

@DependencyClient
public struct ProfileUseCase: Sendable {
    public var load: @Sendable () -> any AsyncSequence<Model.Profile?, Never> = { AsyncStream.never }
    public var save: @Sendable (_ profile: Model.Profile) async -> Void = { _ in }
}

public enum ProfileUseCaseKey: TestDependencyKey {
    public static let testValue = ProfileUseCase()
}

extension DependencyValues {
    public var profileUseCase: ProfileUseCase {
        get { self[ProfileUseCaseKey.self] }
        set { self[ProfileUseCaseKey.self] = newValue }
    }
}
