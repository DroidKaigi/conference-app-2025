package io.github.droidkaigi.confsched.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.util.strippedLicenseContent
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScreen(
    libraries: Libs?,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(AboutRes.string.oss_licenses),
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LibrariesContainer(
            libraries = libraries,
            contentPadding = innerPadding,
            textStyles = LibraryDefaults.libraryTextStyles(
                nameOverflow = TextOverflow.Visible,
                nameMaxLines = 3,
            ),
            licenseDialogBody = { library ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = library.name,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(library.description ?: "")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(library.strippedLicenseContent)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        )
    }
}
