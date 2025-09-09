package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.sessions.SearchScreenEvent
import io.github.droidkaigi.confsched.sessions.SearchScreenUiState
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.filter_chip_category
import io.github.droidkaigi.confsched.sessions.filter_chip_day
import io.github.droidkaigi.confsched.sessions.filter_chip_supported_language
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

const val SearchFilterRowTestTag = "SearchFilterRowTestTag"
const val SearchFilterRowFilterDayChipTestTag = "SearchFilterRowFilterDayChipTestTag"
const val SearchFilterRowFilterCategoryChipTestTag = "SearchFilterRowFilterCategoryChipTestTag"
const val SearchFilterRowFilterSessionTypeChipTestTag = "SearchFilterRowFilterSessionTypeChipTestTag"
const val SearchFilterRowFilterLanguageChipTestTag = "SearchFilterRowFilterLanguageChipTestTag"

const val DropdownFilterChipTestTagPrefix = "DropdownFilterChipTestTag:"

@Composable
fun SearchFilterRow(
    filters: SearchScreenUiState.Filters,
    onFilterToggle: (SearchScreenEvent.Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag(SearchFilterRowTestTag),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Day filter dropdown
        if (filters.availableDays.isNotEmpty()) {
            FilterDropdown(
                label = stringResource(SessionsRes.string.filter_chip_day),
                selectedItems = filters.selectedDays,
                items = filters.availableDays,
                itemLabel = { it.monthAndDay() },
                onItemSelected = { day ->
                    onFilterToggle(SearchScreenEvent.Filter.Day(day))
                },
                modifier = Modifier.testTag(SearchFilterRowFilterDayChipTestTag),
            )
        }

        // Category filter dropdown
        if (filters.availableCategories.isNotEmpty()) {
            FilterDropdown(
                label = stringResource(SessionsRes.string.filter_chip_category),
                selectedItems = filters.selectedCategories,
                items = filters.availableCategories,
                itemLabel = { it.title.currentLangTitle },
                onItemSelected = { category ->
                    onFilterToggle(SearchScreenEvent.Filter.Category(category))
                },
                modifier = Modifier.testTag(SearchFilterRowFilterCategoryChipTestTag),
            )
        }
        // Language filter dropdown
        if (filters.availableLanguages.isNotEmpty()) {
            FilterDropdown(
                label = stringResource(SessionsRes.string.filter_chip_supported_language),
                selectedItems = filters.selectedLanguages,
                items = filters.availableLanguages,
                itemLabel = { it.name },
                onItemSelected = { language ->
                    onFilterToggle(SearchScreenEvent.Filter.Language(language))
                },
                modifier = Modifier.testTag(SearchFilterRowFilterLanguageChipTestTag),
            )
        }
    }
}

@Composable
private fun <T> FilterDropdown(
    label: String,
    selectedItems: List<T>,
    items: List<T>,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit,
    onMultiSelectFinished: (List<T>) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val haptics = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }
    var isMultiSelectMode by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var currentChipWidthPx by remember { mutableStateOf(0) }
    var lockedChipWidthDp by remember { mutableStateOf<Dp?>(null) }

    Box(modifier = modifier) {
        FilterChip(
            selected = selectedItems.isNotEmpty(),
            onClick = {
                scope.launch {
                    withFrameNanos {
                        keyboardController?.hide()
                    }
                }.invokeOnCompletion { expanded = true }
            },
            modifier = Modifier
                .then(
                    if (lockedChipWidthDp != null) {
                        Modifier.width(lockedChipWidthDp!!)
                    } else {
                        Modifier
                    },
                )
                .onGloballyPositioned { cords ->
                    currentChipWidthPx = cords.size.width
                    if (expanded && lockedChipWidthDp == null) {
                        lockedChipWidthDp = with(density) { currentChipWidthPx.toDp() }
                    }
                },
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.alpha(if (selectedItems.isNotEmpty()) 1f else 0f),
                    )
                    Text(
                        text = if (selectedItems.isNotEmpty()) {
                            selectedItems.joinToString { itemLabel(it) }
                        } else {
                            label
                        },
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                if (isMultiSelectMode) onMultiSelectFinished(selectedItems.toList())
                expanded = false
                isMultiSelectMode = false
                lockedChipWidthDp = null
            },
        ) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .testTag(DropdownFilterChipTestTagPrefix.plus(item))
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .combinedClickable(
                            onClick = {
                                onItemSelected(item)
                                if (!isMultiSelectMode) {
                                    expanded = false
                                }
                            },
                            onLongClick = {
                                isMultiSelectMode = true
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onItemSelected(item)
                            },
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .alpha(if (selectedItems.contains(item)) 1f else 0f),
                    )
                    Text(itemLabel(item), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
