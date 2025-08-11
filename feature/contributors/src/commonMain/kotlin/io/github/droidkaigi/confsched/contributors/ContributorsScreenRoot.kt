package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultErrorFallback
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallback
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
        suspenseFallback = {
            DefaultSuspenseFallback(
                title = stringResource(ContributorsRes.string.contributor_title),
                onBackClick = onBackClick,
            )
        },
        errorFallback = {
            DefaultErrorFallback(
                errorBoundaryContext = it,
                title = stringResource(ContributorsRes.string.contributor_title),
                onBackClick = onBackClick,
            )
        },
    ) {
        ContributorsScreen(
            contributors = it,
            onBackClick = onBackClick,
            onContributorItemClick = onContributorClick,
        )
    }
}
