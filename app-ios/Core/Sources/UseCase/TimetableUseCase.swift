import Dependencies
import DependenciesMacros
import Model

public enum LoadTimetableError: Error {
    case networkError
}

@DependencyClient
public struct TimetableUseCase: Sendable {
    public var load: @Sendable () async throws(LoadTimetableError) -> Timetable = { .init(timetableItems: [], bookmarks: .init()) }
}

public enum TimetableUseCaseKey: TestDependencyKey {
    public static let testValue: TimetableUseCase = TimetableUseCase()
}

public extension DependencyValues {
    var timetableUseCase: TimetableUseCase {
        get { self[TimetableUseCaseKey.self] }
        set { self[TimetableUseCaseKey.self] = newValue }
    }
}
