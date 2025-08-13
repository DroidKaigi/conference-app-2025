import Foundation
import Model
import UseCase
import shared

struct EventMapUseCaseImpl {
    func load() async throws(LoadEventError) -> [Model.EventMapEvent] {
        // TODO: implement actual kmp connection
        return await Model.EventMapEvent.mockEvents
    }
}

extension Model.EventMapEvent {
    @MainActor static let mockEvents: [Model.EventMapEvent] = [
        EventMapEvent(
            id: "1",
            title: "Welcome Talk",
            description: "Opening ceremony and keynote presentation",
            room: Room(id: 1, name: .init(jaTitle: "roomF", enTitle: "roomF"), type: .roomF, sort: 0),
            moreDetailUrl: URL(string: "https://droidkaigi.jp/2025/welcome-talk"),
            message: "※こちらのイベントは時間が変更されました",
        ),
        EventMapEvent(
            id: "2",
            title: "Party",
            description: "Networking party with food and drinks",
            room: Room(id: 1, name: .init(jaTitle: "roomG", enTitle: "roomG"), type: .roomG, sort: 0),
            moreDetailUrl: URL(string: "https://droidkaigi.jp/2025/party"),
            message: nil,
        ),
        EventMapEvent(
            id: "3",
            title: "Ask the Speaker",
            description: "Q&A session with conference speakers",
            room: Room(id: 1, name: .init(jaTitle: "roomH", enTitle: "roomH"), type: .roomH, sort: 0),
            moreDetailUrl: URL(string: "https://droidkaigi.jp/2025/"),
            message: nil,
        ),
        EventMapEvent(
            id: "4",
            title: "Closing Ceremony",
            description: "Closing ceremony and awards presentation",
            room: Room(id: 1, name: .init(jaTitle: "roomI", enTitle: "roomI"), type: .roomI, sort: 0),
            moreDetailUrl: nil,
            message: nil,
        ),
    ]
}
