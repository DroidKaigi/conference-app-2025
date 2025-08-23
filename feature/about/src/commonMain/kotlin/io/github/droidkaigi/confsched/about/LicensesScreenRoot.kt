package io.github.droidkaigi.confsched.about

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
context(screenContext: LicensesScreenContext)
fun LicensesScreenRoot(
    onBackClick: () -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.licensesQueryKey),
        fallback = SoilFallbackDefaults.appBar(
            title = stringResource(AboutRes.string.oss_licenses),
            onBackClick = onBackClick,
            windowInsets = WindowInsets.safeDrawing,
        ),
    ) { licensesJson ->
        val libraries by rememberLibraries { licensesJson }
        LicensesScreen(
            libraries = libraries,
            onBackClick = onBackClick,
        )
    }
}
