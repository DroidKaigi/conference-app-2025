package io.github.droidkaigi.confsched.contributors.component

import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.contributors.ContributorsRes
import io.github.droidkaigi.confsched.contributors.contributor_person
import io.github.droidkaigi.confsched.contributors.contributor_total
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.rememberIntSaveable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ContributorsCounter(
    totalContributors: Int,
    modifier: Modifier = Modifier,
) {
    var targetValue by rememberIntSaveable(0)
    val animatedTotalContributor by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(
            delayMillis = 300,
            durationMillis = 1000,
            easing = EaseOutQuart,
        ),
    )
    LaunchedEffect(totalContributors) {
        targetValue = totalContributors
    }
    val totalLabel = stringResource(ContributorsRes.string.contributor_total)
    val unit = stringResource(ContributorsRes.string.contributor_person)
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clearAndSetSemantics {
                contentDescription = "$totalLabel $totalContributors $unit"
            },
    ) {
        Text(
            text = totalLabel,
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "$animatedTotalContributor",
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun ContributorsCounterPreview() {
    KaigiPreviewContainer {
        ContributorsCounter(totalContributors = 100)
    }
}
