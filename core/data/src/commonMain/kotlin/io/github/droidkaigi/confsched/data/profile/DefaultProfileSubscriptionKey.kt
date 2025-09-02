package io.github.droidkaigi.confsched.data.profile

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.profile.ProfileSubscriptionKey
import io.github.droidkaigi.confsched.model.profile.ProfileWithImages
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.map
import qrcode.QRCode
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultProfileSubscriptionKey(
    private val dataStore: ProfileDataStore,
) : ProfileSubscriptionKey by buildSubscriptionKey(
    id = SubscriptionId("profile"),
    subscribe = {
        dataStore.getProfileOrNull().map { profile ->
            if (profile == null) return@map ProfileWithImages()

            if (profile.imagePath.isEmpty() || profile.link.isEmpty()) return@map ProfileWithImages(profile)

            val qrImageByteArray = QRCode.ofSquares()
                .build(profile.link)
                .renderToBytes()

            val profileImageByteArray = PlatformFile(profile.imagePath).takeIf { it.exists() }?.readBytes()

            ProfileWithImages(
                profile = profile,
                profileImageByteArray = profileImageByteArray,
                qrImageByteArray = qrImageByteArray,
            )
        }
    },
)
