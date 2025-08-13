package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailTopAppBar
import org.jetbrains.compose.ui.tooling.preview.Preview
import soil.plant.compose.reacty.ErrorBoundaryContext

@Composable
fun TimetableItemDetailScreenErrorFallback(
    errorBoundaryContext: ErrorBoundaryContext,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            // FIXME: This is a temporary fix for missing LocalRoomTheme
            //   Consider adding TimetableItemDetailTopAppBar with no roomTheme dependency
            ProvideRoomTheme(TimetableItem.Session.fake().room.roomTheme) {
                TimetableItemDetailTopAppBar(
                    onBackClick = { onBackClick() },
                )
            }
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = errorBoundaryContext.err.message ?: "An error occurred",
            )
            Button(onClick = { errorBoundaryContext.reset?.invoke() }) {
                Text(text = "Retry")
            }
        }
    }
}

@Preview
@Composable
private fun TimetableItemDetailScreenErrorFallbackPreview() {
    KaigiPreviewContainer {
        TimetableItemDetailScreenErrorFallback(
            errorBoundaryContext = ErrorBoundaryContext(Throwable("Error"), {}),
            onBackClick = {},
            modifier = Modifier,
        )
    }
}
