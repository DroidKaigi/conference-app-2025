package io.github.confsched.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.confsched.profile.components.ThemeWithShape
import io.github.confsched.profile.components.shapeValue
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.add_image
import io.github.droidkaigi.confsched.profile.clear_button_icon
import io.github.droidkaigi.confsched.profile.create_card
import io.github.droidkaigi.confsched.profile.enter_validate_format
import io.github.droidkaigi.confsched.profile.image
import io.github.droidkaigi.confsched.profile.link
import io.github.droidkaigi.confsched.profile.link_example_text
import io.github.droidkaigi.confsched.profile.nickname
import io.github.droidkaigi.confsched.profile.occupation
import io.github.droidkaigi.confsched.profile.profile_card_edit_description
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.droidkaigi.confsched.profile.select_theme
import io.github.droidkaigi.confsched.profile.url_is_invalid
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.exists
import org.jetbrains.compose.resources.painterResource
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

private val profileSaver: Saver<Profile, Any> = listSaver(
    save = {
        listOf(
            it.nickName,
            it.occupation,
            it.link,
            it.imagePath,
            it.theme.name,
        )
    },
    restore = { list: List<String?> ->
        Profile(
            nickName = list[0] as String,
            occupation = list[1] as String,
            link = list[2] as String,
            imagePath = list[3] as String,
            theme = ProfileCardTheme.valueOf(list[4] as String),
        )
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    initialProfile: Profile?,
    onCreateClick: (Profile) -> Unit,
    modifier: Modifier = Modifier,
) {
    val form: Form<Profile> = rememberForm(
        initialValue = initialProfile ?: Profile(),
        saver = profileSaver,
        onSubmit = onCreateClick,
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(ProfileRes.string.profile_card_title),
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Text(
                text = stringResource(ProfileRes.string.profile_card_edit_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                form.Name()
                form.Occupation()
                form.Link(focusManager = focusManager)
                form.Image()
            }
            form.Theme()
            Button(
                onClick = { form.handleSubmit() },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp),
            ) {
                Text(stringResource(ProfileRes.string.create_card))
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
        selector = { it.nickName },
        updater = { copy(nickName = it) },
        validator = FieldValidator {
            notBlank { emptyNameErrorString }
        },
        render = { field ->
            field.InputField(
                label = stringResource(ProfileRes.string.nickname),
            )
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
            field.InputField(
                label = stringResource(ProfileRes.string.occupation),
            )
        },
    )
}

@Composable
private fun Form<Profile>.Link(focusManager: FocusManager) {
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
            field.InputField(
                label = stringResource(ProfileRes.string.link) + stringResource(ProfileRes.string.link_example_text),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Uri,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                ),
            )
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
        mutableStateOf(
            value.imagePath
                .takeIf { it.isNotBlank() }
                ?.takeIf { path ->
                    path.startsWith("file://") ||
                        path.startsWith("content://") ||
                        !path.contains("://")
                }
                ?.let { safePath ->
                    runCatching { PlatformFile(safePath).takeIf { it.exists() } }.getOrNull()
                },
        )
    }

    Field(
        selector = { it.imagePath },
        updater = { copy(imagePath = it) },
        validator = FieldValidator {
            notBlank { emptyImageErrorString }
        },
        render = { field ->
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                InputLabel(label = stringResource(ProfileRes.string.image))
                ImagePicker(
                    image = image,
                    onImageChange = { file ->
                        try {
                            image = file
                            file.persistPermission()
                            field.onValueChange(file.absolutePath())
                        } catch (e: Throwable) {
                            // accessing the invalid file path may crash on iOS with FileKitNSURLNullPathException
                            println("Failed to load image: ${e.stackTraceToString()}")
                        }
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
private fun Form<Profile>.Theme() {
    Field(
        selector = { it.theme },
        updater = { value.copy(theme = it) },
        render = { field ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                InputLabel(label = stringResource(ProfileRes.string.select_theme))
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ProfileCardTheme.entries
                        .groupBy { it.shapeValue }
                        .forEach { (_, themes) ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                themes.forEach { theme ->
                                    ThemeWithShape(
                                        selected = field.value == theme,
                                        onSelect = {
                                            field.onValueChange(theme)
                                        },
                                        theme = theme,
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                        }
                }
            }
        },
    )
}

@Composable
private fun FormField<String>.InputField(
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        InputLabel(label = label)
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isError = hasError,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(ProfileRes.drawable.clear_button_icon),
                            contentDescription = "clear text",
                        )
                    }
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            supportingText = {
                if (hasError) {
                    Text(
                        text = error.messages.first(),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun InputLabel(
    label: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        modifier = modifier,
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
        OutlinedButton(
            onClick = { launcher.launch() },
            contentPadding = PaddingValues(
                top = 10.dp,
                start = 16.dp,
                end = 24.dp,
                bottom = 10.dp,
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(ProfileRes.string.add_image))
        }
    }
}

@Preview
@Composable
private fun ProfileEditScreenPreview() {
    KaigiPreviewContainer {
        ProfileEditScreen(
            initialProfile = null,
            onCreateClick = {},
        )
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
