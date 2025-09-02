package io.github.droidkaigi.confsched.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun GlassLikeBottomNavigationBar(
    hazeState: HazeState,
    currentTab: MainScreenTab,
    onTabSelected: (MainScreenTab) -> Unit,
    animatedSelectedTabIndex: Float,
    animatedColor: Color,
    modifier: Modifier = Modifier,
) {
    val navigationItemsContentPadding = PaddingValues(horizontal = 12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                width = Dp.Hairline,
                brush = Brush.verticalGradient(
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
        BottomNavigationBarItems(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(navigationItemsContentPadding),
        )

        SelectedTabCircleBlurredBackground(
            color = animatedColor,
            animatedSelectedTabIndex = animatedSelectedTabIndex,
            modifier = Modifier
                .padding(navigationItemsContentPadding)
                .matchParentSize(),
        )

        SelectedTabBottomLineIndicator(
            color = animatedColor,
            animatedSelectedTabIndex = animatedSelectedTabIndex,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Composable
private fun BottomNavigationBarItems(
    currentTab: MainScreenTab,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.selectableGroup(),
    ) {
        MainScreenTab.entries.forEach { tab ->
            NavigationTabItem(
                tab = tab,
                selected = currentTab == tab,
                onTabSelected = onTabSelected,
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun SelectedTabCircleBlurredBackground(
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
        val tabWidth = size.width / MainScreenTab.entries.size
        val selectedTabCenter = Offset(
            x = (tabWidth * animatedSelectedTabIndex) + (tabWidth / 2),
            y = size.height / 2,
        )
        val radius = size.height / 2

        drawSelectedTabCircle(
            color = color,
            center = selectedTabCenter,
            radius = radius,
        )
    }
}

@Composable
private fun SelectedTabBottomLineIndicator(
    color: Color,
    animatedSelectedTabIndex: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            addRoundRect(RoundRect(size.toRect(), CornerRadius(size.height)))
        }
        val length = PathMeasure().apply { setPath(path, false) }.length

        val tabWidth = size.width / MainScreenTab.entries.size

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color.copy(alpha = 0f),
                    color.copy(alpha = 1f),
                    color.copy(alpha = 1f),
                    color.copy(alpha = 0f),
                ),
                startX = tabWidth * animatedSelectedTabIndex,
                endX = tabWidth * (animatedSelectedTabIndex + 1),
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
private fun GlassLikeBottomNavigationBarPreview() {
    KaigiPreviewContainer {
        GlassLikeBottomNavigationBar(
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
private fun BottomNavigationBarItemsPreview() {
    KaigiPreviewContainer {
        BottomNavigationBarItems(
            currentTab = MainScreenTab.Timetable,
            onTabSelected = {},
            modifier = Modifier.padding(horizontal = 12.dp),
        )
    }
}

@Preview
@Composable
private fun SelectedTabCircleBlurredBackgroundPreview() {
    KaigiPreviewContainer {
        SelectedTabCircleBlurredBackground(
            animatedSelectedTabIndex = 0f,
            color = MaterialTheme.colorScheme.primaryFixed,
            modifier = Modifier.fillMaxWidth().height(64.dp),
        )
    }
}

@Preview
@Composable
private fun SelectedTabBottomLineIndicatorPreview() {
    KaigiPreviewContainer {
        SelectedTabBottomLineIndicator(
            color = MaterialTheme.colorScheme.primaryFixed,
            animatedSelectedTabIndex = 0f,
            modifier = Modifier.fillMaxWidth().height(64.dp),
        )
    }
}
