package io.github.confsched.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.enter_validate_format
import io.github.droidkaigi.confsched.profile.link
import io.github.droidkaigi.confsched.profile.nickname
import io.github.droidkaigi.confsched.profile.occupation
import io.github.droidkaigi.confsched.profile.profile_card_title
import io.github.droidkaigi.confsched.profile.url_is_invalid
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
        modifier = modifier
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            form.Name()
            form.Occupation()
            form.Link()
            // TODO: implement Image Field
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
        }
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
        }
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
        }
    )
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
        supportingText = {
            if (hasError) {
                Text(
                    text = error.messages.first(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}


@Preview
@Composable
private fun ProfileEditScreenPreview() {
    val form = rememberForm(
        initialValue = Profile(
            name = "John Doe",
            occupation = "Software Engineer",
            link = "https://example.com",
        ),
        onSubmit = {}
    )
    KaigiPreviewContainer {
        ProfileEditScreen(form = form)
    }
}
