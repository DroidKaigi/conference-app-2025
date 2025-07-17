import UseCase
import Model
import shared

struct TimetableUseCaseImpl {
    func load() async throws(LoadTimetableError) -> Model.Timetable {
        // TODO: Call KMP method
        let kmpTimetable = Timetable.companion.fake()
        return Model.Timetable(from: kmpTimetable)
    }
}
