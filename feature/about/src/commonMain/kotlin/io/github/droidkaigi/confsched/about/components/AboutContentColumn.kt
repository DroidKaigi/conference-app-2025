package io.github.droidkaigi.confsched.about.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.staff
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutContentColumn(
    leadingIcon: ImageVector,
    label: String,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(73.dp)
            .clickable(onClick = onClickAction),
    ) {
        Row(
            modifier = Modifier
                .height(72.dp)
                .align(
                    alignment = Alignment.Start,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = label,
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                    ),
                tint = MaterialTheme.colorScheme.primaryFixed,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 16.dp,
                    ),
                color = MaterialTheme.colorScheme.primaryFixed,
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@Preview
@Composable
private fun AboutContentColumnPreview() {
    KaigiPreviewContainer {
        AboutContentColumn(
            leadingIcon = Outlined.SentimentVerySatisfied,
            label = stringResource(AboutRes.string.staff),
            onClickAction = {},
        )
    }
}
