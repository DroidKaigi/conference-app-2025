import Dependencies
import Model
import Testing
@testable import Presentation

struct TimetableProviderTest {
    @MainActor
    @Test
    func load() async throws {
        let timetable = Timetable(sample: "test data")
        let provider = withDependencies {
            $0.timetableUseCase.load = {
                timetable
            }
        } operation: {
            TimetableProvider()
        }

        await provider.fetchTimetable()
        #expect(provider.timetable == timetable)
    }
}
