package io.github.droidkaigi.confsched.testing.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.LocaleList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.changoFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import soil.query.SwrCachePlus
import soil.query.compose.SwrClientProvider

const val TestRootTestTag = "TestRoot"

@Composable
fun TestDefaultsProvider(
    testDispatcher: TestDispatcher,
    fontFamily: FontFamily? = changoFontFamily(),
    content: @Composable () -> Unit,
) {
    Box(
        // Workaround (JVM): Another window (e.g. DropdownMenu) may create duplicate Root nodes,
        // causing fetchSemanticsNode inside captureRoboImage to fail with multiple candidates.
        // Adding a testTag ensures the Root is uniquely identified.
        modifier = Modifier.testTag(TestRootTestTag),
    ) {
        KaigiTheme(fontFamily = fontFamily) {
            Surface {
                SwrClientProvider(SwrCachePlus(CoroutineScope(testDispatcher))) {
                    CompositionLocalProvider(
                        LocalLifecycleOwner provides FakeLocalLifecycleOwner(),
                        LocalViewModelStoreOwner provides FakeViewModelStoreOwner(),
                        content = content,
                    )
                }
            }
        }
    }
}

private class FakeLocalLifecycleOwner : LifecycleOwner {
    override val lifecycle: Lifecycle = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED
    }
}

private class FakeViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}
