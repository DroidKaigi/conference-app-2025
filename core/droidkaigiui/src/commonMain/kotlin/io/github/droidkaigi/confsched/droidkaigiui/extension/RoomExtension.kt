package io.github.droidkaigi.confsched.droidkaigiui.extension

import io.github.droidkaigi.confsched.designsystem.theme.RoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.DroidkaigiuiRes
import io.github.droidkaigi.confsched.droidkaigiui.ic_circle
import io.github.droidkaigi.confsched.droidkaigiui.ic_diamond
import io.github.droidkaigi.confsched.droidkaigiui.ic_rhombus
import io.github.droidkaigi.confsched.droidkaigiui.ic_square
import io.github.droidkaigi.confsched.droidkaigiui.ic_triangle
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.core.RoomIcon
import io.github.droidkaigi.confsched.model.core.RoomType
import org.jetbrains.compose.resources.DrawableResource

val Room.icon: DrawableResource?
    get() = when (type) {
        RoomType.RoomF -> RoomIcon.Rhombus
        RoomType.RoomG -> RoomIcon.Circle
        RoomType.RoomH -> RoomIcon.Diamond
        RoomType.RoomI -> RoomIcon.Square
        RoomType.RoomJ -> RoomIcon.Triangle
        RoomType.RoomIJ -> RoomIcon.None
    }.toResDrawable()

val Room.roomTheme: RoomTheme
    get() = when (type) {
        RoomType.RoomF -> RoomTheme.Meerkat
        RoomType.RoomG -> RoomTheme.Ladybug
        RoomType.RoomH -> RoomTheme.Koala
        RoomType.RoomI -> RoomTheme.Jellyfish
        RoomType.RoomJ -> RoomTheme.Narwhal
        RoomType.RoomIJ -> RoomTheme.Jellyfish
    }

fun RoomIcon.toResDrawable(): DrawableResource? {
    return when (this) {
        RoomIcon.Square -> DroidkaigiuiRes.drawable.ic_square
        RoomIcon.Circle -> DroidkaigiuiRes.drawable.ic_circle
        RoomIcon.Diamond -> DroidkaigiuiRes.drawable.ic_diamond
        RoomIcon.Rhombus -> DroidkaigiuiRes.drawable.ic_rhombus
        RoomIcon.Triangle -> DroidkaigiuiRes.drawable.ic_triangle
        RoomIcon.None -> null
    }
}
