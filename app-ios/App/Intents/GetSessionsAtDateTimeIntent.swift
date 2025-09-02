import AppIntents
import Foundation

struct GetSessionsAtDateTimeIntent: AppIntent {
    static var title: LocalizedStringResource = "Get sessions at specific date and time"

    @Parameter(title: "date")
    var date: Date

    @MainActor
    func perform() async throws -> some IntentResult & ReturnsValue<[SessionEntity]> & ProvidesDialog {
        let sessions: [SessionEntity] = try await SessionsQuery().suggestedEntities()

        let targetSessions = sessions.filter { $0.startTime <= date && date <= $0.endTime }

        let dialog: IntentDialog
        if targetSessions.isEmpty {
            dialog = IntentDialog("Not found sessions")
        } else {
            let titles = targetSessions.map { $0.title }.joined(separator: ", ")
            dialog = IntentDialog("Session titles: \(titles)")
        }
        return .result(value: targetSessions, dialog: dialog)
    }
}
