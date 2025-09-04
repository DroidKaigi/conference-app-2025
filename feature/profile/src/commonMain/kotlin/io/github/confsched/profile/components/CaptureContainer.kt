package io.github.confsched.profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import kotlinx.coroutines.launch

@Composable
fun CaptureContainer(
    onRendered: (ImageBitmap) -> Unit,
    content: @Composable () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }

                val size = graphicsLayer.size
                val shouldCapture = size.height > 0 && size.width > 0
                if (shouldCapture) {
                    coroutineScope.launch {
                        onRendered(graphicsLayer.toImageBitmap())
                    }
                }
            },
    ) {
        content()
    }
}
