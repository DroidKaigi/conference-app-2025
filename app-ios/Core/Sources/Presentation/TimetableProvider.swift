import Dependencies
import UseCase
import Observation
import Model

@Observable
public final class TimetableProvider {
    @ObservationIgnored
    @Dependency(\.timetableUseCase) private var timeteableUseCase

    public var timetable: Timetable?

    public init() {}

    @MainActor
    public func fetchTimetable() async {
        do {
            timetable = try await timeteableUseCase.load()
        } catch {
            print(error)
        }
    }
}

