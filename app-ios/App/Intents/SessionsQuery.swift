import AppIntents
import Foundation
import Model
import Root

private struct LoadSessions {
    func callAsFunction() async -> [SessionEntity] {
        let flow = KMPDependencyProvider.shared.appGraph.sessionsRepository
            .timetableFlow()
        let firstItem = await flow.first(where: { _ in true })
        guard let firstItem else { return [] }
        let timetable = Model.Timetable(from: firstItem)
        let sessions = timetable.timetableItems.map { item -> SessionEntity in
            .init(from: item)
        }
        return sessions
    }
}

struct SessionsQuery: EntityQuery {
    private let loadSessions = LoadSessions()

    func entities(for identifiers: [String]) async throws -> [SessionEntity] {
        let all = await loadSessions()
        return all.filter { identifiers.contains($0.id) }
    }

    func suggestedEntities() async throws -> [SessionEntity] {
        await loadSessions()
    }
}
