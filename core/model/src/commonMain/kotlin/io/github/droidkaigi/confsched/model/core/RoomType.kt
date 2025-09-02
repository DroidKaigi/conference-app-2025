package io.github.droidkaigi.confsched.model.core

/**
 * Please use [Room] to get room information.
 */
enum class RoomType {
    RoomJ,
    RoomK,
    RoomL,
    RoomM,
    RoomN,
}

fun RoomType.toRoom(): Room = when (this) {
    RoomType.RoomJ -> Room(1, MultiLangText("JELLYFISH", "JELLYFISH"), this, 4)
    RoomType.RoomK -> Room(2, MultiLangText("KOALA", "KOALA"), this, 5)
    RoomType.RoomL -> Room(3, MultiLangText("LADYBUG", "LADYBUG"), this, 1)
    RoomType.RoomM -> Room(4, MultiLangText("MEERKAT", "MEERKAT"), this, 2)
    RoomType.RoomN -> Room(5, MultiLangText("NARWHAL", "NARWHAL"), this, 3)
}
