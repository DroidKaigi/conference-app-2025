package io.github.confsched.profile

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

expect fun Modifier.innerShadow(
    color: Color,
    shape: Shape,
    blur: Dp,
    offsetY: Dp = 2.dp,
    offsetX: Dp = 2.dp,
    spread: Dp = 4.dp,
): Modifier
