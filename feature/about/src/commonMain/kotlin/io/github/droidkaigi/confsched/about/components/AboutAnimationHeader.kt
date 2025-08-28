package io.github.droidkaigi.confsched.about.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.about_day_night_title_only
import io.github.droidkaigi.confsched.about.mascot1
import io.github.droidkaigi.confsched.about.mascot2
import io.github.droidkaigi.confsched.about.mascot3
import org.jetbrains.compose.resources.painterResource
import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

@Composable
fun AboutAnimationHeader(
    modifier: Modifier = Modifier.fillMaxWidth(),
    backgroundAspectRatio: Float = 3f, // 3840x1280
    mascotHeightFraction: Float = 0.16f, // mascot height vs background height
    edgePaddingFraction: Float = 0.02f, // safe margin (center-based)
    minMascotSize: Dp = 16.dp,
    maxMascotSize: Dp = 190.dp,
) {
    val bg: Painter = painterResource(AboutRes.drawable.about_day_night_title_only)
    val m1: Painter = painterResource(AboutRes.drawable.mascot1)
    val m2: Painter = painterResource(AboutRes.drawable.mascot2)
    val m3: Painter = painterResource(AboutRes.drawable.mascot3)

    BoxWithConstraints(
        modifier = modifier.aspectRatio(backgroundAspectRatio).clipToBounds(),
    ) {
        val density = LocalDensity.current
        val w = with(density) { maxWidth.toPx() }
        val h = with(density) { maxHeight.toPx() }

        Image(
            painter = bg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )

        val mascotSize = (h * mascotHeightFraction)
            .coerceIn(with(density) { minMascotSize.toPx() }, with(density) { maxMascotSize.toPx() })
        val mascotHalf = mascotSize / 2f
        val safeEdge = min(w, h) * edgePaddingFraction
        val contentPadding = with(density) { (safeEdge + mascotHalf).toDp() }

        var leftTop by remember { mutableStateOf<Rect?>(null) }
        var rightTop by remember { mutableStateOf<Rect?>(null) }
        var rightBottom by remember { mutableStateOf<Rect?>(null) }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .padding(contentPadding)
                .onGloballyPositioned { leftTop = it.boundsInParent() },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.TopEnd)
                .padding(contentPadding)
                .onGloballyPositioned { rightTop = it.boundsInParent() },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomEnd)
                .padding(start = contentPadding, top = contentPadding, end = contentPadding, bottom = 0.dp)
                .onGloballyPositioned { rightBottom = it.boundsInParent() },
        )

        RoamingMascot(
            region = leftTop,
            painter = m1,
            sizePx = mascotSize,
            waypointsX = 8,
            waypointsY = 7,
            durationXMs = 36_000,
            durationYMs = 28_000,
            seed = 11,
        )
        RoamingMascot(
            region = rightTop,
            painter = m2,
            sizePx = mascotSize * 0.95f,
            waypointsX = 9,
            waypointsY = 6,
            durationXMs = 40_000,
            durationYMs = 30_000,
            seed = 22,
        )
        PeekMascotFromBottom(
            region = rightBottom,
            painter = m3,
            sizePx = mascotSize * 0.9f,
            showMs = 450..900,
            holdMs = 1400..3200,
            hideMs = 300..700,
            waitMs = 800..1200,
        )
    }
}

@Composable
private fun RoamingMascot(
    region: Rect?,
    painter: Painter,
    sizePx: Float,
    waypointsX: Int,
    waypointsY: Int,
    durationXMs: Int,
    durationYMs: Int,
    seed: Int,
) {
    if (region == null) return

    val rnd = remember(seed) { Random(seed) }
    val pathX = remember(seed, waypointsX) { Waypoints.randomLoop(waypointsX, rnd) }
    val pathY = remember(seed, waypointsY) { Waypoints.randomLoop(waypointsY, rnd) }

    val t = rememberInfiniteTransition(label = "roam-$seed")
    val px by t.animateFloat(
        0f,
        1f,
        animationSpec = infiniteRepeatable(tween(durationXMs, easing = LinearEasing), RepeatMode.Restart),
        label = "px",
    )
    val py by t.animateFloat(
        0f,
        1f,
        animationSpec = infiniteRepeatable(tween(durationYMs, easing = LinearEasing), RepeatMode.Restart),
        label = "py",
    )

    val cx = region.xAt(pathX.sample(px))
    val cy = region.yAt(pathY.sample(py))
    val half = sizePx / 2f

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer {
                translationX = cx - half
                translationY = cy - half
            }
            .size(with(LocalDensity.current) { sizePx.toDp() }),
    )
}

