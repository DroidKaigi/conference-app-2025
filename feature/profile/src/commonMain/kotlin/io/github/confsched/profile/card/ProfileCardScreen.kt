package io.github.confsched.profile.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.vinceglb.filekit.PlatformFile
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCardScreen(
    profile: Profile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(ProfileRes.string.profile_card_title),
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
        ) {
            Text("Profile Card Screen")
            Text("$profile")
            val painter = rememberAsyncImagePainter(PlatformFile(profile.imagePath))
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp),
            )
            Button(
                onClick = onEditClick,
            ) {
                Text("Edit")
            }
        }
    }
}

@Preview
@Composable
private fun ProfileCardScreenPreview() {
    KaigiPreviewContainer {
        ProfileCardScreen(
            profile = Profile(
                name = "John Doe",
                occupation = "Software Engineer",
                link = "https://example.com",
                imagePath = "https://example.com/image.png",
                image = ByteArray(0),
            ),
            onEditClick = {},
        )
    }
}
