package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.image
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val TimetableItemDetailMessageRowTestTag = "TimetableItemDetailMessageRowTestTag"
const val TimetableItemDetailMessageRowTextTestTag = "TimetableItemDetailMessageRowTextTestTag"

@Composable
fun TimetableItemDetailAnnounceMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .testTag(TimetableItemDetailMessageRowTestTag),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier.fillMaxHeight(),
            imageVector = Icons.Filled.Info,
            contentDescription = stringResource(SessionsRes.string.image),
            tint = MaterialTheme.colorScheme.error,
        )
        Text(
            modifier = Modifier.testTag(TimetableItemDetailMessageRowTextTestTag),
            text = message,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Preview
@Composable
private fun TimetableItemDetailAnnounceMessagePreview() {
    KaigiPreviewContainer {
        TimetableItemDetailAnnounceMessage(message = "このセッションは事情により中止となりました")
    }
}
