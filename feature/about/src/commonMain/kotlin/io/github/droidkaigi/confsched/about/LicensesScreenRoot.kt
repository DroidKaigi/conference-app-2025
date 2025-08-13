package io.github.droidkaigi.confsched.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import org.jetbrains.compose.resources.ExperimentalResourceApi
import soil.query.compose.rememberQuery

@OptIn(ExperimentalResourceApi::class)
context(screenContext: LicensesScreenContext)
@Composable
fun LicensesScreenRoot(
    onBackClick: () -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.licensesQueryKey),
    ) { licensesJson ->
        val libraries by rememberLibraries(licensesJson)
        LicensesScreen(
            libraries = libraries,
            onBackClick = onBackClick,
        )
    }
}
