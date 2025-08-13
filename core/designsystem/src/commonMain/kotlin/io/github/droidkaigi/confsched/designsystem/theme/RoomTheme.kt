package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

sealed interface RoomTheme {
    val primaryColor: Color
    val containerColor: Color get() = primaryColor.copy(alpha = 0.1f)
    val dimColor: Color

    data object Jellyfish : RoomTheme {
        override val primaryColor = Color(0xFFBCC8F5)
        override val dimColor = Color(0xFF132417)
    }

    data object Koala : RoomTheme {
        override val primaryColor = Color(0xFFDAA3ED)
        override val dimColor = Color(0xFF251C15)
    }

    data object Ladybug : RoomTheme {
        override val primaryColor = Color(0xFFB19BFA)
        override val dimColor = Color(0xFF222213)
    }

    data object Meerkat : RoomTheme {
        override val primaryColor = Color(0xFFB2F5FB)
        override val dimColor = Color(0xFF271A25)
    }

    data object Narwhal : RoomTheme {
        override val primaryColor = Color(0xFF7EE5BD)
        override val dimColor = Color(0xFF121E25)
    }
}

@Suppress("CompositionLocalAllowlist")
val LocalRoomTheme: ProvidableCompositionLocal<RoomTheme> = staticCompositionLocalOf {
    error("No RoomTheme provided")
}

@Composable
fun ProvideRoomTheme(roomTheme: RoomTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalRoomTheme provides roomTheme) {
        content()
    }
}
