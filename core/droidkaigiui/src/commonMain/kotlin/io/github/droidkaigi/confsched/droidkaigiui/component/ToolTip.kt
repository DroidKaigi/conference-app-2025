package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.icon
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.core.RoomType
import io.github.droidkaigi.confsched.model.core.toRoom
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RoomToolTip(
    icon: DrawableResource?,
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    ToolTip(
        icon = icon,
        text = text,
        contentColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(6.dp),
            ),
    )
}

@Composable
fun OutlinedToolTip(
    text: String,
    modifier: Modifier = Modifier,
) {
    ToolTip(
        text = text,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            ),
    )
}

@Composable
private fun ToolTip(
    text: String,
    contentColor: Color,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        icon?.let { icon ->
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = null,
                tint = contentColor,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun ToolTipPreview() {
    KaigiPreviewContainer {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedToolTip("JA")
            OutlinedToolTip("9/11")
            RoomType.entries.forEach { roomType ->
                RoomToolTip(
                    icon = roomType.toRoom().icon,
                    text = roomType.toRoom().name.enTitle,
                    color = roomType.toRoom().roomTheme.primaryColor,
                )
            }
        }
    }
}
