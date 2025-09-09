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
            RoomType.RoomJ -> floor1FString.currentLangTitle
            RoomType.RoomK -> floor1FString.currentLangTitle
            RoomType.RoomL -> basementFloorString.currentLangTitle
            RoomType.RoomM -> basementFloorString.currentLangTitle
            RoomType.RoomN -> basementFloorString.currentLangTitle
        }
        return "${name.currentLangTitle} ($floor)"
    }
