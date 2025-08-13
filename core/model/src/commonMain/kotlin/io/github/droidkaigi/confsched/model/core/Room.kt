package io.github.droidkaigi.confsched.model.core

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int,
    val name: MultiLangText,
    val type: RoomType,
    val sort: Int,
) : Comparable<Room> {
    override fun compareTo(other: Room): Int {
        if (sort < 900 && other.sort < 900) {
            return name.currentLangTitle.compareTo(other.name.currentLangTitle)
        }
        return sort.compareTo(other.sort)
    }

    fun getThemeKey(): String = name.enTitle.lowercase()
}

val Room.nameAndFloor: String
    get() {
        val basementFloorString = MultiLangText(jaTitle = "地下1階", enTitle = "B1F")
        val floor1FString = MultiLangText(jaTitle = "1階", enTitle = "1F")
        val floor = when (type) {
            RoomType.RoomF -> floor1FString.currentLangTitle
            RoomType.RoomG -> floor1FString.currentLangTitle
            RoomType.RoomH -> basementFloorString.currentLangTitle
            RoomType.RoomI -> basementFloorString.currentLangTitle
            RoomType.RoomJ -> basementFloorString.currentLangTitle
            // Assume the room on the first day.
            RoomType.RoomIJ -> basementFloorString.currentLangTitle
        }
        return "${name.currentLangTitle} ($floor)"
    }
