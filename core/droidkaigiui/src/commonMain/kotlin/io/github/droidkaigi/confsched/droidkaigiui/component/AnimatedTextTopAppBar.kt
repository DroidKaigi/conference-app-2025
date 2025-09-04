package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTextTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = AnimatedTextTopAppBarDefaults.windowInsets(),
    colors: TopAppBarColors? = null,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    scrolledTextColor: Color = MaterialTheme.colorScheme.onSurface,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val transitionFraction by remember(scrollBehavior) {
        derivedStateOf {
            scrollBehavior?.state?.overlappedFraction?.coerceIn(0f, 1f) ?: 0f
        }
    }
    val density = LocalDensity.current.density
    var navigationIconWidthDp by remember { mutableStateOf(0f) }
    val currentTextColor = if (transitionFraction > 0f) scrolledTextColor else textColor

    val widthSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val resolvedColors: TopAppBarColors =
        colors ?: TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = when (widthSizeClass) {
                WindowWidthSizeClass.MEDIUM, WindowWidthSizeClass.EXPANDED -> MaterialTheme.colorScheme.background
                else -> MaterialTheme.colorScheme.surfaceContainer
            },
        )

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    autoSize = TextAutoSize.StepBased(
                        maxFontSize = 28.sp,
                    ),
                    color = currentTextColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = 1f - transitionFraction
                        },
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = title,
                    autoSize = TextAutoSize.StepBased(
                        maxFontSize = 16.sp,
                    ),
                    color = currentTextColor,
                    modifier = Modifier
                        // Ensures the title appears centered when a navigation icon is present.
                        // Note: The width of actions is currently not considered.
                        .padding(end = navigationIconWidthDp.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .graphicsLayer {
                            alpha = transitionFraction
                        },
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            onBackClick?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier.onSizeChanged {
                        navigationIconWidthDp = it.width / density
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = actions,
        windowInsets = windowInsets,
        colors = resolvedColors,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior.resetScroll() {
    this.state.heightOffset = 0f
    this.state.contentOffset = 0f
}

object AnimatedTextTopAppBarDefaults {
    @Composable
    fun windowInsets() = WindowInsets.displayCutout.union(WindowInsets.systemBars).only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
    )
}
