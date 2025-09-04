import Foundation
import UseCase

public final class NotificationUseCaseManager: @unchecked Sendable {
    public static let shared = NotificationUseCaseManager()

    private var _impl: NotificationUseCaseImpl?

    private init() {}

    public var impl: NotificationUseCaseImpl {
        guard let impl = _impl else {
            fatalError("NotificationUseCaseImpl not set up. Call setupImpl() first.")
        }
        return impl
    }

    public func setupImpl() {
        if _impl == nil {
            _impl = NotificationUseCaseImpl()
        }
    }

    @MainActor
    public func setNavigationHandler(_ handler: NotificationNavigationHandler?) {
        impl.setNavigationHandler(handler)
    }
}
