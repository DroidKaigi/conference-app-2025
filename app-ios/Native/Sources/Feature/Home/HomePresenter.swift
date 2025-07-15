import Model
import Observation
import Presentation
import Foundation

@MainActor
@Observable
final class HomePresenter {
    let timetable = TimetableProvider()

    init() {}

    func loadInitial() async {
        await timetable.fetchTimetable()
    }
    
    func timetableItemTapped(_ item: TimetableItemWithFavorite) {
        print("Tapped: \(item.timetableItem.title)")
    }
    
    func searchTapped() {
        print("Search tapped")
    }
}
