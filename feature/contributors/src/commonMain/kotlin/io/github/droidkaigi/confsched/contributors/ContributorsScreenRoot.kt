package io.github.droidkaigi.confsched.contributors

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.architecture.AppBarSize
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: ContributorsScreenContext)
fun ContributorsScreenRoot(
    onBackClick: () -> Unit,
    onContributorClick: (String) -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.contributorsQueryKey),
        fallback = SoilFallbackDefaults.appBar(
            title = stringResource(ContributorsRes.string.contributor_title),
            onBackClick = onBackClick,
            appBarSize = AppBarSize.Medium,
            windowInsets = WindowInsets.safeDrawing,
        ),
    ) {
        ContributorsScreen(
            contributors = it,
            onBackClick = onBackClick,
            onContributorItemClick = onContributorClick,
        )
    }
}
