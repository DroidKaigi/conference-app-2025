@preconcurrency import BackgroundTasks
import Dependencies
import Model
import SwiftUI
import UseCase
@preconcurrency import UserNotifications
import os.log

public enum BackgroundTaskHandler {
    private static let taskIdentifier = "io.github.droidkaigi.dk2025.notification-refresh"

    public static func registerBackgroundTasks() {
        if #available(iOS 13.0, *) {
            BGTaskScheduler.shared.register(
                forTaskWithIdentifier: taskIdentifier,
                using: nil
            ) { task in
                guard let refreshTask = task as? BGAppRefreshTask else {
                    return
                }

                // Handle the background refresh task properly
                // Execute background task handling asynchronously without creating a new Task
                // to avoid data race warnings. The BGTaskScheduler callback is already designed
                // to handle concurrent operations safely.
                handleBackgroundRefreshTaskAsync(refreshTask)
            }
        }
    }

    @available(iOS 13.0, *)
    private static func handleBackgroundRefreshTaskAsync(_ task: BGAppRefreshTask) {
        // BGAppRefreshTask is managed by the system and is safe to use across task boundaries
        // We explicitly mark this as @Sendable since the task object is thread-safe in this context
        Task.detached(priority: .background) { @Sendable in
            // BGAppRefreshTask is safe to use here as it's managed by the system's background task scheduler
            await handleBackgroundRefreshTask(task)
        }
    }

    @available(iOS 13.0, *)
    public static func handleBackgroundRefreshTask(_ task: BGAppRefreshTask) async {
        // Set completion handler - default to success
        var taskCompleted = false
        defer {
            if !taskCompleted {
                task.setTaskCompleted(success: true)
            }
        }

        // Get notification settings
        @Dependency(\.notificationUseCase) var notificationUseCase
        let settings = await notificationUseCase.load()

        guard settings.isEnabled else {
            task.setTaskCompleted(success: true)
            taskCompleted = true
            return
        }

        let authStatus = await notificationUseCase.checkAuthorizationStatus()
        guard authStatus == .authorized else {
            task.setTaskCompleted(success: true)
            taskCompleted = true
            return
        }

        // Refresh notifications with latest data
        await updateNotificationsWithLatestData(settings: settings)

        task.setTaskCompleted(success: true)
        taskCompleted = true
    }

    public static func scheduleBackgroundRefreshIfNeeded() async {
        // Check if notifications are enabled
        @Dependency(\.notificationUseCase) var notificationUseCase
        let settings = await notificationUseCase.load()

        guard settings.isEnabled else {
            return
        }

        let authStatus = await notificationUseCase.checkAuthorizationStatus()
        guard authStatus == .authorized else {
            return
        }

        // Check Background App Refresh availability
        if #available(iOS 13.0, *) {
            let backgroundRefreshStatus = await UIApplication.shared.backgroundRefreshStatus

            switch backgroundRefreshStatus {
            case .denied:
                return
            case .restricted:
                return
            case .available:
                break
            @unknown default:
                print("Unknown background refresh status")
            }

            // Schedule background task
            let request = BGAppRefreshTaskRequest(identifier: taskIdentifier)
            request.earliestBeginDate = Date(timeIntervalSinceNow: 15 * 60)  // 15 minutes from now

            do {
                try BGTaskScheduler.shared.submit(request)
            } catch {
                handleBackgroundTaskSchedulingError(error)
            }
        }
    }

    private static func handleBackgroundTaskSchedulingError(_ error: Error) {
        if let bgError = error as? NSError, bgError.domain == "BGTaskSchedulerErrorDomain" {
            switch bgError.code {
            case 1:  // BGTaskSchedulerErrorCodeUnavailable
                return
            case 2:  // BGTaskSchedulerErrorCodeTooManyPendingTaskRequests
                print("Too many pending background tasks: \(error.localizedDescription)")
            case 3:  // BGTaskSchedulerErrorCodeNotPermitted
                return
            default:
                print("Failed to schedule background refresh: \(error.localizedDescription)")
            }
        } else {
            print("Failed to schedule background refresh: \(error.localizedDescription)")
        }
    }

    private static func updateNotificationsWithLatestData(settings: NotificationSettings) async {
        @Dependency(\.timetableUseCase) var timetableUseCase
        @Dependency(\.notificationUseCase) var notificationUseCase

        // Get current timetable data
        let timetableSequence = timetableUseCase.load()

        guard
            let timetable = await timetableSequence.first(where: { @Sendable _ in
                true
            })
        else {
            return
        }

        // Convert to TimetableItemWithFavorite
        let allItems = timetable.timetableItems.map { item in
            let isFavorited = timetable.bookmarks.contains(item.id)
            return TimetableItemWithFavorite(timetableItem: item, isFavorited: isFavorited)
        }

        let favoriteItems = allItems.filter { $0.isFavorited }

        // Update notifications
        await notificationUseCase.rescheduleAllNotifications(allItems, settings)
    }
}
