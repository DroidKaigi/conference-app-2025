import SwiftUI

enum ScenePhaseHandler {
    static func handle(_ scenePhase: ScenePhase) {
        switch scenePhase {
        case .active:
            break

        case .inactive:
            break

        case .background:
            break

        @unknown default:
            break
        }
    }
}
