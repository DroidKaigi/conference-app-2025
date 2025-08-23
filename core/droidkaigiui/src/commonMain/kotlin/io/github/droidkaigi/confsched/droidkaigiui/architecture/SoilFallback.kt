@file:Suppress("UNUSED")

package io.github.droidkaigi.confsched.droidkaigiui.architecture

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar

sealed interface SoilFallback {
    val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit
    val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit
}

object SoilFallbackDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun appBar(
        title: String,
        colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors().copy(
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onBackClick: (() -> Unit)? = null,
        appBarSize: AppBarSize = AppBarSize.Default,
        // Allowing WindowInsets to be overridden to prevent layout jump/glitches
        // when navigating between screens with and without a bottom navigation bar.
        windowInsets: WindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
        contentBackground: (@Composable (innerPadding: PaddingValues) -> Unit)? = null,
    ): SoilFallback = AppBar(
        title = title,
        colors = colors,
        onBackClick = onBackClick,
        size = appBarSize,
        windowInsets = windowInsets,
        contentBackground = contentBackground,
    )

    fun default(): SoilFallback = Default

    fun custom(
        suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit,
        errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit,
    ): SoilFallback = Custom(
        suspenseFallback = suspenseFallback,
        errorFallback = errorFallback,
    )
}

private object Default : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit
        get() = { DefaultSuspenseFallbackContent() }
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit
        get() = { DefaultErrorFallbackContent() }
}

@OptIn(ExperimentalMaterial3Api::class)
private class AppBar(
    val title: String,
    val colors: TopAppBarColors,
    val onBackClick: (() -> Unit)?,
    val size: AppBarSize,
    val windowInsets: WindowInsets,
    val contentBackground: (@Composable (innerPadding: PaddingValues) -> Unit)?,
) : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit = {
        AppBarFallbackScaffold(
            title = title,
            onBackClick = onBackClick,
            appBarSize = size,
            appBarColors = colors,
            windowInsets = windowInsets,
        ) { innerPadding ->
            contentBackground?.invoke(innerPadding)
            DefaultSuspenseFallbackContent(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit = {
        AppBarFallbackScaffold(
            title = title,
            onBackClick = onBackClick,
            appBarSize = size,
            appBarColors = colors,
            windowInsets = windowInsets,
        ) { innerPadding ->
            contentBackground?.invoke(innerPadding)
            DefaultErrorFallbackContent(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

private class Custom(
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit,
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit,
) : SoilFallback
