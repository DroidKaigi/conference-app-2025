package io.github.droidkaigi.confsched.droidkaigiui.extension

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.isBackPressed
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.isForwardPressed
import androidx.compose.ui.input.pointer.isMetaPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.launch
import kotlin.math.abs

private data class NestedScrollConsumption(
    val requestedScrollToChild: Float,
    val childScrollConsumed: Float,
)

@Composable
actual fun Modifier.enableMouseDragScroll(
    scrollableState: ScrollableState,
): Modifier {
    val scope = rememberCoroutineScope()
    val decaySpec: DecayAnimationSpec<Float> = remember { exponentialDecay() }
    val nestedDispatcher = remember { NestedScrollDispatcher() }

    fun ScrollScope.performNestedScrollStep(
        deltaY: Float,
        source: NestedScrollSource,
    ): NestedScrollConsumption {
        val availableDelta = Offset(0f, deltaY)

        val parentPreConsumed = nestedDispatcher.dispatchPreScroll(
            available = availableDelta,
            source = source,
        )

        val deltaToChild = availableDelta.y - parentPreConsumed.y
        val hasSignificantChildDelta = abs(deltaToChild) > 0.5f
        val childConsumedDelta = if (hasSignificantChildDelta) scrollBy(-deltaToChild) else 0f

        val totalConsumedForParent = Offset(0f, parentPreConsumed.y - childConsumedDelta)
        val remainingDelta = availableDelta - totalConsumedForParent
        nestedDispatcher.dispatchPostScroll(
            consumed = totalConsumedForParent,
            available = remainingDelta,
            source = source,
        )

        return NestedScrollConsumption(
            requestedScrollToChild = deltaToChild,
            childScrollConsumed = childConsumedDelta,
        )
    }

    fun didHitScrollEdge(consumption: NestedScrollConsumption): Boolean {
        val childCouldNotConsumeAll = abs(consumption.childScrollConsumed) < abs(consumption.requestedScrollToChild) - 0.5f
        return childCouldNotConsumeAll
    }

    suspend fun applyMouseDragStep(dragDeltaY: Float) {
        val hasDragMovement = dragDeltaY != 0f
        if (!hasDragMovement) return

        scrollableState.scroll {
            performNestedScrollStep(
                deltaY = dragDeltaY,
                source = NestedScrollSource.UserInput,
            )
        }
    }

    suspend fun runFlingAnimation(initialVelocityY: Float) {
        scrollableState.scroll {
            var lastValue = 0f
            val anim = AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocityY,
            )
            anim.animateDecay(decaySpec) {
                val frameDelta = value - lastValue
                lastValue = value
                val hasFrameDelta = frameDelta != 0f
                if (hasFrameDelta) {
                    val consumption = performNestedScrollStep(
                        deltaY = frameDelta,
                        source = NestedScrollSource.SideEffect,
                    )
                    val reachedEdge = didHitScrollEdge(consumption)
                    if (reachedEdge) {
                        cancelAnimation()
                    }
                }
            }
        }
    }

    return this
        // Relay for passing events to higher-level NestedScrollConnection components such as TopAppBar
        .nestedScroll(object : NestedScrollConnection {}, nestedDispatcher)
        .pointerInput(scrollableState) {
            var velocityTracker: VelocityTracker? = null

            detectDragGestures(
                onDragStart = { velocityTracker = VelocityTracker() },
                onDrag = { change, dragAmount ->
                    if (change.type != PointerType.Mouse) return@detectDragGestures
                    val dragDeltaY = dragAmount.y
                    val hasDragMovement = dragDeltaY != 0f
                    if (hasDragMovement) {
                        scope.launch { applyMouseDragStep(dragDeltaY) }
                    }
                    velocityTracker?.addPosition(change.uptimeMillis, change.position)
                    change.consume()
                },
                onDragEnd = {
                    val releaseVelocityY = velocityTracker?.calculateVelocity()?.y ?: 0f
                    velocityTracker = null
                    scope.launch { runFlingAnimation(releaseVelocityY) }
                },
                onDragCancel = { velocityTracker = null },
            )
        }
}

private const val VerticalWheelDeltaThreshold = 0.01f

private enum class ZoomDirection { ZoomIn, ZoomOut }

private fun PointerEvent.verticalWheelDeltaY(): Float = changes.firstOrNull()?.scrollDelta?.y ?: 0f

/**
 * Treat very small vertical wheel deltas as "no scroll".
 *
 * Typical causes:
 * - Horizontal trackpad scroll (dx ≠ 0, dy = 0)
 * - Start/end frames (momentum tail) reporting zero delta
 * - Device/OS rounding tiny deltas down to 0
 */
private fun Float.isEffectivelyNoVerticalScroll(): Boolean = abs(this) < VerticalWheelDeltaThreshold

private fun verticalDeltaToZoomDirection(
    verticalWheelDelta: Float,
): ZoomDirection = if (verticalWheelDelta < 0f) ZoomDirection.ZoomIn else ZoomDirection.ZoomOut

private fun ZoomDirection.multiplier(
    step: Float,
): Float = when (this) {
    ZoomDirection.ZoomIn -> step
    ZoomDirection.ZoomOut -> 1f / step
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.enableMouseWheelZoomForDesktop(
    multiplyVerticalScaleBy: (Float) -> Unit,
    zoomStep: Float,
    requireModifierKey: Boolean,
): Modifier = this.onPointerEvent(
    eventType = PointerEventType.Scroll,
    pass = PointerEventPass.Initial,
) { event ->
    val mods = event.keyboardModifiers
    val wantsZoom = if (requireModifierKey) (mods.isCtrlPressed || mods.isMetaPressed) else true
    if (!wantsZoom) return@onPointerEvent

    val verticalWheelDelta = event.verticalWheelDeltaY()
    // Bail out if there's no significant vertical scroll.
    if (verticalWheelDelta.isEffectivelyNoVerticalScroll()) return@onPointerEvent

    val zoomDirection = verticalDeltaToZoomDirection(verticalWheelDelta)
    val zoomMultiplier = zoomDirection.multiplier(zoomStep)
    multiplyVerticalScaleBy(zoomMultiplier)

    event.changes.forEach { it.consume() }
}
