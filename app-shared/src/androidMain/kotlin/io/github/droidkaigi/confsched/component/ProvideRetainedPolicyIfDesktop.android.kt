package io.github.droidkaigi.confsched.component

import androidx.compose.runtime.Composable

@Composable
actual fun ProvideRetainedPolicyIfDesktop(
    content: @Composable (() -> Unit),
) {
    // NOOP
    content()
}
