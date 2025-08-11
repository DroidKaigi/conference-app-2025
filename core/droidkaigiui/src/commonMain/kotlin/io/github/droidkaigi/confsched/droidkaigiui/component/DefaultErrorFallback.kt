package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.ui.tooling.preview.Preview
import soil.plant.compose.reacty.ErrorBoundaryContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(_: ScreenContext)
fun DefaultErrorFallback(
    errorBoundaryContext: ErrorBoundaryContext,
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            title?.let {
                AnimatedTextTopAppBar(
                    title = it,
                    navigationIcon = {
                        onBackClick?.let { onClick ->
                            IconButton(onClick = onClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }
                    }
                )
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        DefaultErrorFallbackContent(
            errorBoundaryContext = errorBoundaryContext,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun DefaultErrorFallbackPreview() {
    KaigiPreviewContainer {
        DefaultErrorFallback(
            errorBoundaryContext = ErrorBoundaryContext(Throwable("Error"), null),
        )
    }
}

@Preview
@Composable
private fun DefaultErrorFallbackPreview_WithTitle() {
    KaigiPreviewContainer {
        DefaultErrorFallback(
            errorBoundaryContext = ErrorBoundaryContext(Throwable("Error"), null),
            title = "Title",
        )
    }
}

@Preview
@Composable
private fun DefaultErrorFallbackPreview_WithBackButton() {
    KaigiPreviewContainer {
        DefaultErrorFallback(
            errorBoundaryContext = ErrorBoundaryContext(Throwable("Error"), null),
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun DefaultErrorFallbackPreview_WithTitleAndBackButton() {
    KaigiPreviewContainer {
        DefaultErrorFallback(
            errorBoundaryContext = ErrorBoundaryContext(Throwable("Error"), null),
            title = "Title",
            onBackClick = {},
        )
    }
}
