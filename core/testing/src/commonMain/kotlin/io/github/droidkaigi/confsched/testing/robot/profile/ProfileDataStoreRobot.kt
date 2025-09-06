package io.github.droidkaigi.confsched.testing.robot.profile

import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.profile.ProfileDataStore
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.model.profile.fake
import io.github.droidkaigi.confsched.testing.robot.profile.ProfileDataStoreRobot.ProfileInputStatus

interface ProfileDataStoreRobot {
    enum class ProfileInputStatus {
        AllNotEntered,
        NoInputOtherThanImage,
        AllEntered,
    }

    suspend fun setupProfileDataStore(status: ProfileInputStatus)
}

@Inject
class DefaultProfileDataStoreRobot(
    private val profileDataStore: ProfileDataStore,
) : ProfileDataStoreRobot {
    override suspend fun setupProfileDataStore(status: ProfileInputStatus) {
        when (status) {
            ProfileInputStatus.AllNotEntered -> Unit
            ProfileInputStatus.NoInputOtherThanImage -> {
                profileDataStore.saveProfile(
                    profile = Profile.fake().copy(
                        imagePath = "",
                    ),
                )
            }
            ProfileInputStatus.AllEntered -> {
                profileDataStore.saveProfile(Profile.fake())
            }
        }
    }
}
