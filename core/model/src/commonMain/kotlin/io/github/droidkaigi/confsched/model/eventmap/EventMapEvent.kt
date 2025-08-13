package io.github.droidkaigi.confsched.model.eventmap

import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.core.RoomType
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

data class EventMapEvent(
    val name: MultiLangText,
    val room: Room,
    val description: MultiLangText,
    val moreDetailsUrl: String?,
    val message: MultiLangText?,
) {
    companion object
}

fun EventMapEvent.Companion.fakes(): PersistentList<EventMapEvent> = RoomType.entries.map {
    EventMapEvent(
        name = MultiLangText("ランチミートアップ", "Lunch Meetup"),
        room = it.toRoom(),
        description = MultiLangText(
            "様々なテーマごとに集まって、一緒にランチを食べながらお話ししましょう。席に限りがありますので、お弁当受け取り後お早めにお越しください。",
            "Let's gather for lunch and chat about various topics. Seats are limited, so please come soon after receiving your lunch box.",
        ),
        moreDetailsUrl = if (it.ordinal % 2 == 0) {
            "https://2025.droidkaigi.jp/"
        } else {
            null
        },
        message = if (it.ordinal % 3 == 0) {
            MultiLangText(
                "※こちらのイベントは時間が変更されました。",
                "※This event has been rescheduled.",
            )
        } else {
            null
        },
    )
}.toPersistentList()

private fun RoomType.toRoom(): Room = Room(
    id = 0,
    name = this.toRoomName(),
    type = this,
    sort = 0,
)

private fun RoomType.toRoomName(): MultiLangText = when (this) {
    RoomType.RoomF -> MultiLangText("Meerkat", "Meerkat")
    RoomType.RoomG -> MultiLangText("Ladybug", "Ladybug")
    RoomType.RoomH -> MultiLangText("Koala", "Koala")
    RoomType.RoomI -> MultiLangText("Jellyfish", "Jellyfish")
    RoomType.RoomJ -> MultiLangText("Narwhal", "Narwhal")
    RoomType.RoomIJ -> MultiLangText("Jellyfish and Narwhal", "Jellyfish and Narwhal")
}
