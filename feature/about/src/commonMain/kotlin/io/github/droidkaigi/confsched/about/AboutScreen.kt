package io.github.droidkaigi.confsched.about

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.about.section.AboutDroidKaigiHeader
import io.github.droidkaigi.confsched.about.section.AboutFooter
import io.github.droidkaigi.confsched.about.section.aboutCredits
import io.github.droidkaigi.confsched.about.section.aboutOthers
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.droidkaigiui.extension.excludeTop
import io.github.droidkaigi.confsched.droidkaigiui.extension.plus
import io.github.droidkaigi.confsched.model.about.AboutItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    uiState: AboutScreenUiState,
    onAboutItemClick: (AboutItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(AboutRes.string.about_droidkaigi))
                }
            )
        },
        contentWindowInsets = WindowInsets(),
        modifier = modifier,
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding + WindowInsets.safeDrawingWithBottomNavBar.excludeTop().asPaddingValues(),
        ) {
            item {
                AboutDroidKaigiHeader(
                    onViewMapClick = {
                        onAboutItemClick(AboutItem.Map)
                    }
                )
            }
            aboutCredits(
                onSponsorsItemClick = {
                    onAboutItemClick(AboutItem.Sponsors)
                },
                onContributorsItemClick = {
                    onAboutItemClick(AboutItem.Contributors)
                },
                onStaffItemClick = {
                    onAboutItemClick(AboutItem.Staff)
                },
            )
            aboutOthers(
                onCodeOfConductItemClick = {
                    onAboutItemClick(AboutItem.CodeOfConduct)
                },
                onLicenseItemClick = {
                    onAboutItemClick(AboutItem.License)
                },
                onPrivacyPolicyItemClick = {
                    onAboutItemClick(AboutItem.PrivacyPolicy)
                },
                onSettingsItemClick = {
                    onAboutItemClick(AboutItem.Settings)
                },
            )
            item {
                AboutFooter(
                    versionName = uiState.versionName,
                    onYouTubeClick = {
                        onAboutItemClick(AboutItem.Youtube)
                    },
                    onXClick = {
                        onAboutItemClick(AboutItem.X)
                    },
                    onMediumClick = {
                        onAboutItemClick(AboutItem.Medium)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    KaigiPreviewContainer {
        AboutScreen(
            uiState = AboutScreenUiState(
                versionName = "1.0.0",
            ),
            onAboutItemClick = {},
        )
    }
}
