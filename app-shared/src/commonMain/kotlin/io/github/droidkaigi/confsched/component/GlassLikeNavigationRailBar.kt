package io.github.droidkaigi.confsched.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.rememberHazeState
import io.github.droidkaigi.confsched.common.graphics.isBlurSupported
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GlassLikeNavigationRailBar(
    hazeState: HazeState,
    currentTab: MainScreenTab,
    onTabSelected: (MainScreenTab) -> Unit,
    animatedSelectedTabIndex: Float,
    animatedColor: Color,
    modifier: Modifier = Modifier,
) {
    val navigationItemsContentPadding = PaddingValues(vertical = 12.dp)

    Box(
        modifier = modifier
            .size(width = 64.dp, height = 420.dp)
            .border(
                width = Dp.Hairline,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color.White.copy(alpha = 0.2f),
                    ),
                ),
                shape = CircleShape,
            )
            .clip(CircleShape)
            .glassEffect(hazeState),
        contentAlignment = Alignment.Center,
    ) {
        NavigationRailItems(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(navigationItemsContentPadding),
        )

        SelectedTabCircleBlurredBackgroundVertical(
            color = animatedColor,
            animatedSelectedTabIndex = animatedSelectedTabIndex,
            modifier = Modifier
                .padding(navigationItemsContentPadding)
                .matchParentSize(),
        )

        SelectedTabSideLineIndicator(
            color = animatedColor,
            animatedSelectedTabIndex = animatedSelectedTabIndex,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Composable
private fun NavigationRailItems(
    currentTab: MainScreenTab,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.selectableGroup(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MainScreenTab.entries.forEach { tab ->
            NavigationTabItem(
                tab = tab,
                selected = currentTab == tab,
                onTabSelected = onTabSelected,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SelectedTabCircleBlurredBackgroundVertical(
    animatedSelectedTabIndex: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .then(
                if (isBlurSupported()) {
                    Modifier.blur(50.dp, BlurredEdgeTreatment.Unbounded)
                } else {
                    Modifier
                },
            ),
    ) {
        // draw background for current tab
        val tabHeight = size.height / MainScreenTab.entries.size
        val selectedTabCenter = Offset(
            x = size.width / 2,
            y = (tabHeight * animatedSelectedTabIndex) + (tabHeight / 2),
        )
        val radius = size.width / 2

        drawSelectedTabCircle(
            color = color,
            center = selectedTabCenter,
            radius = radius,
        )
    }
}

@Composable
private fun SelectedTabSideLineIndicator(
    color: Color,
    animatedSelectedTabIndex: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            addRoundRect(RoundRect(size.toRect(), CornerRadius(size.width)))
        }
        val length = PathMeasure().apply { setPath(path, false) }.length

        val tabHeight = size.height / MainScreenTab.entries.size

        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0f),
                    color.copy(alpha = 1f),
                    color.copy(alpha = 1f),
                    color.copy(alpha = 0f),
                ),
                startY = tabHeight * animatedSelectedTabIndex,
                endY = tabHeight * (animatedSelectedTabIndex + 1),
            ),
            style = Stroke(
                width = 6f,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(length / 2, length),
                ),
            ),
        )
    }
}

@Preview
@Composable
private fun GlassLikeNavigationRailBarPreview() {
    KaigiPreviewContainer {
        GlassLikeNavigationRailBar(
            hazeState = rememberHazeState(),
            currentTab = MainScreenTab.Timetable,
            onTabSelected = {},
            animatedSelectedTabIndex = 0f,
            animatedColor = MaterialTheme.colorScheme.primaryFixed,
        )
    }
}

@Preview
@Composable
private fun NavigationRailItemsPreview() {
    KaigiPreviewContainer {
        NavigationRailItems(
            currentTab = MainScreenTab.Timetable,
            onTabSelected = {},
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }
}

@Preview
@Composable
private fun SelectedTabCircleBlurredBackgroundVerticalPreview() {
    KaigiPreviewContainer {
        SelectedTabCircleBlurredBackgroundVertical(
            animatedSelectedTabIndex = 0f,
            color = MaterialTheme.colorScheme.primaryFixed,
            modifier = Modifier.fillMaxHeight().width(64.dp),
        )
    }
}

@Preview
@Composable
private fun SelectedTabSideLineIndicatorPreview() {
    KaigiPreviewContainer {
        SelectedTabSideLineIndicator(
            color = MaterialTheme.colorScheme.primaryFixed,
            animatedSelectedTabIndex = 0f,
            modifier = Modifier.fillMaxHeight().width(64.dp),
        )
    }
}
