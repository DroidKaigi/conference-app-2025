package io.github.droidkaigi.confsched

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import dev.zacsweers.metro.createGraphFactory
import io.github.droidkaigi.confsched.app_desktop.AppDesktopRes
import io.github.droidkaigi.confsched.app_desktop.app_name
import io.github.droidkaigi.confsched.app_desktop.ic_app
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

fun main() = application {
    val graphFactory = createGraphFactory<JvmAppGraph.Factory>()
    val graph: JvmAppGraph = graphFactory.createJvmAppGraph(
        licensesJsonReader = JvmLicensesJsonReader(),
        useProductionApi = false,
    )

    // Replace the taskbar icon from Duke when launched via the Gradle command.
    applyRuntimeDockIconIfDev()

    Window(
        // Title for window title
        title = stringResource(AppDesktopRes.string.app_name),
        // Icon for the title bar/task switching UI of that window
        icon = painterResource(AppDesktopRes.drawable.ic_app),
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp,
        ),
    ) {
        window.minimumSize = Dimension(500, 450)

        // workaround for java.lang.NullPointerException at androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner.getCurrent
        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides FakeNavigationEventDispatcherOwner()) {
            with(graph) {
                KaigiApp()
            }
        }
    }
}

private class FakeNavigationEventDispatcherOwner : NavigationEventDispatcherOwner {
    override val navigationEventDispatcher: NavigationEventDispatcher get() = NavigationEventDispatcher()
}
