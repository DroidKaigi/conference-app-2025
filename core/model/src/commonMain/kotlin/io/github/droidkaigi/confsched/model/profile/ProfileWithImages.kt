package io.github.droidkaigi.confsched.model.profile

data class ProfileWithImages(
    val profile: Profile? = null,
    // Use ByteArray instead of ImageBitmap so that the image data can also be decoded on iOS side
    val profileImageByteArray: ByteArray? = null,
    val qrImageByteArray: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ProfileWithImages

        if (profile != other.profile) return false
        if (!profileImageByteArray.contentEquals(other.profileImageByteArray)) return false
        if (!qrImageByteArray.contentEquals(other.qrImageByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = profile?.hashCode() ?: 0
        result = 31 * result + (profileImageByteArray?.contentHashCode() ?: 0)
        result = 31 * result + (qrImageByteArray?.contentHashCode() ?: 0)
        return result
    }
}
