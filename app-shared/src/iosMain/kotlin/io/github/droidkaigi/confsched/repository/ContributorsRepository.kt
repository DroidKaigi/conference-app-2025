package io.github.droidkaigi.confsched.repository

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.model.contributors.Contributor
import io.github.droidkaigi.confsched.model.contributors.ContributorsQueryKey
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import soil.query.SwrClientPlus
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.rememberQuery

@Inject
class ContributorsRepository(
    private val swrClient: SwrClientPlus,
    private val contributorsQueryKey: ContributorsQueryKey,
) {
    @OptIn(ExperimentalSoilQueryApi::class, FlowPreview::class)
    fun contributorsFlow(): Flow<PersistentList<Contributor>> = moleculeFlow(RecompositionMode.Immediate) {
        soilDataBoundary(
            state = rememberQuery(
                key = contributorsQueryKey,
                client = swrClient,
            )
        )
    }
        .filterNotNull()
        // Errors thrown inside flow can't be caught on iOS side, so we catch it here.
        .catch { emit(persistentListOf()) }
}
