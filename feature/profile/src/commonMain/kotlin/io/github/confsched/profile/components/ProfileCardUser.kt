package io.github.confsched.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.changoFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoRegularFontFamily
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileCardUser(
    isDarkTheme: Boolean,
    profileImageBitmap: ImageBitmap,
    userName: String,
    occupation: String,
    profileShape: Shape,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            bitmap = profileImageBitmap,
            contentDescription = "User Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(profileShape),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = occupation,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black,
            fontFamily = robotoRegularFontFamily(),
            maxLines = 1,
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            color = if (isDarkTheme) Color.White else Color.Black,
            fontFamily = changoFontFamily(),
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun ProfileCardUser() {
    KaigiPreviewContainer {
        ProfileCardUser(
            isDarkTheme = true,
            profileImageBitmap = CardPreviewImageBitmaps.profileImage,
            profileShape = ProfileCardTheme.DarkPill.shape,
            userName = "DroidKaigi",
            occupation = "Software Engineer",
        )
    }
}
