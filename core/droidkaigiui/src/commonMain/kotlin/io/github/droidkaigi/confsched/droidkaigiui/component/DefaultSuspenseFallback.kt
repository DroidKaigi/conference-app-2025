package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.ui.tooling.preview.Preview

const val DefaultSuspenseFallbackTestTag = "DefaultSuspenseFallbackTestTag"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
context(_: ScreenContext)
@Composable
fun DefaultSuspenseFallback(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            title?.let {
                AnimatedTextTopAppBar(
                    title = title,
                    navigationIcon = {
                        onBackClick?.let {
                            IconButton(onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                )
            }
        },
        modifier = modifier.testTag(DefaultSuspenseFallbackTestTag),
    ) { innerPadding ->
        DefaultSuspenseFallbackContent(
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun DefaultSuspenseFallbackPreview() {
    KaigiPreviewContainer {
        DefaultSuspenseFallback()
    }
}

@Preview
@Composable
private fun DefaultSuspenseFallbackPreview_WithTitle() {
    KaigiPreviewContainer {
        DefaultSuspenseFallback(
            title = "Title",
        )
    }
}

@Preview
@Composable
private fun DefaultSuspenseFallbackPreview_WithBackNavigation() {
    KaigiPreviewContainer {
        DefaultSuspenseFallback(
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun DefaultSuspenseFallbackPreview_WithTitleAndBackNavigation() {
    KaigiPreviewContainer {
        DefaultSuspenseFallback(
            title = "Title",
            onBackClick = {},
        )
    }
}
