package io.github.droidkaigi.confsched.staff

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundaryDefaults
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundaryFallback
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery

context(screenContext: StaffScreenContext)
@Composable
fun StaffScreenRoot(
    onStaffItemClick: (url: String) -> Unit,
    onBackClick: () -> Unit,
) {
    SoilDataBoundary(
        state = rememberQuery(screenContext.staffQueryKey),
        fallback = SoilDataBoundaryDefaults.appBarFallback(
            title = stringResource(StaffRes.string.staff_title),
            onBackClick = onBackClick,
            size = SoilDataBoundaryFallback.AppBar.Size.Medium,
        ),
    ) {
        StaffScreen(
            staff = it,
            onStaffItemClick = onStaffItemClick,
            onBackClick = onBackClick,
        )
    }
}
