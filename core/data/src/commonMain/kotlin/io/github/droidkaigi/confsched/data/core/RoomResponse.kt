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
        "jellyfish" -> RoomType.RoomJ
        "koala" -> RoomType.RoomK
        "ladybug" -> RoomType.RoomL
        "meerkat" -> RoomType.RoomM
        "narwhal" -> RoomType.RoomN
        else -> throw IllegalArgumentException("This is an unexpected RoomType.")
    }

    return Room(
        id = id,
        name = MultiLangText(jaTitle = name.ja, enTitle = name.en),
        type = roomType,
        sort = sort,
    )
}
