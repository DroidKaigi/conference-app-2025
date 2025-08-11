package io.github.droidkaigi.confsched.staff

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultErrorFallback
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallback
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
        suspenseFallback = {
            DefaultSuspenseFallback(
                title = stringResource(StaffRes.string.staff_title),
                onBackClick = onBackClick,
            )
        },
        errorFallback = {
            DefaultErrorFallback(
                errorBoundaryContext = it,
                title = stringResource(StaffRes.string.staff_title),
                onBackClick = onBackClick,
            )
        }
    ) {
        StaffScreen(
            staff = it,
            onStaffItemClick = onStaffItemClick,
            onBackClick = onBackClick,
        )
    }
}
