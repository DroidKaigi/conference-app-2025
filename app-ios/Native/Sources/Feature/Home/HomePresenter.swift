import Observation
import Presentation

@MainActor
@Observable
final class HomePresenter {
    let timetable = TimetableProvider()

    func loadInitial() async {
        await timetable.fetchTimetable()
    }
}
