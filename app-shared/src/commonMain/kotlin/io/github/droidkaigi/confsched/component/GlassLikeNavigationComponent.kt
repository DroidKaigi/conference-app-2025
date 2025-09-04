package io.github.droidkaigi.confsched.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import io.github.droidkaigi.confsched.common.graphics.isBlurSupported
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun Modifier.glassEffect(hazeState: HazeState): Modifier = then(
    if (isBlurSupported()) {
        Modifier.hazeEffect(
            state = hazeState,
            style = HazeDefaults.style(
                backgroundColor = MaterialTheme.colorScheme.background,
            ),
        )
    } else {
        Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = .95f))
    },
)

fun DrawScope.drawSelectedTabCircle(
    color: Color,
    center: Offset,
    radius: Float,
) {
    if (isBlurSupported()) {
        drawCircle(
            color = color.copy(alpha = .6f),
            radius = radius,
            center = center,
        )
    } else {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.5f),
                    color.copy(alpha = 0.1f),
                ),
                center = center,
                radius = radius,
            ),
            center = center,
            radius = radius,
        )
    }
}

@Composable
fun NavigationTabItem(
    tab: MainScreenTab,
    selected: Boolean,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else .98f,
        visibilityThreshold = .000001f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        label = "scale",
    )

    Box(
        modifier = modifier
            .scale(scale)
            .selectable(
                selected = selected,
                onClick = { onTabSelected(tab) },
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (selected) vectorResource(tab.iconOn) else vectorResource(tab.iconOff),
            contentDescription = stringResource(tab.label),
            tint = if (selected) MaterialTheme.colorScheme.primaryFixed else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )
    }
}
