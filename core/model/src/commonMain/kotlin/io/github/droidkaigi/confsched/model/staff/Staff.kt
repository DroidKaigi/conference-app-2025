package io.github.droidkaigi.confsched.model.staff

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

data class Staff(
    val id: Long,
    val username: String,
    val profileUrl: String,
    val iconUrl: String,
) {
    companion object
}

fun Staff.Companion.fakes(): PersistentList<Staff> {
    return (1..20).map {
        Staff(
            id = it.toLong(),
            username = "username $it",
            iconUrl = "https://placehold.jp/150x150.png",
            profileUrl = "https://developer.android.com/",
        )
    }.toPersistentList()
}
