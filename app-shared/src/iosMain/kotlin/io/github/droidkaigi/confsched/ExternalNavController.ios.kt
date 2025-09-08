package io.github.droidkaigi.confsched

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.utils.toNSData
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.useContents
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.toNSDate
import platform.CoreGraphics.CGRectMake
import platform.EventKit.EKEntityType
import platform.EventKit.EKEvent
import platform.EventKit.EKEventStore
import platform.EventKit.EKSpan
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UINavigationController
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UITabBarController
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController

@Composable
actual fun rememberExternalNavController(): ExternalNavController {
    return IosExternalNavController()
}

internal class IosExternalNavController : ExternalNavController {
    override fun navigate(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            println("Failed to navigate to URL (invalid URL): $url")
            return
        }

        if (!UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            println("Cannot open URL (scheme not allowed or no handler): $url")
            return
        }

        UIApplication.sharedApplication.openURL(
            url = nsUrl,
            options = emptyMap<Any?, Any?>(),
            completionHandler = { success ->
                if (!success) {
                    println("Failed to open URL via UIApplication.open(_:options:completionHandler:): $url")
                }
            },
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun navigateToCalendarRegistration(timetableItem: TimetableItem) {
        val eventStore = EKEventStore()

        eventStore.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent) { granted, error ->
            if (granted) {
                val event = EKEvent.eventWithEventStore(eventStore)
                event.startDate = timetableItem.startsAt.toNSDate()
                event.endDate = timetableItem.endsAt.toNSDate()
                event.title = timetableItem.title.currentLangTitle
                event.notes = timetableItem.url
                event.location = timetableItem.room.name.currentLangTitle
                event.calendar = eventStore.defaultCalendarForNewEvents

                memScoped {
                    val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                    val success = eventStore.saveEvent(event, span = EKSpan.EKSpanThisEvent, error = errorPtr.ptr)
                    if (success) {
                        println("Event added to calendar: ${event.title}")
                    } else {
                        println("Failed to add event to calendar: ${error?.localizedDescription ?: "Unknown error"}")
                    }
                }
            } else {
                println("Access to calendar not granted: $error")
            }
        }
    }

    @OptIn(BetaInteropApi::class)
    override fun onShareClick(timetableItem: TimetableItem) {
        val text = "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.formattedMonthAndDayString} " +
            "${timetableItem.startsTimeString} - ${timetableItem.endsTimeString}\n" +
            "${timetableItem.title.currentLangTitle}\n" +
            timetableItem.url

        share(listOf(NSString.create(text)))
    }

    override fun onShareProfileCardClick(shareText: String, imageBitmap: ImageBitmap) {
        MainScope().launch {
            val image = UIImage(data = imageBitmap.encodeToByteArray().toNSData())
            share(listOf(NSString.create(shareText), image))
        }
    }

    private fun share(items: List<*>) {
        val activityViewController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null,
        )
        presentAsPopoverSafely(activityViewController)
    }

    /**
     * Presents the given [UIActivityViewController] safely.
     *
     * - On iPad, `UIActivityViewController` is presented as a popover,
     *   which requires an anchor (`sourceView`+`sourceRect` or `barButtonItem`)
     *   to be set. Without it, a runtime exception will occur:
     *   "UIPopoverPresentationController should have a non-nil sourceView or barButtonItem".
     *
     * This method sets the popover's source view and rect to the center of the presenter view.
     * You can adjust [sourceRect] as needed to change the popover position.
     *
     * @see <a href="https://developer.apple.com/documentation/uikit/uiactivityviewcontroller">
     *      UIActivityViewController – Apple Developer Documentation</a>
     * @see <a href="https://developer.apple.com/documentation/uikit/uipopoverpresentationcontroller">
     *      UIPopoverPresentationController – Apple Developer Documentation</a>
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun presentAsPopoverSafely(activityVC: UIActivityViewController) {
        val presenter = findPresentingViewController() ?: return

        if (UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad) {
            activityVC.popoverPresentationController?.let { pop ->
                pop.sourceView = presenter.view
                presenter.view.bounds.useContents {
                    // Currently, the center of the presenter.view screen is used as the anchor point, but this can be changed as needed.
                    pop.sourceRect = CGRectMake(size.width / 2.0, size.height / 2.0, 0.0, 0.0)
                }
                pop.permittedArrowDirections = 0u
            }
        }

        presenter.presentViewController(activityVC, true, null)
    }

    /**
     * Finds the most appropriate [UIViewController] to present from.
     *
     * - Since iOS 13, apps can have multiple scenes (multi-window).
     *   `UIApplication.keyWindow` is deprecated and may return `nil`.
     * - Instead, this method searches for a foreground-active [UIWindowScene],
     *   retrieves its key window, and then resolves the top-most visible view controller.
     *
     * @see <a href="https://developer.apple.com/documentation/uikit/uiapplication/keywindow">
     *      UIApplication.keyWindow (deprecated)</a>
     * @see <a href="https://developer.apple.com/documentation/uikit/uiwindowscene">
     *      UIWindowScene – Apple Developer Documentation</a>
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun findPresentingViewController(): UIViewController? {
        val scenes = UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .filter { it.activationState == UISceneActivationStateForegroundActive }

        val window: UIWindow? = scenes.firstOrNull()
            ?.windows
            ?.filterIsInstance<UIWindow>()
            ?.firstOrNull { it.keyWindow }
            ?: UIApplication.sharedApplication.windows
                .filterIsInstance<UIWindow>()
                .firstOrNull { it.keyWindow }
            ?: UIApplication.sharedApplication.windows
                .filterIsInstance<UIWindow>()
                .firstOrNull()

        return topMostViewController(window?.rootViewController)
    }

    /**
     * Traverses the presented view controller chain to find the top-most (visible) view controller.
     *
     * - UIKit maintains a single chain of presented view controllers (no cycles).
     * - Using [generateSequence], we walk down the chain until the last presented VC.
     *
     * @see <a href="https://developer.apple.com/documentation/uikit/uiviewcontroller/1621380-presentedviewcontroller">
     *      UIViewController.presentedViewController</a>
     */
    private fun topMostViewController(root: UIViewController?): UIViewController? {
        val start = normalize(root) ?: return null
        return generateSequence(start) { current ->
            val presented = current.presentedViewController ?: return@generateSequence null
            normalize(presented)
        }.lastOrNull()
    }

    /**
     * Normalizes special container controllers to return the actual visible child.
     *
     * - [UINavigationController] → returns its [visibleViewController] (top of the navigation stack).
     * - [UITabBarController] → returns its [selectedViewController] (current tab).
     * - Otherwise returns the given controller itself.
     *
     * This ensures we always deal with the screen the user actually sees,
     * not the container itself.
     *
     * @see <a href="https://developer.apple.com/documentation/uikit/uinavigationcontroller">
     *      UINavigationController – Apple Developer Documentation</a>
     * @see <a href="https://developer.apple.com/documentation/uikit/uitabbarcontroller">
     *      UITabBarController – Apple Developer Documentation</a>
     */
    private fun normalize(vc: UIViewController?): UIViewController? = when (vc) {
        is UINavigationController -> vc.visibleViewController ?: vc
        is UITabBarController -> vc.selectedViewController ?: vc
        else -> vc
    }
}
