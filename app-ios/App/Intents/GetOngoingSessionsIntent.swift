import AppIntents
import Foundation

struct GetOngoingSessionsIntent: AppIntent {
    static var title: LocalizedStringResource = "Get ongoing sessions"

    @MainActor
    func perform() async throws -> some IntentResult & ReturnsValue<[SessionEntity]> & ProvidesDialog {
        let sessions: [SessionEntity] = try await SessionsQuery().suggestedEntities()
        let now = Date.now
        let ongoingSessions = sessions.filter { $0.startTime <= now && now <= $0.endTime }

        let dialog: IntentDialog
        if ongoingSessions.isEmpty {
            dialog = IntentDialog("Not found ongoing session")
        } else {
            let titles = ongoingSessions.map { $0.title }.joined(separator: ", ")
            dialog = IntentDialog("Ongoing sessions: \(titles)")
        }
        return .result(value: ongoingSessions, dialog: dialog)
    }
}
