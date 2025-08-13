package io.github.droidkaigi.confsched.favorites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedFilterChip
import io.github.droidkaigi.confsched.favorites.FavoritesRes
import io.github.droidkaigi.confsched.favorites.filter_all
import io.github.droidkaigi.confsched.favorites.filter_day1
import io.github.droidkaigi.confsched.favorites.filter_day2
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteFilters(
    allFilterSelected: Boolean,
    day1FilterSelected: Boolean,
    day2FilterSelected: Boolean,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AnimatedFilterChip(
            isSelected = allFilterSelected,
            onClick = onAllFilterChipClick,
            text = stringResource(FavoritesRes.string.filter_all),
        )
        AnimatedFilterChip(
            isSelected = day1FilterSelected,
            onClick = onDay1FilterChipClick,
            text = stringResource(FavoritesRes.string.filter_day1),
        )
        AnimatedFilterChip(
            isSelected = day2FilterSelected,
            onClick = onDay2FilterChipClick,
            text = stringResource(FavoritesRes.string.filter_day2),
        )
    }
}

@Composable
@Preview
private fun FavoriteFiltersPreview() {
    KaigiPreviewContainer {
        FavoriteFilters(
            allFilterSelected = false,
            day1FilterSelected = true,
            day2FilterSelected = true,
            onAllFilterChipClick = {},
            onDay1FilterChipClick = {},
            onDay2FilterChipClick = {},
        )
    }
}
