package io.github.droidkaigi.confsched

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import soil.query.SwrCachePlus
import soil.query.SwrCacheScope
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.SwrClientProvider

context(appGraph: AppGraph)
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSoilQueryApi::class)
@Composable
fun KaigiApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .build()
    }
    SwrClientProvider(SwrCachePlus(SwrCacheScope())) {
        KaigiTheme {
            Surface {
                KaigiAppUi()
            }
        }
    }
}
