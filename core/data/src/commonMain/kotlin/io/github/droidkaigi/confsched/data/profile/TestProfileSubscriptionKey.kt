package io.github.droidkaigi.confsched.data.profile

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.profile.ProfileSubscriptionKey
import io.github.droidkaigi.confsched.model.profile.ProfileWithImages
import kotlinx.coroutines.flow.map
import qrcode.QRCode
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey

@ContributesBinding(DataScope::class, replaces = [DefaultProfileSubscriptionKey::class])
@Inject
public class TestProfileSubscriptionKey(
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

            // For testing, we'll provide a dummy image that can be decoded
            val profileImageByteArray = if (profile.imagePath.isNotBlank()) {
                byteArrayOf(
                    0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, // PNG signature
                    0x00, 0x00, 0x00, 0x0D,
                    0x49, 0x48, 0x44, 0x52,
                    0x00, 0x00, 0x00, 0x01,
                    0x00, 0x00, 0x00, 0x01,
                    0x08, 0x06, 0x00, 0x00, 0x00,
                    0x1F, 0x15, 0xC4.toByte(), 0x89.toByte(),
                    0x00, 0x00, 0x00, 0x0A,
                    0x49, 0x44, 0x41, 0x54,
                    0x78.toByte(), 0x9C.toByte(), 0x63, 0x00, 0x01, 0x00, 0x00, 0x05, 0x00, 0x01,
                    0x0D, 0x0A, 0x2D, 0xB4.toByte(),
                    0x00, 0x00, 0x00, 0x00,
                    0x49, 0x45, 0x4E, 0x44,
                    0xAE.toByte(), 0x42, 0x60, 0x82.toByte()
                )
            } else {
                null
            }

            ProfileWithImages(
                profile = profile,
                profileImageByteArray = profileImageByteArray,
                qrImageByteArray = qrImageByteArray,
            )
        }
    },
)
