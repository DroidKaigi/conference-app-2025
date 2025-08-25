package io.github.confsched.profile.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.confsched.profile.persistPermission
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.add_image
import io.github.droidkaigi.confsched.profile.enter_validate_format
import io.github.droidkaigi.confsched.profile.image
import io.github.droidkaigi.confsched.profile.link
import io.github.droidkaigi.confsched.profile.nickname
import io.github.droidkaigi.confsched.profile.occupation
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.droidkaigi.confsched.profile.url_is_invalid
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.exists
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import soil.form.FieldValidator
import soil.form.compose.Field
import soil.form.compose.Form
import soil.form.compose.FormField
import soil.form.compose.hasError
import soil.form.compose.rememberForm
import soil.form.rule.match
import soil.form.rule.notBlank

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    form: Form<Profile>,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(contentPadding),
        ) {
            form.Name()
            form.Occupation()
            form.Link()
            form.Image()
            Button(
                onClick = { form.handleSubmit() },
            ) {
                Text("Create")
            }
        }
    }
}

@Composable
private fun Form<Profile>.Name() {
    val emptyNameErrorString = stringResource(
        ProfileRes.string.enter_validate_format,
        stringResource(ProfileRes.string.nickname),
    )
    Field(
        selector = { it.name },
        updater = {
            copy(name = it)
        },
        validator = FieldValidator {
            notBlank { emptyNameErrorString }
        },
        render = { field ->
            field.InputField {
                Text("Name")
            }
        },
    )
}

@Composable
private fun Form<Profile>.Occupation() {
    val emptyOccupationErrorString = stringResource(
        ProfileRes.string.enter_validate_format,
        stringResource(ProfileRes.string.occupation),
    )
    Field(
        selector = { it.occupation },
        updater = { copy(occupation = it) },
        validator = FieldValidator {
            notBlank { emptyOccupationErrorString }
        },
        render = { field ->
            field.InputField {
                Text("Occupation")
            }
        },
    )
}

@Composable
private fun Form<Profile>.Link() {
    val emptyLinkErrorString = stringResource(
        ProfileRes.string.enter_validate_format,
        stringResource(ProfileRes.string.link),
    )
    val invalidLinkErrorString = stringResource(
        ProfileRes.string.url_is_invalid,
    )
    val linkPattern = Regex("^(?:https?://)?(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9-]{2,}(?:/\\S*)?$")
    Field(
        selector = { it.link },
        updater = { copy(link = it) },
        validator = FieldValidator {
            notBlank { emptyLinkErrorString }
            match(linkPattern) { invalidLinkErrorString }
        },
        render = { field ->
            field.InputField {
                Text("Link")
            }
        },
    )
}

@Composable
private fun Form<Profile>.Image() {
    val emptyImageErrorString = stringResource(
        ProfileRes.string.enter_validate_format,
        stringResource(ProfileRes.string.image),
    )
    var image: PlatformFile? by remember(value.imagePath) {
        mutableStateOf(PlatformFile(value.imagePath).takeIf { it.exists() })
    }

    Field(
        selector = { it.imagePath },
        updater = { copy(imagePath = it) },
        validator = FieldValidator {
            notBlank { emptyImageErrorString }
        },
        render = { field ->
            Column {
                ImagePicker(
                    image = image,
                    onImageChange = { file ->
                        image = file
                        file.persistPermission()
                        field.onValueChange(file.absolutePath())
                    },
                    onClear = {
                        image = null
                        field.onValueChange("")
                    },
                )
                if (field.hasError) {
                    Text(
                        text = field.error.messages.first(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
    )

    // FIXME: Replace the ByteArray version once the following bug is fixed.
    //   https://github.com/vinceglb/FileKit/issues/346
    // var image: PlatformFile? by remember {
    //     val file = if (value.image.isNotEmpty()) {
    //         PlatformFile.fromBookmarkData(value.image)
    //     } else {
    //         null
    //     }
    //     mutableStateOf(file)
    // }
    // val coroutineScope = rememberCoroutineScope()
    // Field(
    //     selector = { it.image },
    //     updater = { copy(image = it) },
    //     validator = FieldValidator {
    //         notEmpty { emptyImageErrorString }
    //     },
    //     render = { field ->
    //         Column {
    //             ImagePicker(
    //                 image = image,
    //                 onImageChange = { file ->
    //                     image = file
    //                     coroutineScope.launch {
    //                         try {
    //                             val bookmark = file.bookmarkData() // <-- Throw the exception
    //                             field.onValueChange(bookmark.bytes)
    //                         } catch (e: CancellationException) {
    //                             throw e
    //                         } catch (e: Exception) {
    //                             field.onValueChange(ByteArray(0))
    //                             println("Failed to load image: ${e.stackTraceToString()}")
    //                         }
    //                     }
    //                 },
    //                 onClear = {
    //                     image = null
    //                     field.onValueChange(ByteArray(0))
    //                 }
    //             )
    //             if (field.hasError) {
    //                 Text(
    //                     text = field.error.messages.first(),
    //                     color = MaterialTheme.colorScheme.error,
    //                     style = MaterialTheme.typography.bodySmall,
    //                 )
    //             }
    //         }
    //     }
    // )
}

@Composable
private fun FormField<String>.InputField(
    label: @Composable () -> Unit,
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = label,
        isError = hasError,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        supportingText = {
            if (hasError) {
                Text(
                    text = error.messages.first(),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@Composable
private fun ImagePicker(
    image: PlatformFile?,
    onImageChange: (file: PlatformFile) -> Unit,
    onClear: () -> Unit,
) {
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
    ) { file ->
        file?.let { file ->
            onImageChange(file)
        }
    }
    if (image != null) {
        Box(
            modifier = Modifier
                .size(120.dp),
        ) {
            val painter = rememberAsyncImagePainter(image)
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(2.dp)),
            )
            IconButton(
                onClick = onClear,
                colors = IconButtonDefaults
                    .iconButtonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp)
                    .padding(8.dp)
                    .offset(x = 16.dp, y = (-16).dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp),
                )
            }
        }
    } else {
        Button(onClick = { launcher.launch() }) {
            Text(stringResource(ProfileRes.string.add_image))
        }
    }
}

@Preview
@Composable
private fun ProfileEditScreenPreview() {
    val form = rememberForm(
        initialValue = Profile(
            name = "John Doe",
            occupation = "Software Engineer",
            link = "https://example.com",
            imagePath = "https://example.com/image.jpg",
            image = ByteArray(0),
        ),
        onSubmit = {},
    )
    KaigiPreviewContainer {
        ProfileEditScreen(form = form)
    }
}

@Preview
@Composable
private fun ImagePickerPreview() {
    KaigiPreviewContainer {
        ImagePicker(
            image = PlatformFile(""),
            onImageChange = {},
            onClear = {},
        )
    }
}
