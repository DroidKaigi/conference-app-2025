package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultErrorFallback
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallback
import kotlinx.coroutines.launch
import soil.plant.compose.reacty.Await
import soil.plant.compose.reacty.ErrorBoundary
import soil.plant.compose.reacty.ErrorBoundaryContext
import soil.plant.compose.reacty.Suspense
import soil.query.compose.QueryObject
import soil.query.compose.SubscriptionObject
import soil.query.core.DataModel

context(_: ScreenContext)
@Composable
fun <T> SoilDataBoundary(
    state: DataModel<T>,
    modifier: Modifier = Modifier,
    errorFallback: @Composable BoxScope.(ErrorBoundaryContext) -> Unit = {
        DefaultErrorFallback(errorBoundaryContext = it)
    },
    suspenseFallback: @Composable BoxScope.() -> Unit = {
        DefaultSuspenseFallback()
    },
    content: @Composable (T) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    ErrorBoundary(
        fallback = errorFallback,
        onReset = {
            coroutineScope.launch {
                state.performResetIfNeeded()
            }
        },
        modifier = modifier,
    ) {
        Suspense(fallback = suspenseFallback) {
            Await(state = state, content = content)
        }
    }
}

context(_: ScreenContext)
@Composable
fun <T1, T2> SoilDataBoundary(
    state1: DataModel<T1>,
    state2: DataModel<T2>,
    modifier: Modifier = Modifier,
    errorFallback: @Composable BoxScope.(ErrorBoundaryContext) -> Unit = {
        DefaultErrorFallback(errorBoundaryContext = it)
    },
    suspenseFallback: @Composable BoxScope.() -> Unit = {
        DefaultSuspenseFallback()
    },
    content: @Composable (T1, T2) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    ErrorBoundary(
        fallback = errorFallback,
        onReset = {
            coroutineScope.launch {
                state1.performResetIfNeeded()
                state2.performResetIfNeeded()
            }
        },
        modifier = modifier,
    ) {
        Suspense(fallback = suspenseFallback) {
            Await(
                state1 = state1,
                state2 = state2,
                content = content
            )
        }
    }
}

context(_: ScreenContext)
@Composable
fun <T1, T2, T3> SoilDataBoundary(
    state1: DataModel<T1>,
    state2: DataModel<T2>,
    state3: DataModel<T3>,
    modifier: Modifier = Modifier,
    errorFallback: @Composable BoxScope.(ErrorBoundaryContext) -> Unit = {
        DefaultErrorFallback(errorBoundaryContext = it)
    },
    suspenseFallback: @Composable BoxScope.() -> Unit = {
        DefaultSuspenseFallback()
    },
    content: @Composable (T1, T2, T3) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    ErrorBoundary(
        fallback = errorFallback,
        onReset = {
            coroutineScope.launch {
                state1.performResetIfNeeded()
                state2.performResetIfNeeded()
                state3.performResetIfNeeded()
            }
        },
        modifier = modifier,
    ) {
        Suspense(fallback = suspenseFallback) {
            Await(
                state1 = state1,
                state2 = state2,
                state3 = state3,
                content = content
            )
        }
    }
}

private suspend fun <T> DataModel<T>.performResetIfNeeded() {
    when (this) {
        is QueryObject<T> -> this.error?.let { this.refresh() }
        is SubscriptionObject<T> -> this.error?.let { this.reset() }
    }
}
