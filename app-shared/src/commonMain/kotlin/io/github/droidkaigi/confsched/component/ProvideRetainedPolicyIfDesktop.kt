package io.github.droidkaigi.confsched.component

import androidx.compose.runtime.Composable

@Composable
expect fun ProvideRetainedPolicyIfDesktop(
    content: @Composable () -> Unit,
)
