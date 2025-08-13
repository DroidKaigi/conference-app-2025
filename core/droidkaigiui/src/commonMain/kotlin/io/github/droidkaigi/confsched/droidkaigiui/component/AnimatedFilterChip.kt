package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AnimatedFilterChip(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        modifier = modifier.padding(top = 8.dp, bottom = 12.dp),
        selected = isSelected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon = {
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally(),
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                )
            }
        },
    )
}

@Preview
@Composable
private fun AnimatedFilterChipPreview_Selected() {
    KaigiPreviewContainer {
        AnimatedFilterChip(
            isSelected = true,
            text = "text",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun AnimatedFilterChipPreview_NotSelected() {
    KaigiPreviewContainer {
        AnimatedFilterChip(
            isSelected = false,
            text = "text",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun AnimatedFilterChipInteractivePreview() {
    var isSelected by remember { mutableStateOf(false) }
    KaigiPreviewContainer {
        AnimatedFilterChip(
            isSelected = isSelected,
            text = "text",
            onClick = { isSelected = !isSelected },
        )
    }
}
