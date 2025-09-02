package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.about_header
import io.github.droidkaigi.confsched.about.components.AboutDroidKaigiSummaryCard
import io.github.droidkaigi.confsched.about.description
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val AboutHeaderTestTag = "AboutHeaderTestTag"

@Composable
fun AboutDroidKaigiHeader(
    modifier: Modifier = Modifier,
    onViewMapClick: () -> Unit,
) {
    Column(
        modifier = modifier.testTag(AboutHeaderTestTag),
    ) {
        Image(
            painter = painterResource(AboutRes.drawable.about_header),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(AboutRes.string.description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                ),
        )
        AboutDroidKaigiSummaryCard(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                ),
            onViewMapClick = onViewMapClick,
        )
    }
}

@Preview
@Composable
private fun AboutDroidKaigiHeaderPreview() {
    KaigiPreviewContainer {
        AboutDroidKaigiHeader(
            onViewMapClick = {},
        )
    }
}
