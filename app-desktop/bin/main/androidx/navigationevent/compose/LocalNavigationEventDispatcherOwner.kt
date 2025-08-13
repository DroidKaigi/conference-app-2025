package androidx.navigationevent.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.navigationevent.NavigationEventDispatcherOwner

public object LocalNavigationEventDispatcherOwner {
    private val LocalNavigationEventDispatcherOwner =
        compositionLocalOf<NavigationEventDispatcherOwner?> { null }

    /**
     * Returns current composition local value for the owner or `null` if one has not been provided
     * nor is one available via [findViewTreeNavigationEventDispatcherOwner] on the current
     * `androidx.compose.ui.platform.LocalView`.
     */
    public val current: NavigationEventDispatcherOwner?
        @Composable
        get() = LocalNavigationEventDispatcherOwner.current!!

    /**
     * Associates a [LocalNavigationEventDispatcherOwner] key to a value in a call to
     * [CompositionLocalProvider].
     */
    public infix fun provides(
        navigationEventDispatcherOwner: NavigationEventDispatcherOwner
    ): ProvidedValue<NavigationEventDispatcherOwner?> {
        return LocalNavigationEventDispatcherOwner.provides(navigationEventDispatcherOwner)
    }
}
