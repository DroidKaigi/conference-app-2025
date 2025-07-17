import Foundation

public enum TimetableSessionType: String, CaseIterable, Sendable {
    case welcome = "WELCOME"
    case regular = "REGULAR"
    case lightning = "LIGHTNING"
    case fireside = "FIRESIDE"
    case closing = "CLOSING"
    case lunch = "LUNCH"
    case meetup = "MEETUP"
    case other = "OTHER"
}