import EventKit
import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class TimetableDetailPresenter {
    struct Toast {
        var message: String
        var action: (title: String, handler: () -> Void)? = nil
    }

    var timetableItem: TimetableItemWithFavorite
    private let timetableProvider = TimetableProvider()
    private let eventStore: EKEventStore

    var isFavorite: Bool {
        timetableItem.isFavorited
    }

    var toast: Toast?

    init(timetableItem: TimetableItemWithFavorite) {
        self.timetableItem = timetableItem
        self.eventStore = EKEventStore()
    }

    func toggleFavorite() {
        timetableProvider.toggleFavorite(timetableItem)
        timetableItem.isFavorited.toggle()
        if timetableItem.isFavorited {
            toast = Toast(
                message: String(localized: "Added to bookmarks", bundle: .module),
                action: (String(localized: "View list", bundle: .module), navigateToFavorite))
        } else {
            toast = Toast(message: String(localized: "Removed from bookmarks", bundle: .module))
        }
    }

    func addToCalendar() {
        let status = EKEventStore.authorizationStatus(for: .event)

        switch status {
        case .notDetermined:
            eventStore.requestFullAccessToEvents { [weak self] granted, _ in
                guard let self = self else { return }
                Task { @MainActor in
                    if granted {
                        self.createCalendarEvent()
                    } else {
                        self.toast = Toast(message: String(localized: "Calendar access not granted", bundle: .module))
                    }
                }
            }
        case .fullAccess, .writeOnly:
            createCalendarEvent()
        case .restricted, .denied:
            self.toast = Toast(message: "カレンダーへのアクセスが許可されていません")
        @unknown default:
            break
        }
    }

    private func createCalendarEvent() {
        let event = EKEvent(eventStore: eventStore)
        event.title = timetableItem.timetableItem.title.currentLangTitle
        event.startDate = timetableItem.timetableItem.startsAt
        event.endDate = timetableItem.timetableItem.endsAt
        event.location = timetableItem.timetableItem.room.name.currentLangTitle

        if let session = timetableItem.timetableItem as? TimetableItemSession {
            event.notes = session.description.currentLangTitle
        } else if let special = timetableItem.timetableItem as? TimetableItemSpecial {
            event.notes = special.description.currentLangTitle
        }

        event.calendar = eventStore.defaultCalendarForNewEvents

        do {
            try eventStore.save(event, span: .thisEvent)
            toast = Toast(message: String(localized: "Added to calendar", bundle: .module))
        } catch {
            toast = Toast(message: String(localized: "Failed to add to calendar", bundle: .module))
        }
    }

    func shareSession() -> URL? {
        guard let url = URL(string: "https://droidkaigi.jp/2025/timetable/\(timetableItem.timetableItem.id.value)")
        else { return nil }
        return url
    }

    private func navigateToFavorite() {
        // TODO: Add logic to navigate to favorite screen
    }

    var isCancelledSession: Bool {
        if let session = timetableItem.timetableItem as? TimetableItemSession {
            return session.message != nil
        }
        return false
    }
}
