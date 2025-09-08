package io.github.droidkaigi.confsched.repository

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.profile.ProfileDataStore
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.model.profile.ProfileSubscriptionKey
import io.github.droidkaigi.confsched.model.profile.ProfileWithImages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import soil.query.SwrClientPlus
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.rememberSubscription

@Inject
class ProfileRepository(
    private val swrClient: SwrClientPlus,
    private val profileSubscriptionKey: ProfileSubscriptionKey,
    private val profileDataStore: ProfileDataStore,
) {
    @OptIn(ExperimentalSoilQueryApi::class)
    fun profileFlow(): Flow<ProfileWithImages> = moleculeFlow(RecompositionMode.Immediate) {
        soilDataBoundary(state = rememberSubscription(profileSubscriptionKey, client = swrClient))
    }
        .filterNotNull()
        .distinctUntilChanged()
        .catch {
            // Errors thrown inside flow can't be caught on iOS side, so we catch it here.
            emit(ProfileWithImages())
        }

    @Throws(Throwable::class)
    suspend fun save(profile: Profile) {
        profileDataStore.saveProfile(profile)
    }
}
