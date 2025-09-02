package io.github.droidkaigi.confsched.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.Lifecycle
import io.github.takahirom.rin.LocalShouldRemoveRetainedWhenRemovingComposition

@Composable
actual fun ProvideRetainedPolicyIfDesktop(
    content: @Composable (() -> Unit),
) {
    /**
     * Why this workaround is needed (Desktop/JVM only):
     *
     * - rememberRetained (rin) decides whether to DROP retained state when a composable leaves
     *   the composition by calling a policy from
     *   LocalShouldRemoveRetainedWhenRemovingComposition(lifecycleOwner).
     *
     * - The default policy in rin effectively allows removal while the LifecycleOwner is RESUMED.
     *   On Android/iOS, during navigation/transition a leaving entry typically moves to STARTED/CREATED,
     *   so removal doesn’t happen and state survives while you can still navigate back.
     *
     * - On Desktop/JVM, during list→detail transitions the leaving entry can remain RESUMED even
     *   though it’s temporarily removed from the composition. That makes the policy return true and
     *   rin’s ViewModel clears savedData in onNewSideEffect(). When you navigate back, consume()
     *   returns null and previous screen state (e.g., selectedDay/uiType) is re-initialized.
     *
     * - To prevent this premature clearing, we override the policy ONLY on Desktop so that state is
     *   removed **only when the owner is truly DESTROYED** (i.e., the entry is actually gone),
     *   not when it’s just temporarily out of the tree.
     *
     * - This keeps “back stack” state intact on Desktop, aligning behavior with Android/iOS.
     *   Actual cleanup still happens when the NavEntry is destroyed (ViewModel.onCleared()).
     */
    CompositionLocalProvider(
        LocalShouldRemoveRetainedWhenRemovingComposition provides { owner ->
            owner.lifecycle.currentState == Lifecycle.State.DESTROYED
        },
    ) {
        content()
    }
}
