package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.toResDrawable
import io.github.droidkaigi.confsched.model.core.RoomIcon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableItemTag(
    tagText: String,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        tagText = tagText,
        contentTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline,
        modifier = modifier,
    )
}

@Composable
fun TimetableItemTag(
    tagText: String,
    tagColor: Color,
    icon: DrawableResource?,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        tagText = tagText,
        contentTextColor = tagColor,
        borderColor = tagColor,
        icon = icon,
        modifier = modifier,
    )
}

@Composable
fun TimetableItemTag(
    tagText: String,
    contentTextColor: Color,
    borderColor: Color,
    contentBackgroundColor: Color? = null,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(5.dp),
            )
            .then(
                Modifier.background(
                    color = contentBackgroundColor ?: Color.Transparent,
                    shape = RoundedCornerShape(5.dp),
                ).takeIf { contentBackgroundColor != null } ?: Modifier,
            )
            .padding(contentPadding),
    ) {
        icon?.let { icon ->
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = null,
                tint = contentTextColor,
            )
        }
        Text(
            text = tagText,
            style = MaterialTheme.typography.labelMedium,
            color = contentTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun TimeTableItemTagPreview() {
    KaigiPreviewContainer {
        TimetableItemTag(
            tagText = "tag",
            tagColor = MaterialTheme.colorScheme.outline,
            icon = RoomIcon.Diamond.toResDrawable(),
        )
    }
}
