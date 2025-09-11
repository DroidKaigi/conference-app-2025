package io.github.droidkaigi.confsched.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.util.plus
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.model.settings.KaigiFontFamily
import io.github.droidkaigi.confsched.settings.section.accessibility
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val SettingsScreenLazyColumnTestTag = "SettingsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsScreenUiState,
    onBackClick: () -> Unit,
    onSelectUseFontFamily: (KaigiFontFamily) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            AnimatedMediumTopAppBar(
                title = stringResource(SettingsRes.string.settings_title),
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.displayCutout.union(WindowInsets.systemBars),
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .testTag(SettingsScreenLazyColumnTestTag),
            contentPadding = padding + PaddingValues(vertical = 20.dp, horizontal = 16.dp),
            state = lazyListState,
        ) {
            accessibility(
                uiState = uiState,
                onSelectUseFontFamily = onSelectUseFontFamily,
            )
        }
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    KaigiPreviewContainer {
        SettingsScreen(
            uiState = SettingsScreenUiState(
                useKaigiFontFamily = KaigiFontFamily.ChangoRegular,
            ),
            onBackClick = {},
            onSelectUseFontFamily = {},
        )
    }
}
