package io.github.droidkaigi.confsched

import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.xr.compose.material3.EnableXrComponentOverrides
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3XrApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(scrim = TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(scrim = TRANSPARENT),
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = true
        }
        super.onCreate(savedInstanceState)

        with(appGraph) {
            setContent {
                EnableXrComponentOverrides {
                    KaigiApp()
                }
            }
        }
    }
}
