import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class HomePresenter {
    let timetable = TimetableProvider()
    let notificationProvider = NotificationProvider()

    init() {
        timetable.setNotificationProvider(notificationProvider)
    }

    func loadInitial() {
        notificationProvider.initialize()
        timetable.subscribeTimetableIfNeeded()
    }

    func timetableItemTapped(_ item: TimetableItemWithFavorite) {
        // print("Tapped: \(item.timetableItem.title)")
    }

    func searchTapped() {
        // print("Search tapped")
    }
}
