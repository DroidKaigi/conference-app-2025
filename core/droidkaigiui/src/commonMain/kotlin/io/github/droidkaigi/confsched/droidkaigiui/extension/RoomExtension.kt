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
        RoomType.RoomJ -> RoomIcon.Square
        RoomType.RoomK -> RoomIcon.Diamond
        RoomType.RoomL -> RoomIcon.Circle
        RoomType.RoomM -> RoomIcon.Rhombus
        RoomType.RoomN -> RoomIcon.Triangle
    }.toResDrawable()

val Room.roomTheme: RoomTheme
    get() = when (type) {
        RoomType.RoomJ -> RoomTheme.Jellyfish
        RoomType.RoomK -> RoomTheme.Koala
        RoomType.RoomL -> RoomTheme.Ladybug
        RoomType.RoomM -> RoomTheme.Meerkat
        RoomType.RoomN -> RoomTheme.Narwhal
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