@Composable
private fun PeekMascotFromBottom(
    region: Rect?,
    painter: Painter,
    sizePx: Float,
    showMs: IntRange,
    holdMs: IntRange,
    hideMs: IntRange,
    waitMs: IntRange,
) {
    if (region == null) return

    val rnd = remember(painter) { Random(painter.hashCode()) }
    val episodes = remember(painter) {
        Episodes.randomBottomPeek(
            rnd = rnd,
            count = 10,
            minRatio = 0.55f,
            maxRatio = 0.90f,
            show = showMs,
            hold = holdMs,
            hide = hideMs,
            wait = waitMs,
        )
    }

    val t = rememberInfiniteTransition(label = "peek-${painter.hashCode()}")
    val timeline by t.animateFloat(
        0f,
        episodes.totalDuration.toFloat(),
        animationSpec = infiniteRepeatable(tween(episodes.totalDuration, easing = LinearEasing), RepeatMode.Restart),
        label = "timeline",
    )

    val (ep, localMs) = episodes.locate(timeline.toInt())
    val xCurr = ep.xNorm
    val xNext = episodes.nextOf(ep).xNorm
    val xCenter = if (localMs < ep.hideEnd) {
        xCurr
    } else {
        val wait = ep.waitMs.coerceAtLeast(1)
        val f = ((localMs - ep.hideEnd).toFloat() / wait).coerceIn(0f, 1f)
        lerp(xCurr, xNext, LinearOutSlowInEasing.transform(f))
    }

    val xPx = region.xAt(xCenter)
    val showTop = region.bottom - (sizePx * ep.visibleRatio)
    val hideY = region.bottom + with(LocalDensity.current) { 8.dp.toPx() }

    val topY = when {
        localMs < ep.showEnd ->
            lerp(hideY, showTop, LinearOutSlowInEasing.transform(localMs / ep.showMs.toFloat()))

        localMs < ep.holdEnd -> showTop
        localMs < ep.hideEnd ->
            lerp(showTop, hideY, LinearOutSlowInEasing.transform((localMs - ep.holdEnd) / ep.hideMs.toFloat()))

        else -> hideY
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer {
                translationX = xPx - (sizePx / 2f)
                translationY = topY
            }
            .size(with(LocalDensity.current) { sizePx.toDp() }),
    )
}

private data class Waypoints(
    val points: List<Float>,
) {
    fun sample(
        progress: Float,
    ): Float {
        val segs = (points.size - 1).coerceAtLeast(1)
        val s = progress.coerceIn(0f, 1f) * segs
        val i = floor(s).toInt().coerceIn(0, segs - 1)
        val t = s - i
        return lerp(points[i], points[i + 1], t)
    }

    companion object {
        fun randomLoop(
            count: Int,
            rnd: Random,
            margin: Float = 0.08f,
        ): Waypoints {
            val n = count.coerceAtLeast(2)
            val values = MutableList(n) { margin + rnd.nextFloat() * (1f - 2 * margin) }
            return Waypoints(values + values.first())
        }
    }
}

private data class Episode(
    val xNorm: Float,
    val visibleRatio: Float,
    val showMs: Int,
    val holdMs: Int,
    val hideMs: Int,
    val waitMs: Int,
) {
    val showEnd = showMs
    val holdEnd = showEnd + holdMs
    val hideEnd = holdEnd + hideMs
    val total = hideEnd + waitMs
}

private class Episodes(
    private val items: List<Episode>,
) {
    val totalDuration = items.sumOf { it.total }
    private val cumulativeEnds = run {
        val acc = ArrayList<Int>(items.size)
        var t = 0
        for (e in items) {
            t += e.total
            acc += t
        }
        acc
    }

    fun locate(timeMs: Int): Pair<Episode, Int> {
        val t = timeMs.coerceIn(0, totalDuration - 1)
        var idx = 0
        while (idx < cumulativeEnds.size && t >= cumulativeEnds[idx]) idx++
        val start = if (idx == 0) 0 else cumulativeEnds[idx - 1]
        return items[idx] to (t - start)
    }

    fun nextOf(e: Episode): Episode {
        val i = items.indexOf(e)
        return items[(i + 1) % items.size]
    }

    companion object {
        fun randomBottomPeek(
            rnd: Random,
            count: Int,
            minRatio: Float,
            maxRatio: Float,
            show: IntRange,
            hold: IntRange,
            hide: IntRange,
            wait: IntRange,
        ): Episodes {
            val list = List(count) {
                Episode(
                    xNorm = 0.08f + rnd.nextFloat() * 0.84f,
                    visibleRatio = minRatio + rnd.nextFloat() * (maxRatio - minRatio),
                    showMs = rnd.nextInt(show.first, show.last + 1),
                    holdMs = rnd.nextInt(hold.first, hold.last + 1),
                    hideMs = rnd.nextInt(hide.first, hide.last + 1),
                    waitMs = rnd.nextInt(wait.first, wait.last + 1),
                )
            }
            return Episodes(list)
        }
    }
}

private fun Rect.xAt(n: Float): Float = left + width * n
private fun Rect.yAt(n: Float): Float = top + height * n
private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
