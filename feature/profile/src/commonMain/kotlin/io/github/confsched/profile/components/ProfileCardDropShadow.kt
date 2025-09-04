package io.github.confsched.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileCardDropShadow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(3.dp, 3.dp)
                .blur(10.dp, BlurredEdgeTreatment.Unbounded)
                .background(Color.Black.copy(alpha = 0.12f)),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(11.dp, 14.dp)
                .blur(17.dp, BlurredEdgeTreatment.Unbounded)
                .background(Color.Black.copy(alpha = 0.11f)),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(24.dp, 31.dp)
                .blur(24.dp, BlurredEdgeTreatment.Unbounded)
                .background(Color.Black.copy(alpha = 0.06f)),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(42.dp, 55.dp)
                .blur(28.dp, BlurredEdgeTreatment.Unbounded)
                .background(Color.Black.copy(alpha = 0.02f)),
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileCardDropShadowPreview() {
    ProfileCardDropShadow(
        modifier = Modifier.padding(64.dp),
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(380.dp)
                .clip(
                    shape = RoundedCornerShape(16.dp),
                )
                .background(color = Color.Gray),
        )
    }
}
