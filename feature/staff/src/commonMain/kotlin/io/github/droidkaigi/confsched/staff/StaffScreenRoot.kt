package io.github.droidkaigi.confsched.staff

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.architecture.AppBarSize
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: StaffScreenContext)
fun StaffScreenRoot(
    onStaffItemClick: (url: String) -> Unit,
    onBackClick: () -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.staffQueryKey),
        fallback = SoilFallbackDefaults.appBar(
            title = stringResource(StaffRes.string.staff_title),
            onBackClick = onBackClick,
            appBarSize = AppBarSize.Medium,
            windowInsets = WindowInsets.safeDrawing,
        ),
    ) {
        StaffScreen(
            staff = it,
            onStaffItemClick = onStaffItemClick,
            onBackClick = onBackClick,
        )
    }
}
