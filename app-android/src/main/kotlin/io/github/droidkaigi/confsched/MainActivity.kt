package io.github.droidkaigi.confsched

import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.xr.compose.material3.EnableXrComponentOverrides
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.ContentEdge
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.spatial.OrbiterOffsetType
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.fillMaxSize
import io.github.droidkaigi.confsched.component.GlassLikeNavigationRailBar
import io.github.droidkaigi.confsched.component.LocalNavigationRailOverride
import io.github.droidkaigi.confsched.component.NavigationRailOverride
import io.github.droidkaigi.confsched.component.NavigationRailOverrideScope

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
                    EnableXrKaigiComponentOverrides {
                        KaigiApp()
                    }
                }
            }
        }
    }
}

@Composable
private fun EnableXrKaigiComponentOverrides(
    content: @Composable () -> Unit,
) {
    if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
        CompositionLocalProvider(
            LocalNavigationRailOverride provides XrNavigationRailOverride,
            content = content,
        )
    } else {
        content()
    }
}

private object XrNavigationRailOverride : NavigationRailOverride {
    @OptIn(ExperimentalComposeApi::class)
    @Composable
    override fun NavigationRailOverrideScope.NavigationRail() {
        val cornerSize = CornerSize(percent = 50)
        /**
         * SpatialPanel should not be held here but should be implemented on the screen,
         * but because it causes crashes when combined with Navigation3, SpatialPanel is placed here.
         */
        Subspace {
            SpatialPanel(
                modifier = SubspaceModifier.fillMaxSize(),
            ) {
                // Orbiter is only displayed in SpatialPanel
                Orbiter(
                    position = ContentEdge.Vertical.Start,
                    offsetType = OrbiterOffsetType.OuterEdge,
                    alignment = Alignment.CenterVertically,
                    shape = SpatialRoundedCornerShape(cornerSize),
                ) {
                    GlassLikeNavigationRailBar(
                        hazeState = hazeState,
                        currentTab = currentTab,
                        onTabSelected = onTabSelected,
                        animatedSelectedTabIndex = animatedSelectedTabIndex,
                        animatedColor = animatedColor,
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(cornerSize),
                        ),
                    )
                }
            }
        }
    }
}
