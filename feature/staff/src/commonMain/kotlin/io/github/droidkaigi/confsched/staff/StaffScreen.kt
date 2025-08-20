package io.github.droidkaigi.confsched.staff

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.model.staff.Staff
import io.github.droidkaigi.confsched.model.staff.fakes
import io.github.droidkaigi.confsched.staff.component.StaffItem
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val StaffScreenLazyColumnTestTag = "StaffScreenLazyColumnTestTag"
const val StaffItemTestTagPrefix = "StaffItemTestTag:"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    staff: PersistentList<Staff>,
    onStaffItemClick: (url: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            AnimatedMediumTopAppBar(
                title = stringResource(StaffRes.string.staff_title),
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .testTag(StaffScreenLazyColumnTestTag),
            contentPadding = contentPadding,
        ) {
            items(
                items = staff,
                key = { it.id },
            ) { staff ->
                StaffItem(
                    staff = staff,
                    onStaffItemClick = { staff.profileUrl?.let(onStaffItemClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(StaffItemTestTagPrefix.plus(staff.id)),
                )
            }
        }
    }
}

@Preview
@Composable
private fun StaffScreenPreview() {
    KaigiPreviewContainer {
        StaffScreen(
            staff = Staff.fakes(),
            onStaffItemClick = {},
            onBackClick = {},
        )
    }
}
