package io.github.droidkaigi.confsched.navkey

import androidx.navigation3.runtime.NavKey
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import kotlinx.serialization.Serializable

@Serializable
data object TimetableNavKey : NavKey

@Serializable
data class TimetableItemDetailNavKey(val id: TimetableItemId) : NavKey

@Serializable
data class SearchNavKey(val openedAtEpochMillis: Long) : NavKey
