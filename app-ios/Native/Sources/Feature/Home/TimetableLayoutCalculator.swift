import Foundation
import Model

struct TimetableLayoutConfig {
    let heightOfMinute: CGFloat = 5
    let roomWidth: CGFloat = 192
    let roomSpacing: CGFloat = 4
    let timeAxisWidth: CGFloat = 40
}

struct TimetableLayoutCalculator {
    let config: TimetableLayoutConfig
    let rooms: [Room]
    let dayStart: Date?
    let dayEnd: Date?

    func timetableWidth() -> CGFloat {
        guard !rooms.isEmpty else { return 0 }
        let columns = CGFloat(rooms.count)
        return columns * config.roomWidth + (columns - 1) * config.roomSpacing
    }

    func timetableHeight() -> CGFloat {
        if let dayStart = dayStart, let dayEnd = dayEnd {
            let minutes = dayEnd.timeIntervalSince(dayStart) / 60
            return CGFloat(minutes) * config.heightOfMinute
        }
        return 0
    }

    func offsetX(for item: any TimetableItem) -> CGFloat {
        if item.isLunchTime() { return 0 }
        guard let idx = rooms.firstIndex(where: { $0.id == item.room.id }) else { return 0 }
        return CGFloat(idx) * (config.roomWidth + config.roomSpacing)
    }

    func offsetY(for date: Date, base: Date) -> CGFloat {
        let minutes = date.timeIntervalSince(base) / 60.0
        return CGFloat(minutes) * config.heightOfMinute
    }

    func sessionWidth(for item: any TimetableItem) -> CGFloat {
        item.isLunchTime() ? timetableWidth() : config.roomWidth
    }

    func sessionHeight(for item: any TimetableItem) -> CGFloat {
        let minutes = item.endsAt.timeIntervalSince(item.startsAt) / 60.0
        return CGFloat(minutes) * config.heightOfMinute
    }
}
