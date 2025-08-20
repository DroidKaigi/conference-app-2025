package io.github.droidkaigi.confsched.contributors.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.previewOverridePainter
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.contributors.Contributor
import io.github.droidkaigi.confsched.model.contributors.fake
import org.jetbrains.compose.ui.tooling.preview.Preview

const val ContributorsItemImageTestTagPrefix = "ContributorsItemImageTestTag:"
const val ContributorsUserNameTextTestTagPrefix = "ContributorsUserNameTextTestTag:"

@Composable
fun ContributorItem(
    contributor: Contributor,
    onClick: (profileUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !contributor.profileUrl.isNullOrEmpty()) {
                contributor.profileUrl?.let(onClick)
            }
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(23.dp),
    ) {
        Image(
            painter = previewOverridePainter(
                previewPainter = { rememberVectorPainter(image = Icons.Default.Person) },
                painter = { rememberAsyncImagePainter(contributor.iconUrl) },
            ),
            contentDescription = contributor.username,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                )
                .testTag("${ContributorsItemImageTestTagPrefix}${contributor.username}"),
        )
        Text(
            text = contributor.username,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag("${ContributorsUserNameTextTestTagPrefix}${contributor.username}"),
        )
    }
}

@Preview
@Composable
private fun ContributorItemPreview() {
    KaigiPreviewContainer {
        ContributorItem(
            contributor = Contributor.fake(),
            onClick = {},
        )
    }
}
