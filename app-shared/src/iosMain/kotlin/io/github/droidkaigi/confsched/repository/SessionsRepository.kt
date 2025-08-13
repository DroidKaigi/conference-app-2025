package io.github.droidkaigi.confsched.repository

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.model.data.TimetableQueryKey
import io.github.droidkaigi.confsched.model.sessions.Timetable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import soil.query.SwrClientPlus
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.rememberQuery
import soil.query.compose.rememberSubscription

@Inject
class SessionsRepository(
    private val swrClient: SwrClientPlus,
    private val timetableQueryKey: TimetableQueryKey,
    private val favoriteTimetableIdsSubscriptionKey: FavoriteTimetableIdsSubscriptionKey,
) {
    @OptIn(ExperimentalSoilQueryApi::class, FlowPreview::class)
    fun timetableFlow(): Flow<Timetable> = moleculeFlow(RecompositionMode.Immediate) {
        soilDataBoundary(
            state1 = rememberQuery(key = timetableQueryKey, client = swrClient),
            state2 = rememberSubscription(key = favoriteTimetableIdsSubscriptionKey, client = swrClient),
        ) { timetable, favoriteTimetableIds ->
            timetable.copy(bookmarks = favoriteTimetableIds)
        }
    }
        .filterNotNull()
        .catch {
            // Errors thrown inside flow can't be caught on iOS side, so we catch it here.
            emit(Timetable())
        }
}
