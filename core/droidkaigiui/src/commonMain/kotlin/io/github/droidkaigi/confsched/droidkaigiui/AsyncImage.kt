package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State
import coil3.compose.SubcomposeAsyncImageScope
import org.jetbrains.compose.resources.painterResource
import coil3.compose.SubcomposeAsyncImage as CoilSubcomposeAsyncImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SubcomposeAsyncImage(
    model: Any?,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    loading: @Composable (SubcomposeAsyncImageScope.(State.Loading) -> Unit)? = {
        CircularWavyProgressIndicator(
            modifier = Modifier
                .padding(4.dp)
                .wrapContentSize()
                .aspectRatio(1f)
                .semantics {
                    contentDescription?.let {
                        this.contentDescription = it
                    }
                },
        )
    },
    error: @Composable (SubcomposeAsyncImageScope.(State.Error) -> Unit) = {
        Image(
            painter = painterResource(DroidkaigiuiRes.drawable.error_mascot),
            contentDescription = null,
        )
    },
) {
    CoilSubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        loading = loading,
        success = {
            Image(
                painter = painter,
                contentDescription = contentDescription,
            )
        },
        error = error,
    )
}
