package io.github.droidkaigi.confsched.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.context.PresenterContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

typealias EventFlow<T> = MutableSharedFlow<T>

@Composable
fun <T> rememberEventFlow(): EventFlow<T> {
    return remember {
        MutableSharedFlow(extraBufferCapacity = 20)
    }
}

@Composable
context(_: PresenterContext)
fun <EVENT> EventEffect(
    eventFlow: EventFlow<EVENT>,
    block: suspend CoroutineScope.(event: EVENT) -> Unit,
) {
    SafeLaunchedEffect(eventFlow) {
        supervisorScope {
            eventFlow.collect { event ->
                launch {
                    block(event)
                }
            }
        }
    }
}
