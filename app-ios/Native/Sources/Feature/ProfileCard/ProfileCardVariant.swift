import Model

enum ProfileCardType: String {
    case night
    case day
}

enum ProfileCardShape: String {
    case pill
    case diamond
    case flower
}

extension Model.ProfileCardVariant {
    var type: ProfileCardType {
        switch self {
        case .nightPill,
            .nightDiamond,
            .nightFlower:
            return .night
        case .dayPill,
            .dayDiamond,
            .dayFlower:
            return .day
        }
    }

    var shape: ProfileCardShape {
        switch self {
        case .nightPill,
            .dayPill:
            return .pill
        case .nightDiamond,
            .dayDiamond:
            return .diamond
        case .nightFlower,
            .dayFlower:
            return .flower
        }
    }
}
