package io.github.droidkaigi.confsched

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.changoFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoMediumFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoRegularFontFamily
import io.github.droidkaigi.confsched.model.settings.KaigiFontFamily
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import soil.query.SwrCachePlus
import soil.query.SwrCacheScope
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.SwrClientProvider
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSoilQueryApi::class)
@Composable
context(appGraph: AppGraph)
fun KaigiApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                addPlatformFileSupport()
            }
            .build()
    }
    SwrClientProvider(SwrCachePlus(SwrCacheScope())) {
        val subscription = rememberSubscription(
            key = appGraph.createSettingsScreenContext().settingsSubscriptionKey,
            select = { it.useKaigiFontFamily },
        )
        val kaigiFontFamily = if (subscription.isSuccess) {
            when (subscription.data) {
                KaigiFontFamily.ChangoRegular -> changoFontFamily()
                KaigiFontFamily.RobotoRegular -> robotoRegularFontFamily()
                KaigiFontFamily.RobotoMedium -> robotoMediumFontFamily()
                KaigiFontFamily.SystemDefault -> null
                null -> null
            }
        } else {
            null
        }

        KaigiTheme(fontFamily = kaigiFontFamily) {
            Surface {
                KaigiAppUi()
            }
        }
    }
}
