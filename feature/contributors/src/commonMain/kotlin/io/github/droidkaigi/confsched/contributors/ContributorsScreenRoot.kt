package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundaryDefaults
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundaryFallback
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

context(screenContext: ContributorsScreenContext)
@Composable
fun ContributorsScreenRoot(
    onBackClick: () -> Unit,
    onContributorClick: (String) -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.contributorsQueryKey),
        fallback = SoilDataBoundaryDefaults.appBarFallback(
            title = stringResource(ContributorsRes.string.contributor_title),
            onBackClick = onBackClick,
            size = SoilDataBoundaryFallback.AppBar.Size.Medium,
        ),
    ) {
        ContributorsScreen(
            contributors = it,
            onBackClick = onBackClick,
            onContributorItemClick = onContributorClick,
        )
    }
}
