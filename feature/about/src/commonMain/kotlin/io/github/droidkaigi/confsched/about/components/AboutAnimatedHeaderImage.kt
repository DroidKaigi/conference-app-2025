package io.github.droidkaigi.confsched.about.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.about_header_bg_only
import io.github.droidkaigi.confsched.about.about_header_mascot1
import io.github.droidkaigi.confsched.about.about_header_mascot2
import io.github.droidkaigi.confsched.about.about_header_mascot3
import io.github.droidkaigi.confsched.about.about_header_title_bg_title
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@Composable
fun AboutAnimatedHeaderImage(
    modifier: Modifier = Modifier,
) {
    val titleAlpha = remember { Animatable(0f) }
    val mascot1Alpha = remember { Animatable(0f) }
    val mascot2Alpha = remember { Animatable(0f) }

    val density = LocalDensity.current
    val backgroundPainter = painterResource(AboutRes.drawable.about_header_bg_only)

    val dynamicAspect = remember(backgroundPainter) {
        val s = backgroundPainter.intrinsicSize
        val hasSize = s != Size.Unspecified && s.width > 0f && s.height > 0f
        if (hasSize) s.width / s.height else 1648f / 550f
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(dynamicAspect)
            .clipToBounds(),
    ) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }

        val mascot1OffsetX = remember { Animatable(0f) }
        val mascot1OffsetY = remember { Animatable(0f) }

        val mascot2OffsetX = remember { Animatable(0f) }
        val mascot2OffsetY = remember { Animatable(0f) }

        val mascot3OffsetY = remember(maxHeightPx) { Animatable(maxHeightPx) }
        val mascot3OffsetX = remember { Animatable(0f) }

        LaunchedEffect(maxWidth, maxHeight) {
            playHeaderIntro(
                titleAlpha = titleAlpha,
                mascot1Alpha = mascot1Alpha,
                mascot2Alpha = mascot2Alpha,
                mascot3OffsetY = mascot3OffsetY,
            )
            coroutineScope {
                launch {
                    loopMascotFloat(
                        offsetX = mascot1OffsetX,
                        offsetY = mascot1OffsetY,
                        containerWidth = maxWidthPx,
                        containerHeight = maxHeightPx,
                        anchorXRatio = 370f / 1648f,
                        visibleLeftRatio = 0.04f,
                        visibleRightRatio = 0.44f - 0.04f,
                        baselineRangeRatio = 0.04f..0.08f,
                        topCapRatio = 0.10f,
                        amplitudeRangeRatio = 0.015f..0.035f,
                        horizontalDurationMs = 10_000..16_000,
                        verticalDurationMs = 1_400..2_200,
                    )
                }
                launch {
                    loopMascotFloat(
                        offsetX = mascot2OffsetX,
                        offsetY = mascot2OffsetY,
                        containerWidth = maxWidthPx,
                        containerHeight = maxHeightPx,
                        anchorXRatio = 1478f / 1648f,
                        visibleLeftRatio = 0.58f + 0.04f,
                        visibleRightRatio = 1.0f - 0.02f,
                        baselineRangeRatio = 0.03f..0.06f,
                        topCapRatio = 0.085f,
                        amplitudeRangeRatio = 0.01f..0.02f,
                        horizontalDurationMs = 10_000..16_000,
                        verticalDurationMs = 1_400..2_200,
                    )
                }
                launch {
                    loopMascot3Peek(
                        offsetY = mascot3OffsetY,
                        offsetX = mascot3OffsetX,
                        containerWidth = maxWidthPx,
                        containerHeight = maxHeightPx,
                    )
                }
            }
        }

        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize().zIndex(0f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot3),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    translationX = mascot3OffsetX.value
                    translationY = mascot3OffsetY.value
                }
                .zIndex(1.5f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_title_bg_title),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = titleAlpha.value }
                .zIndex(1f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    alpha = mascot1Alpha.value
                    translationX = mascot1OffsetX.value
                    translationY = mascot1OffsetY.value
                }
                .zIndex(2.0f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot2),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    alpha = mascot2Alpha.value
                    translationX = mascot2OffsetX.value
                    translationY = mascot2OffsetY.value
                }
                .zIndex(2.1f),
        )
    }
}

private suspend fun playHeaderIntro(
    titleAlpha: Animatable<Float, *>,
    mascot1Alpha: Animatable<Float, *>,
    mascot2Alpha: Animatable<Float, *>,
    mascot3OffsetY: Animatable<Float, *>,
) {
    delay(180)

    titleAlpha.animateTo(1f, tween(durationMillis = 800))
    delay(80)

    coroutineScope {
        val jobMascot1 = launch {
            mascot1Alpha.animateTo(1f, tween(durationMillis = 500))
        }
        val jobMascot2 = launch {
            while (mascot1Alpha.value < 0.5f) {
                yield()
                delay(16)
            }
            mascot2Alpha.animateTo(1f, tween(durationMillis = 500))
        }
        jobMascot1.join()
        jobMascot2.join()
    }

    delay(40)
    mascot3OffsetY.animateTo(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
    )
    delay(1500)
}

