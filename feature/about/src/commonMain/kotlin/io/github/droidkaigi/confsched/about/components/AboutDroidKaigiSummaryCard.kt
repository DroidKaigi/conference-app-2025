package io.github.droidkaigi.confsched.about.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.date_description
import io.github.droidkaigi.confsched.about.date_title
import io.github.droidkaigi.confsched.about.place_description
import io.github.droidkaigi.confsched.about.place_link
import io.github.droidkaigi.confsched.about.place_title
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutDroidKaigiSummaryCard(
    modifier: Modifier = Modifier,
    onViewMapClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            AboutDroidKaigiSummaryCardRow(
                leadingIcon = Outlined.Schedule,
                label = stringResource(AboutRes.string.date_title),
                content = stringResource(AboutRes.string.date_description),
            )
            val placeContent = stringResource(AboutRes.string.place_description)
                .plus(" " + stringResource(AboutRes.string.place_link))
            AboutDroidKaigiSummaryCardRow(
                leadingIcon = Outlined.Place,
                label = stringResource(AboutRes.string.place_title),
                content = placeContent,
                onLinkClick = { _ -> onViewMapClick() },
            )
        }
    }
}

@Composable
private fun AboutDroidKaigiSummaryCardRow(
    leadingIcon: ImageVector,
    label: String,
    content: String,
    modifier: Modifier = Modifier,
    leadingIconContentDescription: String? = null,
    onLinkClick: (url: String) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = leadingIconContentDescription,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.width(12.dp))
        ClickableLinkText(
            style = MaterialTheme.typography.titleSmall,
            content = content,
            onLinkClick = onLinkClick,
            hasUnderLine = false,
            underLineAndLinkTextColor = MaterialTheme.colorScheme.primary,
            regex = stringResource(AboutRes.string.place_link).toRegex(),
        )
    }
}

@Preview
@Composable
private fun AboutDroidKaigiSummaryCardPreview() {
    KaigiPreviewContainer {
        AboutDroidKaigiSummaryCard(
            onViewMapClick = {},
        )
    }
}
