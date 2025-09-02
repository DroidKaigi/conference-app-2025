import AppIntents
import Foundation
import Model
import Root

struct SessionEntity: AppEntity {
    static var typeDisplayRepresentation: TypeDisplayRepresentation {
        "Session"
    }

    var id: String
    var title: String
    var startTime: Date
    var endTime: Date
    var room: String

    var displayRepresentation: DisplayRepresentation {
        DisplayRepresentation(
            title: "session title: \(title)",
            subtitle:
                "\(room) | \(startTime.formatted(date: .omitted, time: .shortened)) - \(endTime.formatted(date: .omitted, time: .shortened))"
        )
    }

    static var defaultQuery = SessionsQuery()

    init(from session: any Model.TimetableItem) {
        self.id = session.id.value
        self.title = session.title.jaTitle.isEmpty ? session.title.enTitle : session.title.jaTitle
        self.startTime = session.startsAt
        self.endTime = session.endsAt
        self.room = session.room.name.jaTitle.isEmpty ? session.room.name.enTitle : session.room.name.jaTitle
    }
}
