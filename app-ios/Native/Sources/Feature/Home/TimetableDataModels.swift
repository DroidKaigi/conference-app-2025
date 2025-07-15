import Foundation
import Model
import SwiftUI

extension Room {
    var color: Color {
        switch self {
        case .roomA: return .blue
        case .roomB: return .green
        case .roomC: return .orange
        case .roomD: return .purple
        }
    }
}

extension TimetableItem {
    var startsTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: startsAt)
    }

    var endsTimeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: endsAt)
    }
}