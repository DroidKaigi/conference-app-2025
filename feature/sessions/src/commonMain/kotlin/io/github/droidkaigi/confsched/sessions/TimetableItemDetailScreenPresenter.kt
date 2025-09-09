package io.github.droidkaigi.confsched.sessions

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.stringResource
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableSessionType
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.PersistentSet
import soil.query.compose.rememberMutation

@Composable
context(screenContext: TimetableItemDetailScreenContext)
fun timetableItemDetailScreenPresenter(
    eventFlow: EventFlow<TimetableItemDetailScreenEvent>,
    timetableItem: TimetableItem,
    favoriteTimetableItemIds: PersistentSet<TimetableItemId>,
    snackbarHostState: SnackbarHostState,
): TimetableItemDetailScreenUiState = providePresenterDefaults {
    val favoriteTimetableItemIdMutation = rememberMutation(screenContext.favoriteTimetableItemIdMutationKey)
    var selectedLang by rememberRetained { mutableStateOf(Lang.valueOf(timetableItem.language.langOfSpeaker)) }
    var previousBookmarkState by rememberRetained { mutableStateOf(favoriteTimetableItemIds.contains(timetableItem.id)) }
    val currentIsBookmarked = favoriteTimetableItemIds.contains(timetableItem.id)
    val bookmarkedSuccessfullyMessage = stringResource(SessionsRes.string.bookmarked_successfully)
    val viewBookmarkListLabel = stringResource(SessionsRes.string.view_bookmark_list)

    LaunchedEffect(currentIsBookmarked) {
        if (!previousBookmarkState && currentIsBookmarked) {
            snackbarHostState.currentSnackbarData?.dismiss()

            snackbarHostState.showSnackbar(
                message = bookmarkedSuccessfullyMessage,
                actionLabel = viewBookmarkListLabel,
                duration = SnackbarDuration.Short,
            )
        }
        previousBookmarkState = currentIsBookmarked
    }

    EventEffect(eventFlow) { event ->
        when (event) {
            is TimetableItemDetailScreenEvent.Bookmark -> {
                favoriteTimetableItemIdMutation.mutate(timetableItem.id)
            }

            is TimetableItemDetailScreenEvent.LanguageSelect -> {
                selectedLang = event.lang
            }
        }
    }

    TimetableItemDetailScreenUiState(
        timetableItem = timetableItem,
        isBookmarked = favoriteTimetableItemIds.contains(timetableItem.id),
        currentLang = selectedLang,
        isLangSelectable = timetableItem.sessionType == TimetableSessionType.NORMAL,
    )
}
