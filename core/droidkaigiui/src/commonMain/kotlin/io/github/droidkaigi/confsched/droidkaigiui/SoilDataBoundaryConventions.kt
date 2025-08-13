package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultErrorFallback
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallback
import soil.plant.compose.reacty.ErrorBoundaryContext
import soil.query.core.DataModel

@Composable
context(_: ScreenContext)
fun <T> SoilDataBoundary(
    state: DataModel<T>,
    fallback: SoilDataBoundaryFallback = SoilDataBoundaryDefaults.simpleFallback(),
    content: @Composable (T) -> Unit,
) {
    SoilDataBoundary(
        state = state,
        suspenseFallback = { SuspenseFallback(fallback) },
        errorFallback = { ErrorFallback(it, fallback) },
        content = content,
    )
}

@Composable
context(_: ScreenContext)
fun <T1, T2> SoilDataBoundary(
    state1: DataModel<T1>,
    state2: DataModel<T2>,
    fallback: SoilDataBoundaryFallback = SoilDataBoundaryDefaults.simpleFallback(),
    content: @Composable (T1, T2) -> Unit,
) {
    SoilDataBoundary(
        state1 = state1,
        state2 = state2,
        suspenseFallback = { SuspenseFallback(fallback) },
        errorFallback = { ErrorFallback(it, fallback) },
        content = content,
    )
}

@Composable
context(_: ScreenContext)
fun <T1, T2, T3> SoilDataBoundary(
    state1: DataModel<T1>,
    state2: DataModel<T2>,
    state3: DataModel<T3>,
    fallback: SoilDataBoundaryFallback = SoilDataBoundaryDefaults.simpleFallback(),
    content: @Composable (T1, T2, T3) -> Unit,
) {
    SoilDataBoundary(
        state1 = state1,
        state2 = state2,
        state3 = state3,
        suspenseFallback = { SuspenseFallback(fallback) },
        errorFallback = { ErrorFallback(it, fallback) },
        content = content,
    )
}

context(_: ScreenContext)
@Composable
private fun SuspenseFallback(
    fallback: SoilDataBoundaryFallback,
) {
    when (fallback) {
        is SoilDataBoundaryFallback.NoAppBar -> {
            DefaultSuspenseFallback()
        }

        is SoilDataBoundaryFallback.AppBar -> {
            DefaultSuspenseFallback(
                title = fallback.title,
                onBackClick = fallback.onBackClick,
            )
        }

        is SoilDataBoundaryFallback.Custom -> {
            fallback.suspenseFallback()
        }
    }
}

context(_: ScreenContext)
@Composable
private fun ErrorFallback(
    errorBoundaryContext: ErrorBoundaryContext,
    fallback: SoilDataBoundaryFallback,
) {
    when (fallback) {
        is SoilDataBoundaryFallback.NoAppBar -> {
            DefaultErrorFallback(
                errorBoundaryContext = errorBoundaryContext,
                title = null,
            )
        }

        is SoilDataBoundaryFallback.AppBar -> {
            DefaultErrorFallback(
                errorBoundaryContext = errorBoundaryContext,
                title = fallback.title,
                onBackClick = fallback.onBackClick,
            )
        }

        is SoilDataBoundaryFallback.Custom -> {
            fallback.errorFallback(errorBoundaryContext)
        }
    }
}