private suspend fun loopMascot3Peek(
    offsetY: Animatable<Float, *>,
    offsetX: Animatable<Float, *>,
    containerWidth: Float,
    containerHeight: Float,
) {
    val maxRightShift = containerWidth * 0.20f

    while (true) {
        offsetY.animateTo(
            targetValue = containerHeight,
            animationSpec = tween(
                durationMillis = Random.nextInt(700, 1100),
                easing = FastOutSlowInEasing,
            ),
        )

        val nextX = Random.nextFloat() * maxRightShift
        offsetX.snapTo(nextX)

        delay(Random.nextLong(250, 600))

        val revealRatio = Random.nextFloat() * 0.1f + 0.9f
        val targetYForReveal = (1f - revealRatio) * containerHeight

        offsetY.animateTo(
            targetValue = targetYForReveal,
            animationSpec = tween(
                durationMillis = Random.nextInt(900, 1400),
                easing = FastOutSlowInEasing,
            ),
        )

        delay(Random.nextLong(2000, 3000))
    }
}

/**
 * Shared for mascot1 / mascot2: horizontal back-and-forth within the upper half + vertical sway.
 *
 * @param anchorXRatio        Horizontal anchor position within the image (ratio from the left: 0.0..1.0)
 * @param visibleLeftRatio    Left screen boundary where the anchor should stay (ratio)
 * @param visibleRightRatio   Right screen boundary where the anchor should stay (ratio)
 * @param baselineRangeRatio  Vertical baseline range as ratios (e.g., 0.04..0.08)
 * @param topCapRatio         Upper vertical cap (e.g., 0.10 = within the top 10%)
 * @param amplitudeRangeRatio Vertical sway amplitude range (e.g., 0.015..0.035)
 * @param horizontalDurationMs Duration range for one horizontal leg of motion
 * @param verticalDurationMs   Duration range for one vertical leg of motion
 */
private suspend fun loopMascotFloat(
    offsetX: Animatable<Float, *>,
    offsetY: Animatable<Float, *>,
    containerWidth: Float,
    containerHeight: Float,
    anchorXRatio: Float,
    visibleLeftRatio: Float,
    visibleRightRatio: Float,
    baselineRangeRatio: ClosedRange<Float>,
    topCapRatio: Float,
    amplitudeRangeRatio: ClosedRange<Float>,
    horizontalDurationMs: IntRange,
    verticalDurationMs: IntRange,
) {
    val anchorX = containerWidth * anchorXRatio

    val visibleLeft = containerWidth * visibleLeftRatio
    val visibleRight = containerWidth * visibleRightRatio

    var xMin = visibleLeft - anchorX
    var xMax = visibleRight - anchorX
    if (xMin > xMax) {
        val t = xMin
        xMin = xMax
        xMax = t
    }

    fun randIn(range: ClosedRange<Float>) = range.start + Random.nextFloat() * (range.endInclusive - range.start)

    val topCapY = containerHeight * topCapRatio
    val baselineY = (containerHeight * randIn(baselineRangeRatio)).coerceAtMost(topCapY * 0.9f)
    val amplitude = containerHeight * randIn(amplitudeRangeRatio)

    val downY = (baselineY + amplitude).coerceAtMost(topCapY)
    val upY = (baselineY - amplitude).coerceAtLeast(0f)

    if (offsetX.value < xMin || offsetX.value > xMax) {
        offsetX.animateTo(xMin, tween(durationMillis = 700, easing = FastOutSlowInEasing))
    }
    if (offsetY.value != baselineY) {
        offsetY.animateTo(baselineY, tween(durationMillis = 700, easing = FastOutSlowInEasing))
    }

    coroutineScope {
        launch {
            var toRight = Random.nextBoolean()
            while (true) {
                val targetX = if (toRight) xMax else xMin
                offsetX.animateTo(
                    targetX,
                    tween(
                        durationMillis = Random.nextInt(
                            horizontalDurationMs.first,
                            horizontalDurationMs.last + 1,
                        ),
                        easing = FastOutSlowInEasing,
                    ),
                )
                toRight = !toRight
            }
        }
        launch {
            while (true) {
                offsetY.animateTo(
                    downY,
                    tween(
                        durationMillis = Random.nextInt(
                            verticalDurationMs.first,
                            verticalDurationMs.last + 1,
                        ),
                        easing = FastOutSlowInEasing,
                    ),
                )
                offsetY.animateTo(
                    upY,
                    tween(
                        durationMillis = Random.nextInt(
                            verticalDurationMs.first,
                            verticalDurationMs.last + 1,
                        ),
                        easing = FastOutSlowInEasing,
                    ),
                )
            }
        }
    }
}
