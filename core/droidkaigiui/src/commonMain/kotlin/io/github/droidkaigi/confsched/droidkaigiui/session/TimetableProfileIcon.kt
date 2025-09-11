package io.github.droidkaigi.confsched.droidkaigiui.session

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.droidkaigiui.DroidkaigiuiRes
import io.github.droidkaigi.confsched.droidkaigiui.SubcomposeAsyncImage
import io.github.droidkaigi.confsched.droidkaigiui.fallback_profile_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimetableProfileIcon(
    speakerUrl: String,
    modifier: Modifier = Modifier,
) {
    if (speakerUrl.isEmpty()) {
        Image(
            painter = painterResource(DroidkaigiuiRes.drawable.fallback_profile_icon),
            contentDescription = null,
            modifier = modifier,
        )
    } else {
        SubcomposeAsyncImage(
            model = speakerUrl,
            contentDescription = null,
            modifier = modifier,
        )
    }
}
