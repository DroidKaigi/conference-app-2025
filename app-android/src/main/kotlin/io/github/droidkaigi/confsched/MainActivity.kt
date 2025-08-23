package io.github.droidkaigi.confsched

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(scrim = TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(scrim = TRANSPARENT),
        )
        super.onCreate(savedInstanceState)

        with(appGraph) {
            setContent {
                KaigiApp()
            }
        }
    }
}
