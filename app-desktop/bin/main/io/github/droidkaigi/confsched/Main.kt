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

fun main() = application {
    val graphFactory = createGraphFactory<JvmAppGraph.Factory>()
    val graph: JvmAppGraph = graphFactory.createJvmAppGraph(JvmLicensesJsonReader())

    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp,
        )
    ) {
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
