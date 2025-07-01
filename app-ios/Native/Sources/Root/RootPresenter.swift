import Dependencies
import Model
import shared

struct RootPresenter {
    func prepareWindow() {
        prepareDependencies {
            $0.timetableUseCase = .init(
                load: {
                    try? await Task.sleep(nanoseconds: 1_000_000_000)
                    return Model.Timetable(sample: shared.Timetable.companion.fake().timetableItems.map(\.title.jaTitle).joined())
                }
            )
        }
    }
}
