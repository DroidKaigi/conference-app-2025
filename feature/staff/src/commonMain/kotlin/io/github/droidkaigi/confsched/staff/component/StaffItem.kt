package io.github.droidkaigi.confsched.staff.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.SubcomposeAsyncImage
import io.github.droidkaigi.confsched.model.staff.Staff
import io.github.droidkaigi.confsched.model.staff.fakes
import org.jetbrains.compose.ui.tooling.preview.Preview

const val StaffItemImageTestTag = "StaffItemImageTestTag:"
const val StaffItemUserNameTextTestTag = "StaffItemUserNameTextTestTag:"

private val staffIconShape = CircleShape

@Composable
fun StaffItem(
    staff: Staff,
    onStaffItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(enabled = staff.profileUrl.isNotBlank()) {
                onStaffItemClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(23.dp),
    ) {
        SubcomposeAsyncImage(
            model = staff.iconUrl,
            contentDescription = staff.username,
            loading = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = staff.username,
                )
            },
            modifier = Modifier
                .size(52.dp)
                .clip(staffIconShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = staffIconShape,
                )
                .testTag(StaffItemImageTestTag.plus(staff.id)),
        )
        Text(
            text = staff.username,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(StaffItemUserNameTextTestTag.plus(staff.id)),
        )
    }
}

@Composable
@Preview
private fun StaffItemPreview() {
    KaigiPreviewContainer {
        StaffItem(
            staff = Staff.fakes().first(),
            onStaffItemClick = {},
        )
    }
}
