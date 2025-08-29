@preconcurrency import shared

public struct KMPDependencyProvider: Sendable {
    public static let shared: KMPDependencyProvider = .init()

    public let appGraph: shared.IosAppGraph

    private init() {
        self.appGraph = IosAppGraphKt.createIosAppGraph()
    }
}
