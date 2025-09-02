@preconcurrency import shared

public struct KMPDependencyProvider: Sendable {
    public static let shared: KMPDependencyProvider = .init()

    public let appGraph: shared.IosAppGraph

    private init() {
        #if DEBUG
        // Use staging API for debug builds
        self.appGraph = IosAppGraphKt.createIosAppGraph(useProductionApiBaseUrl: false)
        #else
        // Use production API for release builds
        self.appGraph = IosAppGraphKt.createIosAppGraph(useProductionApiBaseUrl: true)
        #endif
    }
}
