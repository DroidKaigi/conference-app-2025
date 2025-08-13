package io.github.droidkaigi.confsched.droidkaigiui.session

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.model.core.RoomIcon
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.toResDrawable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TimetableItemLangTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        text = text,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 6.dp),
        modifier = modifier,
    )
}

@Composable
internal fun TimetableItemRoomTag(
    icon: DrawableResource?,
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        icon = icon,
        text = text,
        contentColor = color,
        borderColor = color,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 8.dp),
        modifier = modifier,
    )
}

@Composable
fun TimetableItemDateTag(
    dateText: String,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        icon = null,
        text = dateText,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline,
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 6.dp),
        modifier = modifier,
    )
}

@Composable
private fun TimetableItemTag(
    text: String,
    contentColor: Color,
    borderColor: Color,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(contentPadding),
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
        )
    }
}

@Preview
@Composable
private fun TimetableItemLangTagPreview() {
    KaigiPreviewContainer {
        TimetableItemLangTag("JA")
    }
}

@Preview
@Composable
private fun TimetableItemRoomTagPreview() {
    KaigiPreviewContainer {
        TimetableItemRoomTag(
            icon = RoomIcon.Diamond.toResDrawable(),
            text = "HEDGEHOG",
            color = Color(0xFFFF974B),
        )
    }
}

@Preview
@Composable
private fun TimetableItemDateTagPreview() {
    KaigiPreviewContainer {
        TimetableItemDateTag(dateText = "9/11")
    }
}
