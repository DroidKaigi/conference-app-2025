import Root
import UIKit
import UserNotifications
import os.log

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    private var launchNotificationResponse: UNNotificationResponse?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // Set AppDelegate as the notification center delegate to handle notifications
        // This ensures we can capture notification taps even when app is terminated
        let center = UNUserNotificationCenter.current()
        center.delegate = self

        return true
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo

        // Handle notification tap (both when app is running and when launched from terminated state)
        if let itemId = userInfo["itemId"] as? String {
            switch response.actionIdentifier {
            case "VIEW_SESSION", UNNotificationDefaultActionIdentifier:
                // Store the notification response for processing when RootScreen is ready
                NotificationLaunchHandler.shared.storeLaunchNotification(userInfo)

                // Also store in UserDefaults as fallback
                UserDefaults.standard.set(itemId, forKey: "pending_notification_item_id")

            default:
                print("Unknown notification action: \(response.actionIdentifier)")
            }
        } else {
            print("Notification response missing itemId")
        }

        completionHandler()
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        // Show notification banner, sound, and badge when app is in foreground
        completionHandler([.banner, .sound, .badge])
    }
}
