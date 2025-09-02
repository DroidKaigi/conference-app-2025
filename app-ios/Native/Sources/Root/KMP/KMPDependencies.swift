import Dependencies
import UseCase

enum KMPDependencies {
    static func prepareKMPDependencies(_ dependencyValues: inout DependencyValues) {
        let timetableUseCaseImpl = TimetableUseCaseImpl()
        dependencyValues.timetableUseCase = .init(
            load: timetableUseCaseImpl.load,
            toggleFavorite: timetableUseCaseImpl.toggleFavorite
        )

        let sponsorsUseCaseImpl = SponsorsUseCaseImpl()
        dependencyValues.sponsorsUseCase = .init(
            load: sponsorsUseCaseImpl.load
        )

        let staffUseCaseImpl = StaffUseCaseImpl()
        dependencyValues.staffUseCase = .init(
            load: staffUseCaseImpl.load
        )

        let contributorsUseCaseImpl = ContributorsUseCaseImpl()
        dependencyValues.contributorsUseCase = .init(
            load: contributorsUseCaseImpl.load
        )

        let eventMapUseCaseImpl = EventMapUseCaseImpl()
        dependencyValues.eventMapUseCase = .init(
            load: eventMapUseCaseImpl.load
        )

        let profileUseCaseImpl = ProfileUseCaseImpl()
        dependencyValues.profileUseCase = .init(
            load: profileUseCaseImpl.load,
            save: profileUseCaseImpl.save
        )

        // Store the notification use case implementation as a shared instance
        // so it can be accessed directly from RootScreen for navigation handler setup
        NotificationUseCaseManager.shared.setupImpl()
        let notificationUseCaseImpl = NotificationUseCaseManager.shared.impl
        dependencyValues.notificationUseCase = UseCase.NotificationUseCase(
            load: notificationUseCaseImpl.load,
            save: notificationUseCaseImpl.save,
            requestPermission: notificationUseCaseImpl.requestPermission,
            checkAuthorizationStatus: notificationUseCaseImpl.checkAuthorizationStatus,
            scheduleNotification: notificationUseCaseImpl.scheduleNotification(_:_:),
            cancelNotification: notificationUseCaseImpl.cancelNotification(_:),
            rescheduleAllNotifications: notificationUseCaseImpl.rescheduleAllNotifications(_:_:),
            cancelAllNotifications: notificationUseCaseImpl.cancelAllNotifications,
            setNavigationHandler: { @MainActor handler in
                NotificationUseCaseManager.shared.setNavigationHandler(handler)
            }
        )
    }
}
