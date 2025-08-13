package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.app_version
import io.github.droidkaigi.confsched.about.components.AboutLinkIcon
import io.github.droidkaigi.confsched.about.content_description_youtube
import io.github.droidkaigi.confsched.about.icon_medium
import io.github.droidkaigi.confsched.about.icon_x
import io.github.droidkaigi.confsched.about.icon_youtube
import io.github.droidkaigi.confsched.about.license_description
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutFooter(
    versionName: String,
    onYouTubeClick: () -> Unit,
    onXClick: () -> Unit,
    onMediumClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AboutLinkIcon(
                painter = painterResource(AboutRes.drawable.icon_youtube),
                contentDescription = stringResource(AboutRes.string.content_description_youtube),
                onClick = onYouTubeClick,
            )
            AboutLinkIcon(
                painter = painterResource(AboutRes.drawable.icon_x),
                contentDescription = "X",
                onClick = onXClick,
            )
            AboutLinkIcon(
                painter = painterResource(AboutRes.drawable.icon_medium),
                contentDescription = "Medium",
                onClick = onMediumClick,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(AboutRes.string.app_version),
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = versionName,
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = stringResource(AboutRes.string.license_description),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun AboutFooterPreview() {
    KaigiPreviewContainer {
        AboutFooter(
            versionName = "1.2",
            onYouTubeClick = {},
            onXClick = {},
            onMediumClick = {},
        )
    }
}
