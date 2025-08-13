package io.github.droidkaigi.confsched.data.core

import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.core.RoomType
import kotlinx.serialization.Serializable

@Serializable
public data class RoomResponse(
    val name: LocaledResponse,
    val id: Int,
    val sort: Int,
)

public fun RoomResponse.toRoom(): Room {
    val roomType = when (name.en.lowercase()) {
        "meerkat" -> RoomType.RoomF
        "ladybug" -> RoomType.RoomG
        "koala" -> RoomType.RoomH
        "jellyfish" -> RoomType.RoomI
        "narwhal" -> RoomType.RoomJ
        else -> RoomType.RoomIJ // TODO: investigate what RoomIJ is later on
    }

    return Room(
        id = id,
        name = MultiLangText(jaTitle = name.ja, enTitle = name.en),
        type = roomType,
        sort = sort,
    )
}
